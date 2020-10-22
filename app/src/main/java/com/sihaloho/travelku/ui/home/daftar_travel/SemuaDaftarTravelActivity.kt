package com.sihaloho.travelku.ui.home.daftar_travel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.TravelModul
import com.sihaloho.travelku.ui.home.daftar_driver.DetailDriverActivity
import com.sihaloho.travelku.utlis.Preferences
import kotlinx.android.synthetic.main.activity_semua_daftar_travel.*

class SemuaDaftarTravelActivity : AppCompatActivity() {

    private var dataListTravel = ArrayList<TravelModul>()
    private lateinit var preferences: Preferences
    lateinit var mDatabaseTravel: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semua_daftar_travel)

        mDatabaseTravel = FirebaseDatabase.getInstance().getReference("DataTravel")
        rv_travel_all.layoutManager = LinearLayoutManager(this.applicationContext)

        getDataTravel("")

        et_search_travel.addTextChangedListener(object : TextWatcher {


            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString()!=null){

                    getDataTravel(s.toString())
                }else{
                    getDataTravel("")
                }
            }
        })

        mDatabaseTravel = FirebaseDatabase.getInstance().getReference("DataTravel")

    }

    private fun getDataTravel(data : String) {
        val query : Query = mDatabaseTravel
            .orderByChild("tujuan")
            .startAt(data)
            .endAt(data+"\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataListTravel.clear()
                for (getdataSnapshot in dataSnapshot.getChildren())
                {
                    val daftarTravel = getdataSnapshot
                        .getValue(TravelModul::class.java)
                    dataListTravel.add(daftarTravel!!)
                }

                if (dataListTravel.isNotEmpty())
                {
                    rv_travel_all.adapter =
                        DaftarTravelAllAdapter(dataListTravel)
                        {
                            val intent = Intent(
                                this@SemuaDaftarTravelActivity,
                                DetailTravelActivity::class.java
                            ).putExtra("data", it)
                            startActivity(intent)
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SemuaDaftarTravelActivity,
                    ""+error.message,
                    Toast.LENGTH_LONG).show()
            }
        })
    }

}
