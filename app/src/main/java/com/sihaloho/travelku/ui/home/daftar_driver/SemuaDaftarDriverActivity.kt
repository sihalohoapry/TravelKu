package com.sihaloho.travelku.ui.home.daftar_driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.UserDriver
import com.sihaloho.travelku.utlis.Preferences
import kotlinx.android.synthetic.main.activity_semua_daftar_driver_aktivity.*

class SemuaDaftarDriverActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    lateinit var mDatabase: DatabaseReference
    private var dataList = ArrayList<UserDriver>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semua_daftar_driver_aktivity)


        preferences = Preferences(this.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("userDriver")
        rv_driver_all.layoutManager = LinearLayoutManager(this.applicationContext)

        getDataDriver("")

        //serach
        et_search_driver.addTextChangedListener(object : TextWatcher{


            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
                if (cs.toString().toLowerCase()!=null){

                    getDataDriver(cs.toString().toLowerCase())
                }else{
                    getDataDriver("")
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })


        mDatabase = FirebaseDatabase.getInstance().getReference("userDriver")

    }




    private fun getDataDriver(data : String) {

        val query : Query = mDatabase
            .orderByChild("nama_penumpang")
            .startAt(data)
            .endAt(data+"\uf8ff")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val daftarUser = getdataSnapshot.getValue(UserDriver::class.java)
                    dataList.add(daftarUser!!)
                }

                if (dataList.isNotEmpty()) {
                    rv_driver_all.adapter =
                        DaftarDriverAllAdapter(dataList) {
                            val intent = Intent(
                                this@SemuaDaftarDriverActivity,
                                DetailDriverActivity::class.java
                            ).putExtra("data", it)
                            startActivity(intent)
                        }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SemuaDaftarDriverActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }


}
