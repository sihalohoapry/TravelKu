package com.sihaloho.travelku.modul

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class user (
    var uid : String ?="",
    var foto : String ?="",
    var email : String ?="",
    var nama_penumpang : String ?="",
    var nik  : String ?="",
    var alamat : String ?="",
    var no_hp : String ?="",
    var user_level : String ?= "",
    var username : String ?= ""
):Parcelable