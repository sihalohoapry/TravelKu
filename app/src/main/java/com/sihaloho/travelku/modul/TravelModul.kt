package com.sihaloho.travelku.modul

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravelModul (
    var uid_driver : String ?="",
    var uid_travel : String ?="",
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
    var no_hp_driver : String ?= "",
    var status_penumpang : String ?= "",
    var status_travel : String ?= "",
    var id : Int = 0

    ): Parcelable