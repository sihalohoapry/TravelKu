package com.sihaloho.travelku


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sihaloho.travelku.activity.booking.DetailPenumpangBookingctivity
import com.sihaloho.travelku.activity.booking.PenumpangBookingAdapter
import com.sihaloho.travelku.modul.BookingPenumpang
import com.sihaloho.travelku.modul.TravelModul
import com.sihaloho.travelku.ui.history.EditJadwalActivity
import kotlinx.android.synthetic.main.activity_detail_travel_dari_history.*
import java.util.*

class DetailTravelDariHistoryActivity : AppCompatActivity() {

    lateinit var data: TravelModul
    lateinit var mDatabaseTravel: DatabaseReference
    private var dataListBooking = ArrayList<BookingPenumpang>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_travel_dari_history)

        btn_pesan2.setOnClickListener {
            startActivity(Intent(this, EditJadwalActivity::class.java))
            finish()
        }

        //cekuser
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refUsers = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUser!!.uid)
        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    btn_pesan.visibility = View.GONE
                    btn_hub_driver.visibility = View.GONE

                    if (firebaseUser.uid == data.uid_driver){
                        btn_pesan2.visibility = View.VISIBLE
                    }


                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        btn_hub_driver.setOnClickListener {
            val nomer = data.no_hp_driver
            val telpon = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$nomer"))
            startActivity(telpon)
        }




        //Get data travel
        data = intent.getParcelableExtra<TravelModul>("data")
        show(data!!)



        rv_penumpang_booking.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        getDataPenumpangBooking()

    }


    private fun show(data: TravelModul) {


        val statusTravel = data.status_travel.toString()
        if ( statusTravel == "Sudah Berangkat"){
            btn_pesan.visibility = View.GONE
        }
        val statusPenumpang = data.status_penumpang.toString()
        if ( statusPenumpang == "Sudah Penuh"){
            btn_pesan.visibility = View.GONE
        }

        Glide.with(this)
            .load(data.foto_mobil)
            .into(imageView9)


        tv_detail_berangkat.text = data.keberangkatan
        tv_detail_tujuan.text = data.tujuan
        tv_detail_tgl.text = data.tanggal
        tv_detail_jam.text = data.jam
        tv_detail_bpkb.text = data.no_polisi
        tv_detail_harga.text = data.Harga
        tv_detail_merek_mobil.text = data.merek_mobil
        tv_detail_namadriver.text = data.user_driver
        tv_detail_statuspenumpang.text = data.status_penumpang
        tv_detail_status_travel.text = data.status_travel
    }



    //untuk rv_penumpang_booking
    private fun getDataPenumpangBooking() {
        val z = data.uid_travel.toString()
        mDatabaseTravel = FirebaseDatabase.getInstance().getReference("DataBooking")
        val query: Query = mDatabaseTravel.orderByChild("uid_travel").startAt(z).endAt(z + "\uF8FF")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataListBooking.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val daftarBooking = getdataSnapshot.getValue(BookingPenumpang::class.java!!)
                    dataListBooking.add(daftarBooking!!)
                }
                if (dataListBooking.isNotEmpty()) {

                    rv_penumpang_booking.adapter = PenumpangBookingAdapter(dataListBooking) {

                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        val refUsers = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUser!!.uid)

                        refUsers.addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(p0: DataSnapshot) {

                                if (p0.exists()){

                                    btn_pesan.visibility = View.GONE
                                    if (firebaseUser.uid == data.uid_driver ){
                                        rv_penumpang_booking.adapter =
                                            PenumpangBookingAdapter(
                                                dataListBooking
                                            ) {
                                                val intent = Intent(
                                                    baseContext,
                                                    DetailPenumpangBookingctivity::class.java
                                                ).putExtra("data", it)
                                                startActivity(intent)
                                            }
                                    }else{
                                        return
                                    }




                                }


                            }

                            override fun onCancelled(p0: DatabaseError) {

                            }

                        })

                    }



                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })

    }

}
