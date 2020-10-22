package com.sihaloho.travelku.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.TravelModul
import kotlinx.android.synthetic.main.activity_edit_jadwal.*
import kotlinx.android.synthetic.main.activity_edit_jadwal.imageView9
import kotlinx.android.synthetic.main.activity_edit_jadwal.tv_detail_berangkat
import kotlinx.android.synthetic.main.activity_edit_jadwal.tv_detail_harga
import kotlinx.android.synthetic.main.activity_edit_jadwal.tv_detail_jam
import kotlinx.android.synthetic.main.activity_edit_jadwal.tv_detail_merek_mobil
import kotlinx.android.synthetic.main.activity_edit_jadwal.tv_detail_namadriver
import kotlinx.android.synthetic.main.activity_edit_jadwal.tv_detail_tgl
import kotlinx.android.synthetic.main.activity_edit_jadwal.tv_detail_tujuan

class EditJadwalActivity : AppCompatActivity() {

    var refUsers: DatabaseReference? = null

    private lateinit var spinnerPenumpang: Spinner
    private lateinit var spinnerTravel: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_jadwal)

        Glide.with(this)
            .load(HistoryFragment.editPotoMobil)
            .into(imageView9)

        tv_detail_berangkat.text = HistoryFragment.editKeberangkatan
        tv_detail_tujuan.text = HistoryFragment.editTujuan
        tv_detail_tgl.text = HistoryFragment.editTgl
        tv_detail_jam.text = HistoryFragment.editJam
        tv_detail_harga.text = HistoryFragment.editHarga
        tv_detail_merek_mobil.text = HistoryFragment.editMerek
        tv_detail_namadriver.text = HistoryFragment.editNamaDriver




        btn_pesan.setOnClickListener {

            simpanDataUser()

        }

    }

    private fun simpanDataUser() {
        val uidTravel = HistoryFragment.uidTravel
        refUsers = FirebaseDatabase.getInstance().reference.child("DataTravel").child(uidTravel)

        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){

                    val dbuser = FirebaseDatabase.getInstance().getReference("DataTravel").child(HistoryFragment.uidTravel)

                    spinnerPenumpang = findViewById(R.id.spinnerPenumpang)
                    spinnerTravel = findViewById(R.id.spinnerTravel)

                    val sp = spinnerPenumpang.selectedItem.toString()
                    val st = spinnerTravel.selectedItem.toString()

                    dbuser.setValue(
                        TravelModul(
                            HistoryFragment.uidDriver,
                            HistoryFragment.uidTravel,
                            HistoryFragment.editPotoMobil,
                            HistoryFragment.editPotoStnk,
                            HistoryFragment.nopolisi,
                            tv_detail_berangkat.text.toString(),
                            tv_detail_tujuan.text.toString(),
                            tv_detail_tgl.text.toString(),
                            tv_detail_jam.text.toString(),
                            tv_detail_harga.text.toString(),
                            tv_detail_merek_mobil.text.toString(),
                            tv_detail_namadriver.text.toString(),
                            HistoryFragment.editNohpdriver,
                            sp,
                            st
                        )
                    )
                        .addOnSuccessListener {
                            Toast.makeText(baseContext,"Status Travel berhasil di ubah", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {

                        }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

}
