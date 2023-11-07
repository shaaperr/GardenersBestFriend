package com.example.gardenersbestfriend

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{

        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "plantjournal.db"
        private const val TBL_PLANT = "tbl_plant"
        private const val ID = "id"
        //private const val PHOTO = "photo"
        private const val NAME = "name"
        private const val REMINDERS = "reminders"
        private const val DATE = "date"
        private const val ENTRY = "entry"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblPlant = ("CREATE TABLE " + TBL_PLANT + "(" + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + DATE + " TEXT," + REMINDERS + " TEXT," + ENTRY + " TEXT" + ")")

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

        val success = db.insert(TBL_PLANT, null, contentValues)
        db.close()
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

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                reminders = cursor.getString(cursor.getColumnIndexOrThrow("reminders"))
                date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                entry = cursor.getString(cursor.getColumnIndexOrThrow("entry"))
                val std = PlantModel(id = id, name = name, reminders = reminders, date = date, entry = entry)
                stdList.add(std)
            }while(cursor.moveToNext())
        }

        return stdList
    }


}