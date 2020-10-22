package com.sihaloho.travelku.activity.signin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.sihaloho.travelku.modul.UserDriver
import kotlinx.android.synthetic.main.activity_sing_up_driver.*
import kotlinx.android.synthetic.main.activity_sing_up_driver.add_photo_penumpang
import kotlinx.android.synthetic.main.activity_sing_up_driver.alamat_penumpang
import kotlinx.android.synthetic.main.activity_sing_up_driver.btn_signin_signup
import kotlinx.android.synthetic.main.activity_sing_up_driver.et_email_signup
import kotlinx.android.synthetic.main.activity_sing_up_driver.et_nama_penumpang
import kotlinx.android.synthetic.main.activity_sing_up_driver.et_pass_signup
import kotlinx.android.synthetic.main.activity_sing_up_driver.iv_poto_penumpang
import kotlinx.android.synthetic.main.activity_sing_up_driver.lvl_user
import kotlinx.android.synthetic.main.activity_sing_up_driver.nik_penumpang
import kotlinx.android.synthetic.main.activity_sing_up_driver.no_hp_penumpang
import java.util.*
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout
import android.app.ProgressDialog


class SignUpDriverActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var urlKTP: TextView
    val PICK_PHOTO = 1010
    val PICK_PHOTO_SIM = 1111
    var FOTO_URI : Uri? = null
    var FOTO_URI_KTP : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.sihaloho.travelku.R.layout.activity_sing_up_driver)

        urlKTP = findViewById<TextView>(com.sihaloho.travelku.R.id.textView24)


        auth = FirebaseAuth.getInstance()

        btn_signin_signup.setOnClickListener{
            signUpUser()
        }

        val floatingUsernameLabel = findViewById(com.sihaloho.travelku.R.id.nik_penumpangg) as TextInputLayout
        floatingUsernameLabel.editText!!.addTextChangedListener(object : TextWatcher {
            // ...
            override fun onTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                if (text.length > 0 && text.length < 16) {
                    floatingUsernameLabel.error = getString(com.sihaloho.travelku.R.string.nik_kontrol)
                    floatingUsernameLabel.isErrorEnabled = true
                } else {
                    floatingUsernameLabel.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        val floatingUsernameLabelPass = findViewById(com.sihaloho.travelku.R.id.et_pass_signupp) as TextInputLayout
        floatingUsernameLabelPass.editText!!.addTextChangedListener(object : TextWatcher {
            // ...
            override fun onTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                if (text.length > 0 && text.length < 6) {
                    floatingUsernameLabelPass.error = getString(com.sihaloho.travelku.R.string.pass_kontrol)
                    floatingUsernameLabelPass.isErrorEnabled = true
                } else {
                    floatingUsernameLabelPass.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        add_photo_penumpang.setOnClickListener {
            getPhotofromphone()
        }
        imageView13.setOnClickListener {
            getPhotofromphoneSIM()
        }

    }

    private fun getPhotofromphoneSIM() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,PICK_PHOTO_SIM)
    }

    private fun getPhotofromphone() {
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
                iv_poto_penumpang.setImageBitmap(bitmap)
                Glide.with(this)
                    .load(FOTO_URI)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_poto_penumpang)
            }
        }
        if (requestCode == PICK_PHOTO_SIM){
            if (resultCode== Activity.RESULT_OK && data!!.data !=null){

                FOTO_URI_KTP =data.data
                val bitmapKTP = MediaStore.Images.Media.getBitmap(contentResolver,FOTO_URI_KTP)
                imageView13.setImageBitmap(bitmapKTP)
                Glide.with(this)
                    .load(FOTO_URI_KTP)
                    .into(imageView13)
            }
        }
    }

    private fun signUpUser(){
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Mohon tunggu...")
        progressBar.show()
        if (et_email_signup.text.toString().isEmpty()){
            progressBar.dismiss()
            et_email_signup.error = "Masukkan Email Kamu"
            et_email_signup.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(et_email_signup.text.toString()).matches()) {
            progressBar.dismiss()
            et_email_signup.error = "Masukkan Valid Email Kamu"
            et_email_signup.requestFocus()
            return
        }

        if (et_pass_signup.text.toString().isEmpty()){
            progressBar.dismiss()
            et_pass_signup.error = "Masukkan Password Kamu"
            et_pass_signup.requestFocus()
            return
        }

        if (et_pass_signup.length()<6){
            progressBar.dismiss()
            et_pass_signup.error = "Masukkan Password Minimal 6 karakter"
            et_pass_signup.requestFocus()
        }
        if (nik_penumpang.length()<16){
            progressBar.dismiss()
            nik_penumpang.error = "NIK mu kurang dari 16"
            nik_penumpang.requestFocus()
        }

        auth.createUserWithEmailAndPassword(et_email_signup.text.toString(), et_pass_signup.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {


                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                progressBar.show()
                                uploadFoto()
                            }
                        }

                } else {

                    progressBar.dismiss()
                    Toast.makeText(baseContext, "Sign up Gagal", Toast.LENGTH_SHORT).show()
                }

            }

    }

    private fun uploadFoto() {

        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Mohon tunggu...")
        progressBar.show()


        val fotoNameKTP = UUID.randomUUID().toString()
        val uploadFirebaseKTP = FirebaseStorage.getInstance().getReference("images/userprofile/$fotoNameKTP")
        uploadFirebaseKTP.putFile(FOTO_URI_KTP!!)
            .addOnSuccessListener {

                uploadFirebaseKTP.downloadUrl.addOnSuccessListener {
                    urlKTP.setText(it.toString())
                    urlKTP.visibility = View.INVISIBLE

                }

            }

        val fotoName = UUID.randomUUID().toString()
        val uploadFirebase = FirebaseStorage.getInstance().getReference("images/userprofile/$fotoName")

        uploadFirebase.putFile(FOTO_URI!!)
            .addOnSuccessListener {

                uploadFirebase.downloadUrl.addOnSuccessListener {
                    simpanDataUser(it.toString())
                }

            }


    }

    private fun simpanDataUser(fotoURL : String) {


        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Mohon tunggu...")
        progressBar.show()
        val uid_driver = FirebaseAuth.getInstance().uid
        val db = FirebaseDatabase.getInstance().getReference("userDriver/$uid_driver")

        db.setValue(
            UserDriver(
                uid_driver.toString(),
                fotoURL,
                et_email_signup.text.toString(),
                nama_travel.text.toString(),
                et_nama_penumpang.text.toString().toLowerCase(),
                nik_penumpang.text.toString(),
                urlKTP.text.toString(),
                alamat_penumpang.text.toString(),
                no_hp_penumpang.text.toString(),
                lvl_user.text.toString()



            )
        )
            .addOnSuccessListener {
                progressBar.dismiss()
                Toast.makeText(baseContext, "Sign Up berhasil di lakukan \nsilahkan tunggu verifikasi dari admin",
                    Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {

            }
    }



}
