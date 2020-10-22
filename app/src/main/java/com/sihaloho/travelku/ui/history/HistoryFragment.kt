package com.sihaloho.travelku.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.sihaloho.travelku.DetailTravelDariHistoryActivity
import com.sihaloho.travelku.R
import com.sihaloho.travelku.activity.booking.DetailPenumpangBookingctivity
import com.sihaloho.travelku.activity.booking.PenumpangBookingAdapter
import com.sihaloho.travelku.database.MappingHelper
import com.sihaloho.travelku.database.PesanankuHelper
import com.sihaloho.travelku.modul.*
import com.sihaloho.travelku.ui.home.daftar_penumpang.DaftarPenumpangAdapter
import com.sihaloho.travelku.ui.home.daftar_penumpang.DaftarPenumpangAllActivity
import com.sihaloho.travelku.ui.home.daftar_penumpang.DetailPenumpangActivity
import com.sihaloho.travelku.ui.home.daftar_travel.BuatJadwalTravelActivity
import com.sihaloho.travelku.ui.home.daftar_travel.DaftarTravelAdapter
import com.sihaloho.travelku.ui.home.daftar_travel.DetailTravelActivity
import kotlinx.android.synthetic.main.activity_detail_travel.*
//import com.sihaloho.travelku.database.PesanankuHelper
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {


    private lateinit var favmovAdapter: HistoryPemesananAdapter
    private lateinit var movFavHelper: PesanankuHelper

    lateinit var mDatabaseTravel: DatabaseReference
    private var dataListBooking = java.util.ArrayList<TravelModul>()


    var refUsersDriver: DatabaseReference? = null
    var firebaseUserDriver: FirebaseUser? = null

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
        lateinit var uidTravel : String
        lateinit var uidDriver : String
        lateinit var editKeberangkatan : String
        lateinit var editHarga : String
        lateinit var editJam : String
        lateinit var editTgl : String
        lateinit var editNamaDriver : String
        lateinit var editTujuan : String
        lateinit var editPotoMobil : String
        lateinit var editPotoStnk : String
        lateinit var editNohpdriver : String
        lateinit var editMerek : String
        lateinit var nopolisi : String

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_history_pemesanan.layoutManager = LinearLayoutManager(activity)
        rv_history_pemesanan.setHasFixedSize(true)
        favmovAdapter = HistoryPemesananAdapter()
        rv_history_pemesanan.adapter = favmovAdapter

        movFavHelper = PesanankuHelper.getInstance(context!!)
        movFavHelper.open()

        favmovAdapter.setOnItemClickCallback(object : HistoryPemesananAdapter.OnItemClickCallback{
            override fun onItemClicked(data: TravelModulFavDB) {
                showSelectedData(data)

            }

        })

        if (savedInstanceState == null){
            loadMoviesAsync()
        } else{
            val list = savedInstanceState.getParcelableArrayList<TravelModulFavDB>(EXTRA_STATE)
            if(list != null){
                favmovAdapter.listMovie = list
            }
        }


        firebaseUserDriver = FirebaseAuth.getInstance().currentUser
        refUsersDriver = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUserDriver!!.uid)

        refUsersDriver!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {


                if (p0.exists()){
                    val userDriver: UserDriver = p0.getValue(UserDriver::class.java)!!

                    rv_history_jadwal.layoutManager = LinearLayoutManager(context!!.applicationContext)
                    rv_history_pemesanan.visibility = View.GONE

                    val z = userDriver.uid_driver.toString()
                    mDatabaseTravel = FirebaseDatabase.getInstance().getReference("DataTravel")
                    val query: Query = mDatabaseTravel.orderByChild("uid_driver").startAt(z).endAt(z + "\uF8FF")

                    query.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataListBooking.clear()
                            for (getdataSnapshot in dataSnapshot.getChildren()) {

                                val daftarBooking = getdataSnapshot.getValue(TravelModul::class.java)
                                dataListBooking.add(daftarBooking!!)
                            }
                            if (dataListBooking.isNotEmpty()) {

                                rv_history_jadwal.adapter =
                                    HistoryJadwalAdapter(
                                        dataListBooking
                                    ) {
                                        uidTravel = it.uid_travel.toString()
                                        uidDriver = it.uid_driver.toString()
                                        editKeberangkatan = it.keberangkatan.toString()
                                        editTujuan = it.tujuan.toString()
                                        editHarga = it.Harga.toString()
                                        editTgl = it.tanggal.toString()
                                        editJam = it.jam.toString()
                                        editNamaDriver = it.user_driver.toString()
                                        editPotoMobil = it.foto_mobil.toString()
                                        editPotoStnk = it.foto_STNK.toString()
                                        editNohpdriver = it.no_hp_driver.toString()
                                        editMerek = it.merek_mobil.toString()
                                        nopolisi = it.no_polisi.toString()
                                        val intent = Intent(
                                            context,
                                            DetailTravelDariHistoryActivity::class.java
                                        ).putExtra("data", it)
                                        startActivity(intent)

                                    }

                            }else{
                                return
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })


                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })



    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favmovAdapter.listMovie)
    }

    private fun loadMoviesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredMovies = async(Dispatchers.IO) {
                val cursor = movFavHelper.queryAll()
                Log.d("isi cursor", "cursor: ${cursor.count}")
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val movies = deferredMovies.await()
            if (movies.size > 0) {
                favmovAdapter.listMovie = movies
            } else {
                favmovAdapter.listMovie = ArrayList()
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        movFavHelper.close()
    }

    private fun showSelectedData(movie: TravelModulFavDB) {

        val moveObjectIntent = Intent(activity, HistoryPemesananActivity::class.java)
        moveObjectIntent.putExtra(HistoryPemesananActivity.EXTRA_MOVIE, movie)
        startActivity(moveObjectIntent)
    }

}