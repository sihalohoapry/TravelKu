package com.sihaloho.travelku.modul

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MasukanUser (
    var uid_masukan : String ?="",
    var uid_user : String ?="",
    var email : String ?="",
    var nama_user : String ?="",
    var masukan : String ?=""
):Parcelable