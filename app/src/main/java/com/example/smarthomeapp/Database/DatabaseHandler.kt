package com.example.smarthomeapp.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.smarthomeapp.Models.RoutineModel

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "RoutineDatabase"
        private val TABLE_ROUTINES = "RoutineTable"

        private val KEY_ID = "_id"
        private val KEY_NAME = "routine_name"
        private val KEY_LAST_RUN = "last_run"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_ROUTINES_TABLE = ("CREATE TABLE " + TABLE_ROUTINES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT," + KEY_LAST_RUN + " TEXT" + ")")
        db?.execSQL(CREATE_ROUTINES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTINES")
        onCreate(db)
    }

    //function to insert data
    fun addRoutine(routine: RoutineModel): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, routine.routineName) //RoutineName class routineName
        contentValues.put(KEY_LAST_RUN, routine.lastRun) //RoutineName class lastRun

        //Inserting employee details using insert query
        val success = db.insert(TABLE_ROUTINES, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() //Closing database connection
        return success
    }

    //Method to read the records from database in form of ArrayList
    fun viewRoutine(): ArrayList<RoutineModel>{
        val routineList: ArrayList<RoutineModel> = ArrayList<RoutineModel>()

        //Query to select all the records from the table
        val selectQuery = "SELECT * FROM $TABLE_ROUTINES"

        val db = this.readableDatabase
        //Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var routineName: String
        var lastRun: String

        if (cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                routineName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                lastRun = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_RUN))

                val routine = RoutineModel(id = id, routineName = routineName, lastRun = lastRun )

                routineList.add(routine)
            }while (cursor.moveToNext())
        }
        return routineList

    }
}