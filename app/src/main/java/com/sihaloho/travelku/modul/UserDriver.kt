package com.sihaloho.travelku.modul

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDriver (
    var uid_driver : String ?="",
    var foto : String ?="",
    var email : String ?="",
    var nama_travel : String ?="",
    var nama_penumpang : String ?="",
    var nik  : String ?="",
    var foto_ktp  : String ?="",
    var alamat : String ?="",
    var no_hp : String ?="",
    var user_level_driver : String ?=""
): Parcelable