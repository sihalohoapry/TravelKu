package com.sihaloho.travelku.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.UserDriver
import com.sihaloho.travelku.modul.user
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_ganti_foto.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class GantiFotoActivity : AppCompatActivity() {

    val PICK_PHOTO = 100
    var FOTO_URI : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganti_foto)
        showLoading(false)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refUsers = FirebaseDatabase.getInstance().reference.child("user").child(firebaseUser!!.uid)
        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val user: user = p0.getValue(user::class.java)!!

                    Glide.with(baseContext)
                        .load(user.foto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(iv_ganti)

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })


        val firebaseUserDriver = FirebaseAuth.getInstance().currentUser
        val refUsersDriver = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUserDriver!!.uid)
        refUsersDriver.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val user: UserDriver = p0.getValue(UserDriver::class.java)!!

                    Glide.with(baseContext)
                        .load(user.foto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(iv_ganti)

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        btn_tambah.setOnClickListener {
            ambilFoto()
        }

        btn_ganti.setOnClickListener {
            showLoading(true)
            upload()
        }
    }

    private fun upload() {
        showLoading(true)

        val fotoName = UUID.randomUUID().toString()
        val uploadFirebase = FirebaseStorage.getInstance().getReference("images/userprofile/$fotoName")

        uploadFirebase.putFile(FOTO_URI!!)
            .addOnSuccessListener {

                uploadFirebase.downloadUrl.addOnSuccessListener {
                    simpanUpdate(it.toString())
                }

            }
    }

    private fun simpanUpdate(fotoURL: String) {

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refUsers = FirebaseDatabase.getInstance().reference.child("user").child(firebaseUser!!.uid)
        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){

                    val user: user = p0.getValue(user::class.java)!!
                    Glide.with(this@GantiFotoActivity)
                        .load(user.foto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(iv_ganti)
                    val foto = fotoURL

                    val dbuser = FirebaseDatabase.getInstance().getReference("user").child(user.uid!!)

                    dbuser.setValue(
                        user(
                            user.uid,
                            foto,
                            user.email,
                            user.nama_penumpang,
                            user.nik,
                            user.alamat,
                            user.no_hp,
                            user.user_level
                        )
                    )
                        .addOnSuccessListener {
                            showLoading(false)
                            Toast.makeText(baseContext,"Data Berhasil di Edit", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {

                        }


                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })


        val firebaseUserDriver = FirebaseAuth.getInstance().currentUser
        val refUsersDriver = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUserDriver!!.uid)
        refUsersDriver.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){

                    val user: UserDriver = p0.getValue(UserDriver::class.java)!!
                    Glide.with(this@GantiFotoActivity)
                        .load(user.foto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(iv_ganti)
                    val foto = fotoURL

                    val dbuser = FirebaseDatabase.getInstance().getReference("userDriver").child(user.uid_driver!!)

                    dbuser.setValue(
                        UserDriver(
                            user.uid_driver,
                            foto,
                            user.email,
                            user.nama_travel,
                            user.nama_penumpang,
                            user.nik,
                            user.foto_ktp,
                            user.alamat,
                            user.no_hp,
                            user.user_level_driver
                        )
                    )
                        .addOnSuccessListener {
                            showLoading(false)
                            Toast.makeText(baseContext,"Data Berhasil di Edit", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {

                        }


                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }

    private fun ambilFoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO){
            if (resultCode== Activity.RESULT_OK && data!!.data !=null){

                FOTO_URI =data.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,FOTO_URI)
                iv_ganti.setImageBitmap(bitmap)
                Glide.with(this)
                    .load(FOTO_URI)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_ganti)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progresbar_signup2.visibility = View.VISIBLE
        } else {
            progresbar_signup2.visibility = View.GONE
        }
    }


}
