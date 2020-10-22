package com.sihaloho.travelku.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.sihaloho.travelku.database.DatabaseContract.PesananKu.Companion.ID
import com.sihaloho.travelku.database.DatabaseContract.PesananKu.Companion.TABLE_NAME
import java.sql.SQLException

class PesanankuHelper (context: Context) {
    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databasePesanankuHelper: DatabaseHelper
        private var INSTANCE: PesanankuHelper? = null

        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): PesanankuHelper{
            if(INSTANCE == null){
                INSTANCE = PesanankuHelper(context)
            }
            return INSTANCE as PesanankuHelper
        }

    }

    init {
        databasePesanankuHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open(){
        database = databasePesanankuHelper.writableDatabase
    }
    fun close(){
        databasePesanankuHelper.close()

        if(database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }


    fun deleteById(id: String): Int{
        return database.delete(TABLE_NAME, "$ID = '$id'", null)
    }

}