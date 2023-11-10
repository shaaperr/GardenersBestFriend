package com.example.gardenersbestfriend

//import androidx.recyclerview.widget.LinearLayoutManager
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNewPlantActivity : AppCompatActivity() {
    //database stuff
    private lateinit var newPlantName: EditText
    private lateinit var waterScheduleText: TextView
    private lateinit var date: EditText
    private lateinit var entry: EditText
    private lateinit var sqLiteHelper: SQLiteHelper
    private var plantUri = Uri.EMPTY


    //reminder stuff
    val daysOfWeek = arrayOf("Mondays", "Tuesdays", "Wednesdays", "Thursdays", "Fridays", "Saturdays", "Sundays")
    lateinit var selectedDay: BooleanArray
    lateinit var textView: TextView
    private val selectedItems = HashSet<Int>() // Keep track of selected items
  //  val dbUrl = "jdbc:sqlite:com/example/gardenersbestfriend/PhotoJournalDatabase.kt"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_plant)


      initView()
     // initRecyclerView()
      sqLiteHelper = SQLiteHelper(this)


      //reminder stuff
      val notificationButton = findViewById<Button>(R.id.submitButton)
      notificationButton.setOnClickListener {
          addPlant()

          val intent = Intent(this, Reminders::class.java)

          //CUSTOM NOTIFS
          val name = newPlantName.text.toString()

          intent.putExtra(messageExtra, name) // 'name' is the name of the plant

          val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
          val now = Calendar.getInstance()


          // Create a set to keep track of the alarms to be scheduled
          val alarmsToSchedule = HashSet<Int>()

          // Schedule alarms for the selected days
          for (dayOfWeek in selectedItems) {
              val selectedDayString = daysOfWeek[dayOfWeek]
              val today = Calendar.getInstance()
              val dayOfWeekToday = today.get(Calendar.DAY_OF_WEEK)
              val daysUntilSelectedDay = (getDayOfWeekValue(selectedDayString) - dayOfWeekToday + 7) % 7

              // Calculate the next occurrence for the selected day
              today.add(Calendar.DAY_OF_YEAR, daysUntilSelectedDay)
              val calendar = Calendar.getInstance()
              calendar.timeInMillis = today.timeInMillis
              calendar.set(Calendar.HOUR_OF_DAY, 20) //Time alarm will go off for each upcoming day
              calendar.set(Calendar.MINUTE, 34)
              calendar.set(Calendar.SECOND, 0)

              // Check if the time is in the past on the same day, add 7 days if it is
              if (today.get(Calendar.DAY_OF_WEEK) == dayOfWeekToday && calendar.before(now)) {
                  calendar.add(Calendar.DAY_OF_YEAR, 7)
              }



              val scheduledTime = calendar.time
              Log.d("AlarmScheduling", "Scheduled time for $selectedDayString: $scheduledTime")

              // Create a new PendingIntent to ensure a new alarm is scheduled
              val newPendingIntent = PendingIntent.getBroadcast(
                  this, dayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT
              )


              alarmManager.setRepeating(
                  AlarmManager.RTC_WAKEUP,
                  calendar.timeInMillis,
                  AlarmManager.INTERVAL_DAY * 7, // Ensures that it repeats every week.
                  newPendingIntent
              )

              alarmsToSchedule.add(dayOfWeek)
          }

          // Cancel alarms for unselected days, ends up cancelling alarms that don't exist might work on it more
          for (dayOfWeek in (0 until 7).toSet() - alarmsToSchedule) {
              val pendingIntent = PendingIntent.getBroadcast(
                  this, dayOfWeek, intent,
                  PendingIntent.FLAG_UPDATE_CURRENT
              )
              alarmManager.cancel(pendingIntent)
              Log.d("AlarmCancelling", "Canceled alarm for ${daysOfWeek[dayOfWeek]}")
          }

      }

      //Drop down menu, populates textview
      textView = findViewById(R.id.waterScheduleText)
      textView.setOnClickListener {
          val builder = AlertDialog.Builder(this)
          builder.setTitle("Set Weekly Reminders")
          builder.setCancelable(false)

          selectedDay = BooleanArray(daysOfWeek.size) { selectedItems.contains(it) }

          builder.setMultiChoiceItems(daysOfWeek, selectedDay) { _, i, b ->
              if (b) {
                  selectedItems.add(i)
              } else {
                  selectedItems.remove(i)
              }
          }

          builder.setPositiveButton("Ok") { _, _ ->
              val stringBuilder = StringBuilder()

              for (item in selectedItems) {
                  stringBuilder.append(daysOfWeek[item])
                  stringBuilder.append(", ")
              }

              if (stringBuilder.isNotEmpty()) {
                  stringBuilder.setLength(stringBuilder.length - 2)
              }

              textView.text = stringBuilder.toString()
          }

          builder.setNegativeButton("Cancel") { dialogInterface, _ ->
              dialogInterface.dismiss()
          }

          builder.setNeutralButton("Clear all") { _, _ ->
              selectedItems.clear()
              textView.text = ""
          }
          builder.show()
      }

        val plantImagePreview: ImageButton = findViewById(R.id.plantImagePreview)

      val useCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { imageTaken ->
          val TAG = "Camera"
          if (imageTaken) {
              plantImagePreview.setImageURI(plantUri)
              Log.d(TAG, "Picture taken: $plantUri")
          }
          else {
              Log.d(TAG, "No picture taken")
          }
      }

      val pickDocument = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
          if (result.resultCode == Activity.RESULT_OK) {
              val data: Intent? = result.data
              val uri: Uri? = data?.data
              val tag = "ImagePicker"
              if (uri != null) {
                  plantImagePreview.setImageURI(uri)
                  plantUri = uri
                  Log.d(tag, "Selected image: $uri")
              } else {
                  Log.d(tag, "No image selected")
              }
          }
      }

      val addPlantImage = PopupMenu(this, plantImagePreview)
      addPlantImage.inflate(R.menu.popup_add_plant_image)

      plantImagePreview.setOnClickListener {
          addPlantImage.show()
      }

      /*val createImageUri = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
          val TAG = "ImageCreation"

          if (result.resultCode == Activity.RESULT_OK) {
              val data: Intent? = result.data
              val uri: Uri? = data?.data
              if (uri != null) {
                  plantUri = uri
                  useCamera.launch(plantUri)
                  Log.d(TAG, "File created: $plantUri")
              }
              else {
                  Log.d(TAG, "No file created")
              }
          }
      }*/

      addPlantImage.setOnMenuItemClickListener { menuItem ->
          val id = menuItem.itemId
          if (id == R.id.useCamera) {
              plantUri = createImageFile()
              Log.d("GetUri", "plantUri: $plantUri")
              useCamera.launch(plantUri)

          } else if (id == R.id.openGallery) {          //HAD TO MODIFY FOR DATABASE, Permissions were failing for some reason, gets pics from documents
              val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
              intent.addCategory(Intent.CATEGORY_OPENABLE)
              intent.type = "image/*"
              pickDocument.launch(intent)
          }
          false
      }

  }

    private fun getPlant() {
        val plantList = sqLiteHelper.getAllPlant()
        Log.e("Show added plant", "${plantList.size}")

    }

    private fun copyImageToInternalStorage(context: Context, uri: Uri?): String {
        // Create a folder in the app's internal storage
        val folder = File(context.filesDir, "plant_images")
        if (!folder.exists()) {
            folder.mkdir()
        }

        // Copy the image to the folder and return the path, had to save pictures to app, permissions were failing
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageName = "IMG_$timeStamp.jpg"
        val destinationFile = File(folder, imageName)

        try {
            context.contentResolver.openInputStream(uri ?: return "")?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    val buffer = ByteArray(4 * 1024) // 4k buffer
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            }
            Log.d("ImageCopy", "Image copied to internal storage: ${destinationFile.absolutePath}")
            return destinationFile.absolutePath
        } catch (e: IOException) {
            Log.e("ImageCopy", "Error copying image to internal storage", e)
        }

        return ""
    }

    private fun addPlant() {
        val name = newPlantName.text.toString()
        val date = date.text.toString()
        val entry = entry.text.toString()
        val reminders = waterScheduleText.text.toString()

        if (name.isEmpty() || date.isEmpty() || entry.isEmpty()) {
            Toast.makeText(this, "Please fill out the required fields", Toast.LENGTH_SHORT).show()
        } else {
            val std = PlantModel(name = name, reminders = reminders, date = date, entry = entry)

            // Copy the image to internal storage and get the path
            val imagePath = copyImageToInternalStorage(this, plantUri)
            Log.d("ImagePath", "Image path: $imagePath")

            // Check if the file exists at the specified path
            val file = File(imagePath)
            if (file.exists()) {
                Log.d("FileExists", "Image file exists at path: $imagePath")
            } else {
                Log.e("FileExists", "Image file not found at path: $imagePath")
            }
            if (imagePath.isNotEmpty()) {
                std.imagepath = imagePath
            }

            val status = sqLiteHelper.insertPlant(std)

            if (status > -1) {
                Toast.makeText(this, "Plant Added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Submission failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        newPlantName = findViewById(R.id.newPlantName)
        waterScheduleText = findViewById(R.id.waterScheduleText)
        date = findViewById(R.id.date)
        entry = findViewById(R.id.entry)

    }

    private fun createImageFile(): Uri {
        val context = applicationContext
        val TAG = "ImageCreation"
        val provider = "${context.packageName}.provider"
        val fileName = "JPEG_${System.currentTimeMillis()}"
        val imagePath = File(context.filesDir, "plant_images")
        Log.d(TAG, "createImageFile: ${imagePath.exists()}")
        Log.d(TAG, "createImageFile: ${imagePath.absolutePath}")
        if (!imagePath.mkdirs()){
            Log.d(TAG, "createImageFile: Failed to create directory")
        }
        val tempFile = File.createTempFile(fileName, ".jpg", imagePath)
        return FileProvider.getUriForFile(context, provider, tempFile)
    }
}


private fun getDayOfWeekValue(dayString: String): Int {
    return when (dayString) {
        "Sundays" -> Calendar.SUNDAY
        "Mondays" -> Calendar.MONDAY
        "Tuesdays" -> Calendar.TUESDAY
        "Wednesdays" -> Calendar.WEDNESDAY
        "Thursdays" -> Calendar.THURSDAY
        "Fridays" -> Calendar.FRIDAY
        "Saturdays" -> Calendar.SATURDAY
        else -> -1 // Invalid value
    }
}



