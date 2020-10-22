@file:Suppress("DEPRECATION")

package com.sihaloho.travelku.ui.home.daftar_travel

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.sihaloho.travelku.R
import com.sihaloho.travelku.activity.MainActivity
import com.sihaloho.travelku.modul.TravelModul
import com.sihaloho.travelku.modul.UserDriver
import kotlinx.android.synthetic.main.activity_buat_jadwal_travel.*
import java.text.SimpleDateFormat
import java.util.*

class BuatJadwalTravelActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var spinnerPenumpang: Spinner
    private lateinit var spinnerTravel: Spinner

    val PICK_PHOTO_STNK = 101
    val PICK_PHOTO_BPKB = 102
    val PICK_PHOTO_MOBIL = 103
    var FOTO_URI_STNK: Uri? = null
    var FOTO_URI_BPKB: Uri? = null
    var FOTO_URI_MOBIL: Uri? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buat_jadwal_travel)

        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)


        et_tanggal_input.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                view:DatePicker, mYear: Int , mMonth:Int , mDay:Int ->
                et_tanggal_input.setText(""+ mDay +"/" + mMonth +"/"+ mYear)
            }, year,month,day)
            dpd.show()
        }
        imageView30.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                view:DatePicker, mYear: Int , mMonth:Int , mDay:Int ->
                et_tanggal_input.setText(""+ mDay +"/" + mMonth +"/"+ mYear)
            }, year,month,day)
            dpd.show()
        }
        imageView31.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{view: TimePicker?, hourOfDay: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                et_jam_input.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE), true).show()
        }
        et_jam_input.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{view: TimePicker?, hourOfDay: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                et_jam_input.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE), true).show()
        }



        auth = FirebaseAuth.getInstance()

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUser!!.uid)

        refUsers!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                    val userDriver: UserDriver = p0.getValue(UserDriver::class.java)!!

                tv_buat_jadwal_user.text = userDriver.nama_penumpang.toString()
                tv_buatjadwal_lvluser.text = userDriver.no_hp.toString()
                Glide.with(baseContext)
                    .load(userDriver.foto)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_buatjadwal_profil)

            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })



        iv_stnk.setOnClickListener {
            getPhotofromphone()
        }



        iv_foto_mobil.setOnClickListener {
            getPhotofromphoneMOBIL()
        }

        btn_upload_foto.setOnClickListener {
            simpandata()
        }


        btn_simpan.setOnClickListener {

            uploadAllData()

        }

    }

    private fun getPhotofromphoneMOBIL() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO_MOBIL)
    }

    private fun getPhotofromphoneBPKB() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO_BPKB)
    }

    private fun uploadAllData() {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Mohon tunggu...")
        progressBar.show()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUser!!.uid)
        val uid = FirebaseAuth.getInstance().uid
        val uid_travel = UUID.randomUUID().toString()

        val db = FirebaseDatabase.getInstance().getReference("DataTravel/$uid_travel")

        spinnerPenumpang = findViewById(R.id.spinner_status_penumpang)
        spinnerTravel = findViewById(R.id.spinner_status_travel)

        val sp = spinnerPenumpang.selectedItem.toString()
        val st = spinnerTravel.selectedItem.toString()

        db.setValue(
            TravelModul(
                uid.toString(),
                uid_travel,
                tv_mobil.text.toString(),
                tv_stnk.text.toString(),
                no_polisi.text.toString(),
                et_keberangkatan_input.text.toString(),
                et_tujuan_input.text.toString(),
                et_tanggal_input.text.toString(),
                et_jam_input.text.toString(),
                et_harga_input.text.toString(),
                et_merek_mobil_input.text.toString(),
                tv_buat_jadwal_user.text.toString(),
                tv_buatjadwal_lvluser.text.toString(),
                sp,
                st
            )
        )
            .addOnSuccessListener {

                progressBar.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {

            }
    }


    private fun simpandata() {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Mohon tunggu...")
        progressBar.show()

        if (et_keberangkatan_input.text.toString().isEmpty()) {
            progressBar.dismiss()
            et_keberangkatan_input.error = "Masukkan Keberangkatan"
            et_keberangkatan_input.requestFocus()
            return
        }

        if (et_tujuan_input.text.toString().isEmpty()) {
            progressBar.dismiss()
            et_tujuan_input.error = "Masukkan Tujuan"
            et_tujuan_input.requestFocus()
            return
        }

        if (et_tanggal_input.text.toString().isEmpty()) {
            progressBar.dismiss()
            et_tanggal_input.error = "Masukkan Tanggal"
            et_tanggal_input.requestFocus()
            return
        }

        if (et_jam_input.text.toString().isEmpty()) {
            progressBar.dismiss()
            et_jam_input.error = "Masukkan Jam"
            et_jam_input.requestFocus()
            return
        }
        if (et_harga_input.text.toString().isEmpty()) {
            progressBar.dismiss()
            et_harga_input.error = "Masukkan Harga"
            et_harga_input.requestFocus()
            return
        }
        if (et_merek_mobil_input.text.toString().isEmpty()) {
            progressBar.dismiss()
            et_merek_mobil_input.error = "Masukkan Merek Mobil"
            et_merek_mobil_input.requestFocus()
            return
        }

        if (iv_stnk.toString().isEmpty()) {
            progressBar.dismiss()
            Toast.makeText(this, "Masukkan Foto STNK anda", Toast.LENGTH_SHORT).show()
            return
        }

//        if (iv_bpkb.toString().isEmpty()) {
//            progressBar.dismiss()
//            Toast.makeText(this, "Masukkan Foto BPKB anda", Toast.LENGTH_SHORT).show()
//            return
//        }

        if (iv_foto_mobil.toString().isEmpty()) {
            progressBar.dismiss()
            Toast.makeText(this, "Masukkan Foto Mobil anda", Toast.LENGTH_SHORT).show()
            return
        } else {
            progressBar.dismiss()
            uploadFoto()
        }


    }

    private fun uploadFotoMOBIL() {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Mohon tunggu...")
        progressBar.show()
        val fotoMobil = UUID.randomUUID().toString()
        val uploadFirebaseee =
            FirebaseStorage.getInstance().getReference("images/datatravel/$fotoMobil")

        uploadFirebaseee.putFile(FOTO_URI_MOBIL!!)
            .addOnSuccessListener {

                uploadFirebaseee.downloadUrl.addOnSuccessListener {
                    tv_mobil.visibility = View.INVISIBLE
                    Toast.makeText(baseContext, "Foto berhasil di upload, silahkan klik tombol simpan", Toast.LENGTH_SHORT).show()
                    progressBar.dismiss()
                    tv_mobil.setText(it.toString())
                    btn_upload_foto.visibility = View.GONE
                    btn_simpan.visibility = View.VISIBLE

                }
            }
    }




    private fun uploadFoto() {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Mohon tunggu...")
        progressBar.show()
        val fotostnk = UUID.randomUUID().toString()
        val uploadFirebase =
            FirebaseStorage.getInstance().getReference("images/datatravel/$fotostnk")

        uploadFirebase.putFile(FOTO_URI_STNK!!)
            .addOnSuccessListener {

                uploadFirebase.downloadUrl.addOnSuccessListener {

                    tv_stnk.visibility = View.INVISIBLE
                    tv_stnk.setText(it.toString())
                    progressBar.dismiss()
                    uploadFotoMOBIL()

                }

            }


    }


    private fun getPhotofromphone() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO_STNK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO_STNK) {
            if (resultCode == Activity.RESULT_OK && data!!.data != null) {

                FOTO_URI_STNK = data.data
                val bitmap_stnk = MediaStore.Images.Media.getBitmap(contentResolver, FOTO_URI_STNK)
                iv_stnk.setImageBitmap(bitmap_stnk)
                Glide.with(this)
                    .load(FOTO_URI_STNK)
                    .into(iv_stnk)


            }

        }



        if (requestCode == PICK_PHOTO_MOBIL) {
            if (resultCode == Activity.RESULT_OK && data!!.data != null) {
                FOTO_URI_MOBIL = data.data
                val bitmap_mobil =
                    MediaStore.Images.Media.getBitmap(contentResolver, FOTO_URI_MOBIL)
                iv_foto_mobil.setImageBitmap(bitmap_mobil)
                Glide.with(this)
                    .load(FOTO_URI_MOBIL)
                    .into(iv_foto_mobil)

            }

        }


    }

}
