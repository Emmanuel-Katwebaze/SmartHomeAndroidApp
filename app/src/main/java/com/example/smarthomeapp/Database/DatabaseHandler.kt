package com.example.smarthomeapp.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.smarthomeapp.Models.RoutineModel

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "RoutineDatabase"
        private val TABLE_ROUTINES = "RoutineTable"
        private val TABLE_FAVORITES = "FavoritesTable"

        private val KEY_ID = "_id"
        private val KEY_NAME = "routine_name"
        private val KEY_TIME = "time"
        private val KEY_NOTIFICATION = "notification"
        private val KEY_LOCATION = "location"
        private val KEY_LAST_RUN = "last_run"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Drop the existing table if it exists
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTINES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")

        // Creating table with fields
        val CREATE_ROUTINES_TABLE =
            ("CREATE TABLE " + TABLE_ROUTINES + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_NAME + " TEXT," +
                    KEY_TIME + " TEXT," +
                    KEY_NOTIFICATION + " TEXT," +
                    KEY_LOCATION + " TEXT," +
                    KEY_LAST_RUN + " TEXT" +
                    ")")

        // Creating table with fields
        val CREATE_FAVORITES_TABLE =
            ("CREATE TABLE " + TABLE_FAVORITES + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_NAME + " TEXT," +
                    KEY_TIME + " TEXT," +
                    KEY_NOTIFICATION + " TEXT," +
                    KEY_LOCATION + " TEXT," +
                    KEY_LAST_RUN + " TEXT" +
                    ")")

        db?.execSQL(CREATE_ROUTINES_TABLE)
        db?.execSQL(CREATE_FAVORITES_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTINES")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        onCreate(db)
    }

    //function to insert data
    fun addRoutine(routine: RoutineModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, routine.routineName)
        contentValues.put(KEY_TIME, routine.time)
        contentValues.put(KEY_NOTIFICATION, routine.notification)
        contentValues.put(KEY_LOCATION, routine.location)
        contentValues.put(KEY_LAST_RUN, routine.lastRun)

        //Inserting employee details using insert query
        val success = db.insert(TABLE_ROUTINES, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() //Closing database connection
        return success
    }
    fun addFavoriteRoutine(routine: RoutineModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, routine.routineName)
        contentValues.put(KEY_TIME, routine.time)
        contentValues.put(KEY_NOTIFICATION, routine.notification)
        contentValues.put(KEY_LOCATION, routine.location)
        contentValues.put(KEY_LAST_RUN, routine.lastRun)

        //Inserting employee details using insert query
        val success = db.insert(TABLE_FAVORITES, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() //Closing database connection
        return success
    }

    //Method to read the records from database in form of ArrayList
    fun viewRoutine(): ArrayList<RoutineModel> {
        val routineList: ArrayList<RoutineModel> = ArrayList<RoutineModel>()

        //Query to select all the records from the table
        val selectQuery = "SELECT * FROM $TABLE_ROUTINES"

        val db = this.readableDatabase
        //Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var routineName: String
        var time: String
        var notification: String
        var location: String
        var lastRun: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                routineName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                time = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME))
                notification = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTIFICATION))
                location = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION))
                lastRun = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_RUN))

                val routine =
                    RoutineModel(
                        id = id,
                        routineName = routineName,
                        time = time,
                        notification = notification,
                        location = location,
                        lastRun = lastRun
                    )

                routineList.add(routine)
            } while (cursor.moveToNext())
        }
        return routineList

    }
    fun viewFavorites(): ArrayList<RoutineModel> {
        val favoritesList: ArrayList<RoutineModel> = ArrayList<RoutineModel>()

        //Query to select all the records from the table
        val selectQuery = "SELECT * FROM $TABLE_FAVORITES"

        val db = this.readableDatabase
        //Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var routineName: String
        var time: String
        var notification: String
        var location: String
        var lastRun: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                routineName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                time = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME))
                notification = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTIFICATION))
                location = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION))
                lastRun = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_RUN))

                val routine =
                    RoutineModel(
                        id = id,
                        routineName = routineName,
                        time = time,
                        notification = notification,
                        location = location,
                        lastRun = lastRun
                    )

                favoritesList.add(routine)
            } while (cursor.moveToNext())
        }
        return favoritesList

    }

    /**
     * Function to update record
     */
    fun updateRoutine(routine: RoutineModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, routine.routineName)
        contentValues.put(KEY_TIME, routine.time)
        contentValues.put(KEY_NOTIFICATION, routine.notification)
        contentValues.put(KEY_LOCATION, routine.location)
        contentValues.put(KEY_LAST_RUN, routine.lastRun)

        // Updating Row
        val success = db.update(TABLE_ROUTINES, contentValues, KEY_ID + "=" + routine.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    /**
     * Function to delete record
     */
    fun deleteRoutine(routine: RoutineModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, routine.id) // RoutineModelClass id
        // Deleting Row
        val success = db.delete(TABLE_ROUTINES, KEY_ID + "=" + routine.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }
    fun deleteFavorite(routine: RoutineModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, routine.id) // RoutineModelClass id
        // Deleting Row
        val success = db.delete(TABLE_FAVORITES, KEY_ID + "=" + routine.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    fun getLastInsertedId(): Int {
        val db = this.readableDatabase
        var lastInsertedId: Int = -1

        val query = "SELECT MAX($KEY_ID) FROM $TABLE_ROUTINES"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            lastInsertedId = cursor.getInt(0)
        }
        cursor.close()
        db.close()

        return lastInsertedId
    }


    fun getAllIdsFromDatabase(): List<Int> {
        val idList = mutableListOf<Int>()
        val db = this.readableDatabase

        // Query the database to retrieve all IDs
        val selectQuery = "SELECT $KEY_ID FROM $TABLE_ROUTINES"
        val cursor = db.rawQuery(selectQuery, null)

        // Iterate over the cursor and add IDs to the list
        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                idList.add(id)
            }
        }

        // Return the list of IDs
        return idList
    }


}