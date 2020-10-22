package com.sihaloho.travelku.database

import android.database.Cursor
import android.util.Log
import com.sihaloho.travelku.modul.TravelModulFavDB

object MappingHelper {
    fun mapCursorToArrayList(moviesCursor: Cursor): ArrayList<TravelModulFavDB> {
        val movieList = ArrayList<TravelModulFavDB>()
        while (moviesCursor.moveToNext()){
            val foto_mobil =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.FOTOMOBIL))
            val foto_STNK =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.FOTOSTNK))
            val no_polisi =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.NOPOLISI))
            val keberangkatan =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.KEBERANGKATAN))
            val tujuan =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.TUJUAN))
            val tanggal =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.TANGGAL))
           val jam =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.JAM))
            val Harga =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.HARGA))
            val merek_mobil =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.MEREKMOBIL))
             val user_driver =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.USERDRIVER))
             val uid_driver =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.UIDDRIVER))
            val no_driver =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.NODRIVER))
            val id =
                moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(DatabaseContract.PesananKu.ID))

            movieList.add(
                TravelModulFavDB(
                    foto_mobil,
                    foto_STNK,
                    no_polisi,
                    keberangkatan,
                    tujuan,
                    tanggal,
                    jam,
                    Harga,
                    merek_mobil,
                    user_driver,
                    uid_driver,
                    no_driver,
                    id
                )
            )
        }
        Log.d("isi cursor", "movieList: ${movieList.count()}")
        return movieList
    }
}