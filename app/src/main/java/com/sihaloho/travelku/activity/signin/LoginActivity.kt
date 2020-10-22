package com.sihaloho.travelku.activity.signin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.sihaloho.travelku.R
import com.sihaloho.travelku.activity.MainActivity
import com.sihaloho.travelku.ui.home.daftar_travel.BuatJadwalTravelActivity
import com.sihaloho.travelku.modul.UserDriver
import com.sihaloho.travelku.utlis.Preferences
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var preferences: Preferences
    lateinit var mdatabase: DatabaseReference

    private lateinit var prBar: ProgressBar


    var refUsers: DatabaseReference? = null
    var refUsersDrivers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    var firebaseUserDriver: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prBar = findViewById(R.id.progresbarr_signin)

        auth = FirebaseAuth.getInstance()

        preferences = Preferences(this)
        preferences.setValues("onboarding", "1")
        if (preferences.getValues("status").equals("1")) {
            finishAffinity()

            val intent = Intent(
                this@LoginActivity,
                MainActivity::class.java
            )
            startActivity(intent)
        }
        if (preferences.getValues("status").equals("12")) {
            finishAffinity()

            val intent = Intent(
                this@LoginActivity,
                BuatJadwalTravelActivity::class.java
            )
            startActivity(intent)
        }
        btn_signin.setOnClickListener {
            showLoading(true)
            doLogin()
        }
        btn_signup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            overridePendingTransition(R.anim.slide_in, R.anim.slide_in_out)

        }

        btn_signup_driver.setOnClickListener {
            startActivity(Intent(this, SignUpDriverActivity::class.java))
            overridePendingTransition(R.anim.slide_in, R.anim.slide_in_out)

        }

        tv_lupa_pass.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Lupa Password")
            val view = layoutInflater.inflate(R.layout.dialog_lupa_password, null)
            val username = view.findViewById<EditText>(R.id.et_username)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { dialog, which ->
                lupaPass(username)
            })
            builder.setNegativeButton(
                "Keluar",
                DialogInterface.OnClickListener { dialog, which -> })
            builder.show()
        }

    }

    private fun lupaPass(username: EditText) {
        if (username.text.toString().isEmpty()) {
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        }

        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->



                if (task.isSuccessful) {
                    Toast.makeText(this, "Mengirim Email.", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun doLogin() {

        if (et_email.text.toString().isEmpty()) {
            showLoading(false)
            et_email.error = "Masukkan Email Kamu"
            et_email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches()) {
            showLoading(false)
            et_email.error = "Masukkan Valid Email Kamu"
            et_email.requestFocus()
            return
        }

        if (et_pass.text.toString().isEmpty()) {
            showLoading(false)
            et_pass.error = "Masukkan Password Kamu"
            et_pass.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(et_email.text.toString(), et_pass.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    updateUI(user)
                } else {

                    updateUI(null)
                }

            }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {
            if (currentUser.isEmailVerified) {

                firebaseUserDriver = FirebaseAuth.getInstance().currentUser
                refUsersDrivers = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUserDriver!!.uid)

                refUsersDrivers!!.addValueEventListener(object  : ValueEventListener{

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val userDriver: UserDriver? = p0.getValue(UserDriver::class.java)

                            if (userDriver!!.user_level_driver.toString() == ("Driver Travel")) {
                                loginDriver()
                            } else {
                                showLoading(false)
                                Toast.makeText(
                                    baseContext, "Gagal Login Sebagai Driver \ntunggu" +
                                            " verifikasi dari admin ", Toast.LENGTH_SHORT
                                ).show()
                                return
                            }
                        }

                    }
                    override fun onCancelled(p0: DatabaseError) {


                    }
                })


                firebaseUser = FirebaseAuth.getInstance().currentUser
                refUsers = FirebaseDatabase.getInstance().reference.child("user").child(firebaseUser!!.uid)

                refUsers!!.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(penumpang: DataSnapshot) {

                        if (penumpang.exists()){

                            showLoading(false)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            preferences.setValues("status", "1")
                            finish()
                        }

                    }

                    override fun onCancelled(penumpang: DatabaseError) {

                    }

                })

            } else {
                showLoading(false)
                Toast.makeText(
                    baseContext, "Verifikasi email anda",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            showLoading(false)
            Toast.makeText(
                baseContext, "Login Gagal.",
                Toast.LENGTH_SHORT
            ).show()

        }

    }



    private fun loginDriver() {
        showLoading(false)
        startActivity(Intent(this, MainActivity::class.java))
        preferences.setValues("status", "1")
        finish()
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            prBar.visibility = View.VISIBLE
        } else {
            prBar.visibility = View.GONE
        }
    }

}

