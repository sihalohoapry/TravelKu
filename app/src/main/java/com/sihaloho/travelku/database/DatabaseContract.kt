package com.sihaloho.travelku.database

import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.sihaloho.travelku.database"
    const val SCHEME = "content"

    internal class PesananKu: BaseColumns {
        companion object{
            const val TABLE_NAME = "pesananku"
            const val ID = "_id"
            const val FOTOMOBIL = "foto_mobil"
            const val FOTOSTNK = "foto_STNK"
            const val NOPOLISI = "no_polisi"
            const val KEBERANGKATAN = "keberangkatan"
            const val TUJUAN = "tujuan"
            const val TANGGAL = "tanggal"
            const val JAM = "jam"
            const val HARGA = "Harga"
            const val MEREKMOBIL = "merek_mobil"
            const val USERDRIVER = "user_driver"
            const val UIDDRIVER = "uid_driver"
            const val NODRIVER = "no_driver"


            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }
    }


    fun getColumnString(cursor: Cursor, columName: String): String{
        return cursor.getString(cursor.getColumnIndex(columName))
    }
    fun getColumnInt(cursor: Cursor, columName: String): Int{
        return cursor.getInt(cursor.getColumnIndex(columName))
    }
    fun getColumDouble(cursor: Cursor, columName: String): Double{
        return cursor.getDouble(cursor.getColumnIndex(columName))
    }
}