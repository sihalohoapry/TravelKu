package com.sihaloho.travelku.modul

import android.database.Cursor
import android.os.Parcelable
import com.sihaloho.travelku.database.DatabaseContract
import com.sihaloho.travelku.database.DatabaseContract.PesananKu.Companion.ID
import com.sihaloho.travelku.database.DatabaseContract.getColumDouble
import com.sihaloho.travelku.database.DatabaseContract.getColumnInt
import com.sihaloho.travelku.database.DatabaseContract.getColumnString
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravelModulFavDB (
    var foto_mobil : String ?="",
    var foto_STNK : String ?="",
    var no_polisi : String ?="",
    var keberangkatan : String ?="",
    var tujuan : String ?="",
    var tanggal  : String ?="",
    var jam : String ?="",
    var Harga : String ?="",
    var merek_mobil : String ?= "",
    var user_driver : String ?= "",
    var uid_driver : String ?= "",
    var no_driver : String ?= "",
    var id : Int = 0
): Parcelable{
    constructor(cursor: Cursor) : this(){
        this.foto_mobil = getColumnString(cursor, DatabaseContract.PesananKu.FOTOMOBIL)
        this.foto_STNK = getColumnString(cursor, DatabaseContract.PesananKu.FOTOSTNK)
        this.no_polisi = getColumnString(cursor, DatabaseContract.PesananKu.NOPOLISI)
        this.keberangkatan = getColumnString(cursor, DatabaseContract.PesananKu.KEBERANGKATAN)
        this.tujuan = getColumnString(cursor, DatabaseContract.PesananKu.TUJUAN)
        this.tanggal = getColumnString(cursor, DatabaseContract.PesananKu.TANGGAL)
        this.jam = getColumnString(cursor, DatabaseContract.PesananKu.JAM)
        this.Harga = getColumnString(cursor, DatabaseContract.PesananKu.HARGA)
        this.user_driver = getColumnString(cursor, DatabaseContract.PesananKu.USERDRIVER)
        this.uid_driver = getColumnString(cursor, DatabaseContract.PesananKu.UIDDRIVER)
        this.no_driver = getColumnString(cursor, DatabaseContract.PesananKu.NODRIVER)
        this.id = getColumnInt(cursor, ID)
    }
}
