package com.sihaloho.travelku.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sihaloho.travelku.database.DatabaseContract.PesananKu.Companion.TABLE_NAME

internal class DatabaseHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "dbpesananku"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.PesananKu.ID} INTEGER PRIMARY KEY," +
                "${DatabaseContract.PesananKu.FOTOMOBIL} TEXT NOT NULL," +
                "${DatabaseContract.PesananKu.FOTOSTNK} TEXT NOT NULL," +
                "${DatabaseContract.PesananKu.NOPOLISI} TEXT NOT NULL," +
                "${DatabaseContract.PesananKu.KEBERANGKATAN} TEXT NOT NULL," +
                "${DatabaseContract.PesananKu.TUJUAN} TEXT NOT NULL," +
                "${DatabaseContract.PesananKu.TANGGAL} TEXT NOT NULL," +
                "${DatabaseContract.PesananKu.JAM} TEXT NOT NULL," +
                "${DatabaseContract.PesananKu.HARGA} TEXT NOT NULL," +
                "${DatabaseContract.PesananKu.MEREKMOBIL} TEXT NOT NULL," +
                "${DatabaseContract.PesananKu.USERDRIVER} TEXT NOT NULL,"+
                "${DatabaseContract.PesananKu.UIDDRIVER} TEXT NOT NULL,"+
                "${DatabaseContract.PesananKu.NODRIVER} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}