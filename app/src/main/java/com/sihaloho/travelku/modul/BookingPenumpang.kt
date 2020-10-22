package com.sihaloho.travelku.modul

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookingPenumpang (
    var idbooking : String ?="",
    var uid_penumpang_booking : String ?="",
    var uid_travel : String ?="",
    var nama_penumpang_booking : String ?="",
    var foto_penumpang_booking : String ?="",
    var nama_driver : String ?="",
    var tanggal_berangkat : String ?="",
    var jam_berangkat : String ?="",
    var berangkat : String ?="",
    var tujuan : String ?="",
    var no_hp_penumpang : String ?="",
    var uid_driver : String ?="",
    var lokasijemput : String ?=""
):Parcelable