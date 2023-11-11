package com.example.gardenersbestfriend

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SQLiteHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{

        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "plantjournal.db"
        private const val TBL_PLANT = "tbl_plant"
        private const val ID = "id"
        private const val IMAGE_PATH = "imagepath"
        private const val NAME = "name"
        private const val REMINDERS = "reminders"
        private const val DATE = "date"
        private const val ENTRY = "entry"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblPlant = ("CREATE TABLE " + TBL_PLANT + "(" + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + DATE + " TEXT," + REMINDERS + " TEXT," + ENTRY + " TEXT," + IMAGE_PATH + " TEXT" + ")")
        db?.execSQL(createTblPlant)
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_PLANT")
        onCreate(db)
    }


    fun insertPlant(std:PlantModel): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(REMINDERS, std.reminders)
        contentValues.put(DATE, std.date)
        contentValues.put(ENTRY, std.entry)
        contentValues.put(IMAGE_PATH, std.imagepath)

        val success = db.insert(TBL_PLANT, null, contentValues)
        db.close()
        Log.d("SQLiteHelper", "Image path stored in the database: ${std.imagepath}")

        return success
    }
    fun getAllPlant(): ArrayList<PlantModel>{
        val stdList: ArrayList<PlantModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_PLANT"
        val db = this.readableDatabase
        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch(e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var name:String
        var reminders:String
        var date:String
        var entry:String
        var imagepath:String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                reminders = cursor.getString(cursor.getColumnIndexOrThrow("reminders"))
                date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                entry = cursor.getString(cursor.getColumnIndexOrThrow("entry"))
                imagepath = cursor.getString(cursor.getColumnIndexOrThrow("imagepath"))
                val std = PlantModel(id = id, name = name, reminders = reminders, date = date, entry = entry, imagepath = imagepath)
                stdList.add(std)
            }while(cursor.moveToNext())
        }

        return stdList
    }

    fun getOneById(id: Int): PlantModel? {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TBL_PLANT WHERE $ID = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(id.toString()))

        var plant: PlantModel? = null

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
            val reminders = cursor.getString(cursor.getColumnIndexOrThrow(REMINDERS))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
            val entry = cursor.getString(cursor.getColumnIndexOrThrow(ENTRY))
            val imagepath = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PATH))

            plant = PlantModel(id, name, reminders, date, entry, imagepath)
        }

        cursor.close()
        return plant
    }

    fun updatePlant(plant: PlantModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, plant.name)
        contentValues.put(REMINDERS, plant.reminders)
        contentValues.put(DATE, plant.date)
        contentValues.put(ENTRY, plant.entry)
        contentValues.put(IMAGE_PATH, plant.imagepath)

        val success = db.update(TBL_PLANT, contentValues, "$ID=?", arrayOf(plant.id.toString()))
        db.close()

        return success
    }


    fun getEntriesForPlantId(id: Int): List<PlantModel> {
        val entries = mutableListOf<PlantModel>()
        val selectQuery = "SELECT $DATE, $ENTRY FROM $TBL_PLANT WHERE $ID = ?"
        val cursor = readableDatabase.rawQuery(selectQuery, arrayOf(id.toString()))

        try {
            if (cursor.moveToFirst()) {
                do {
                    val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
                    val entry = cursor.getString(cursor.getColumnIndexOrThrow(ENTRY))

                    // Create a new PlantModel with only date and entry
                    val plant = PlantModel(id = id, date = date, entry = entry)
                    entries.add(plant)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
        }

        return entries
    }





}