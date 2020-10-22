package com.sihaloho.travelku.ui.home.daftar_penumpang

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
import com.sihaloho.travelku.modul.user
import com.sihaloho.travelku.ui.home.daftar_driver.DaftarDriverAllAdapter
import com.sihaloho.travelku.ui.home.daftar_driver.DetailDriverActivity
import com.sihaloho.travelku.utlis.Preferences
import kotlinx.android.synthetic.main.activity_daftar_penumpang_all.*
import kotlinx.android.synthetic.main.activity_semua_daftar_driver_aktivity.*

class DaftarPenumpangAllActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    lateinit var mDatabase: DatabaseReference
    private var dataList = ArrayList<user>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_penumpang_all)

        preferences = Preferences(this.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("user")
        rv_penumpang_all.layoutManager = LinearLayoutManager(this.applicationContext)

        getDataPenumpang("")

        et_search_penumpang.addTextChangedListener(object : TextWatcher {


            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString()!=null){

                    getDataPenumpang(s.toString().toLowerCase())
                }else{
                    getDataPenumpang("")
                }
            }
        })


        mDatabase = FirebaseDatabase.getInstance().getReference("user")

    }

    private fun getDataPenumpang(data : String) {

        val query : Query = mDatabase.orderByChild("nama_penumpang").startAt(data).endAt(data+"\uf8ff")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val daftarUser = getdataSnapshot.getValue(user::class.java)
                    dataList.add(daftarUser!!)
                }

                if (dataList.isNotEmpty()) {
                    rv_penumpang_all.adapter =
                        DaftarPenumpangAllAdapter(dataList) {
                            val intent = Intent(
                                this@DaftarPenumpangAllActivity,
                                DetailPenumpangActivity::class.java
                            ).putExtra("data", it)
                            startActivity(intent)
                        }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DaftarPenumpangAllActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }


}
