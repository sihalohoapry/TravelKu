package com.sihaloho.travelku.activity.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.user
import com.sihaloho.travelku.ui.home.daftar_travel.DetailTravelActivity
import kotlinx.android.synthetic.main.activity_booking_sukses.*
class BookingSuksesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_sukses)

        tv_sukses_idbooking.text = DetailTravelActivity.uid_booking
        tv_sukses_nama_driver.text = DetailTravelActivity.nama_driver
        tv_sukses_tanggal.text = DetailTravelActivity.tanggal
        tv_sukses_jam.text = DetailTravelActivity.jam
        tv_sukses_berangkat.text = DetailTravelActivity.berangkat
        tv_sukses_tujuan.text = DetailTravelActivity.tujuan


        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("user/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val user: user = p0.getValue(user::class.java)!!

                    tv_sukses_nama_penumpang.text = user.nama_penumpang

                    Glide.with(this@BookingSuksesActivity)
                        .load(user.foto)
                        .into(iv_sukses_profile)
                }


            }

        })


    }
}
