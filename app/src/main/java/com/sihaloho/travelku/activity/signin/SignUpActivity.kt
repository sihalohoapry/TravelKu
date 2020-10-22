package com.sihaloho.travelku.activity.signin

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.user
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    val PICK_PHOTO = 101
    var FOTO_URI : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        auth = FirebaseAuth.getInstance()

        btn_signin_signup.setOnClickListener{
            signUpUser()
        }

        add_photo_penumpang.setOnClickListener {
            getPhotofromphone()
        }

        val floatingUsernameLabel = findViewById(R.id.nik_penumpangg) as TextInputLayout
        floatingUsernameLabel.editText!!.addTextChangedListener(object : TextWatcher {
            // ...
            override fun onTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                if (text.length > 0 && text.length < 16) {
                    floatingUsernameLabel.error = getString(R.string.nik_kontrol)
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

        val floatingUsernameLabelPass = findViewById(R.id.et_pass_signupp) as TextInputLayout
        floatingUsernameLabelPass.editText!!.addTextChangedListener(object : TextWatcher {
            // ...
            override fun onTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                if (text.length > 0 && text.length < 6) {
                    floatingUsernameLabelPass.error = getString(R.string.pass_kontrol)
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


    }

    private fun getPhotofromphone() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO){
            if (resultCode== RESULT_OK && data!!.data !=null){

                FOTO_URI =data.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,FOTO_URI)
                iv_poto_penumpang.setImageBitmap(bitmap)
                Glide.with(this)
                    .load(FOTO_URI)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_poto_penumpang)
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
                                progressBar.dismiss()
                                uploadFoto()
                            }
                        }

                } else {
                    progressBar.dismiss()
                    Toast.makeText(baseContext, "Sign up Gagal",
                        Toast.LENGTH_SHORT).show()
                }

            }

    }

    private fun uploadFoto() {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Mohon tunggu...")
        progressBar.show()
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

        val uid = FirebaseAuth.getInstance().uid
        val db = FirebaseDatabase.getInstance().getReference("user/$uid")

        db.setValue(
            user(
                uid.toString(),
                fotoURL,
                et_email_signup.text.toString(),
                et_nama_penumpang.text.toString().toLowerCase(),
                nik_penumpang.text.toString(),
                alamat_penumpang.text.toString(),
                no_hp_penumpang.text.toString(),
                lvl_user.text.toString(),
                et_username_signup.text.toString()


            ))
            .addOnSuccessListener {
                progressBar.dismiss()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {

            }
    }



}
