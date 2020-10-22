package com.sihaloho.travelku.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.UserDriver
import com.sihaloho.travelku.modul.user
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    var refUsersDriver: DatabaseReference? = null
    var firebaseUserDriver: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        showLoading(false)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("user").child(firebaseUser!!.uid)
        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val user: user = p0.getValue(user::class.java)!!

                    et_nama_penumpang.setText(user.username)
                    no_hp_penumpang.setText(user.no_hp)
                    alamat_penumpang.setText(user.alamat)



                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })


        firebaseUserDriver = FirebaseAuth.getInstance().currentUser
        refUsersDriver = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUserDriver!!.uid)
        refUsersDriver!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val user: UserDriver = p0.getValue(UserDriver::class.java)!!

                    nama_user_penumpangg.hint = "Nama Travel"
                    et_nama_penumpang.setText(user.nama_travel)
                    no_hp_penumpang.setText(user.no_hp)
                    alamat_penumpang.setText(user.alamat)

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })


        btn_signin_signup.setOnClickListener {

            showLoading(true)

            simpanDataUser()

        }

    }


    private fun simpanDataUser() {

        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val user: user = p0.getValue(user::class.java)!!
                    val nama = et_nama_penumpang.text.toString().trim()
                    val no = no_hp_penumpang.text.toString().trim()
                    val alamat = alamat_penumpang.text.toString().trim()

                    val dbuser = FirebaseDatabase.getInstance().getReference("user").child(user.uid!!)

                    dbuser.setValue(user(
                        user.uid,
                        user.foto,
                        user.email,
                        user.nama_penumpang,
                        user.nik,
                        alamat,
                        no,
                        user.user_level,
                        nama
                    ))
                        .addOnSuccessListener {
                            showLoading(false)
                            finish()
                        }
                        .addOnFailureListener {

                        }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        refUsersDriver!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val user: UserDriver = p0.getValue(UserDriver::class.java)!!
                    val nama = et_nama_penumpang.text.toString().trim()
                    val no = no_hp_penumpang.text.toString().trim()
                    val alamat = alamat_penumpang.text.toString().trim()

                    val dbuser = FirebaseDatabase.getInstance().getReference("userDriver").child(user.uid_driver!!)

                    dbuser.setValue(UserDriver(
                        user.uid_driver,
                        user.foto,
                        user.email,
                        nama,
                        user.nama_penumpang,
                        user.nik,
                        user.foto_ktp,
                        alamat,
                        no,
                        user.user_level_driver
                    ))
                        .addOnSuccessListener {
                            showLoading(false)
                            finish()
                        }
                        .addOnFailureListener {

                        }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

    }


    private fun showLoading(state: Boolean) {
        if (state) {
            progresbar_signup.visibility = View.VISIBLE
        } else {
            progresbar_signup.visibility = View.GONE
        }
    }
}
