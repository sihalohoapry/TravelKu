package com.sihaloho.travelku.activity.booking

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.BookingPenumpang
import com.sihaloho.travelku.modul.TravelModul
import kotlinx.android.synthetic.main.activity_detail_penumpang_bookingctivity.*
import kotlinx.android.synthetic.main.activity_detail_travel.*

class DetailPenumpangBookingctivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_penumpang_bookingctivity)

        val data = intent.getParcelableExtra<BookingPenumpang>("data")

        Glide.with(this)
            .load(data.foto_penumpang_booking)
            .into(imageView12)

        tv_detailpenumpangbooking_nama.text = data.nama_penumpang_booking
        tv_detailpenumpangbooking_tgl.text = data.tanggal_berangkat
        tv_detailpenumpangbooking_jam.text = data.jam_berangkat
        tv_detailpenumpangbooking_driver.text = data.nama_driver
        tv_penumpangbooking_id.text = data.idbooking
        tv_detailpenumpangbooking_nohp.text = data.no_hp_penumpang
        tv_detailpenumpangbooking_lokasijemput.text = data.lokasijemput

        tv_detailpenumpangbooking_hub.setOnClickListener {
            val nomer = data.no_hp_penumpang
            val telpon = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$nomer"))
            startActivity(telpon)
        }


    }
}
