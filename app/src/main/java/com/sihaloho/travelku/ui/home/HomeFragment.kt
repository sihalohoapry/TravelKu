package com.sihaloho.travelku.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId

import com.sihaloho.travelku.R
import com.sihaloho.travelku.ui.home.daftar_travel.BuatJadwalTravelActivity
import com.sihaloho.travelku.modul.TravelModul
import com.sihaloho.travelku.modul.UserDriver
import com.sihaloho.travelku.modul.user
import com.sihaloho.travelku.notificationbooking.Token
import com.sihaloho.travelku.ui.home.daftar_driver.DaftarDriverAdapter
import com.sihaloho.travelku.ui.home.daftar_driver.DetailDriverActivity
import com.sihaloho.travelku.ui.home.daftar_driver.SemuaDaftarDriverActivity
import com.sihaloho.travelku.ui.home.daftar_penumpang.DaftarPenumpangAdapter
import com.sihaloho.travelku.ui.home.daftar_penumpang.DaftarPenumpangAllActivity
import com.sihaloho.travelku.ui.home.daftar_penumpang.DetailPenumpangActivity
import com.sihaloho.travelku.ui.home.daftar_travel.DaftarTravelAdapter
import com.sihaloho.travelku.ui.home.daftar_travel.DetailTravelActivity
import com.sihaloho.travelku.ui.home.daftar_travel.SemuaDaftarTravelActivity
import com.sihaloho.travelku.utlis.Preferences
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var preferences: Preferences

    private var dataListPenumpang = ArrayList<user>()
    lateinit var mDatabasePenumpang: DatabaseReference

    private var dataList = ArrayList<UserDriver>()
    lateinit var mDatabase: DatabaseReference
    lateinit var uid_Driver: String


    private var dataListTravel = ArrayList<TravelModul>()
    lateinit var mDatabaseTravel: DatabaseReference


    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    var refUsersDriver: DatabaseReference? = null
    var firebaseUserDriver: FirebaseUser? = null

    lateinit var mAdView : AdView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        tv_lihat_semua_travel.setOnClickListener {
            startActivity(Intent(activity, SemuaDaftarTravelActivity::class.java))
        }


        preferences = Preferences(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("userDriver")
        mDatabasePenumpang = FirebaseDatabase.getInstance().getReference("user")
        mDatabaseTravel = FirebaseDatabase.getInstance().getReference("DataTravel")


        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("user").child(firebaseUser!!.uid)

        firebaseUserDriver = FirebaseAuth.getInstance().currentUser
        refUsersDriver = FirebaseDatabase.getInstance().reference.child("userDriver")
            .child(firebaseUserDriver!!.uid)



        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    val user: user = p0.getValue(user::class.java)!!

                    tv_nama_user.text = user.username
                    tv_user_level_home.text = user.user_level

                    Glide.with(activity!!)
                        .load(user.foto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView6)

                    rv_travel.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    getDataTravel()

                    rv_driver.layoutManager = LinearLayoutManager(context!!.applicationContext)
                    getData()

                    tv_daftar_penumpang.visibility = View.INVISIBLE
                    btn_buat_jadwal.visibility = View.INVISIBLE
                    liat_semua.setOnClickListener {
                        startActivity(Intent(activity, SemuaDaftarDriverActivity::class.java))
                    }


                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })


        refUsersDriver!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    val userDriver: UserDriver = p0.getValue(UserDriver::class.java)!!
                    tv_nama_user.text = userDriver.nama_travel
                    tv_user_level_home.text = userDriver.user_level_driver

                    uid_Driver = userDriver.uid_driver.toString()

                    Glide.with(activity!!)
                        .load(userDriver.foto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView6)


                    rv_travel.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    getDataTravel()

                    rv_driver.layoutManager = LinearLayoutManager(context!!.applicationContext)
                    getDataPenumpang()

                    textView16.visibility = View.INVISIBLE

                    liat_semua.setOnClickListener {
                        startActivity(Intent(activity, DaftarPenumpangAllActivity::class.java))
                    }
                    btn_buat_jadwal.setOnClickListener {
                        startActivity(Intent(activity, BuatJadwalTravelActivity::class.java))
                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        updateToken(FirebaseInstanceId.getInstance().token)



    }

    private fun updateToken(token: String?) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
        val token1 = Token(token!!)
        ref.child(firebaseUser!!.uid).setValue(token1)
    }  

    private fun getDataTravel() {


        mDatabaseTravel.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                    dataListTravel.clear()
                    for (getdataSnapshot in dataSnapshot.getChildren()) {

                        val daftarTravel = getdataSnapshot.getValue(TravelModul::class.java)
                        dataListTravel.add(daftarTravel!!)
                    }
                    if (dataListTravel.isNotEmpty()) {
                        rv_travel.adapter =
                            DaftarTravelAdapter(
                                dataListTravel
                            ) {
                                val intent = Intent(
                                    context,
                                    DetailTravelActivity::class.java
                                ).putExtra("data", it)
                                startActivity(intent)
                            }

                    } else{
                        return
                    }





            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun getDataPenumpang() {
        mDatabasePenumpang.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataListPenumpang.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val daftarPenumpang = getdataSnapshot.getValue(user::class.java)
                    dataListPenumpang.add(daftarPenumpang!!)
                }

                if (dataListPenumpang.isNotEmpty()) {
                    rv_driver.adapter =
                        DaftarPenumpangAdapter(dataListPenumpang) {
                            val intent = Intent(
                                context,
                                DetailPenumpangActivity::class.java
                            ).putExtra("data", it)
                            startActivity(intent)
                        }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val daftardriver = getdataSnapshot.getValue(UserDriver::class.java)
                    dataList.add(daftardriver!!)
                }

                if (dataList.isNotEmpty()) {
                    rv_driver.adapter =
                        DaftarDriverAdapter(dataList) {
                            val intent = Intent(
                                context,
                                DetailDriverActivity::class.java
                            ).putExtra("data", it)
                            startActivity(intent)
                        }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}