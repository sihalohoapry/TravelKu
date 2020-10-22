package com.sihaloho.travelku.ui.profile


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.sihaloho.travelku.activity.signin.LoginActivity
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.UserDriver
import com.sihaloho.travelku.modul.user
import com.sihaloho.travelku.utlis.Preferences
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    lateinit var preferences: Preferences

    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    var refUsersDriver: DatabaseReference? = null
    var firebaseUserDriver: FirebaseUser? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        btn_ganti_pass.setOnClickListener {
            gantiPassword()
        }

        btn_profile_edit.setOnClickListener {
            startActivity(Intent(context,EditProfileActivity::class.java))
        }

        btn_logout.setOnClickListener {
            preferences.setValues("status", "2")
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }
        preferences = Preferences(activity!!)


        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("user").child(firebaseUser!!.uid)
        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val user: user = p0.getValue(user::class.java)!!

                    tv_profile_nama.text = user.username
                    textView54.text = user.nama_penumpang
                    tv_profile_no_hp.text = user.no_hp
                    tv_provfile_email.text = user.email
                    tv_profile_alamat.text = user.alamat
                    tv_profile_nik.text = user.nik

                    Glide.with(activity!!)
                        .load(user.foto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(iv_profile)


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


                    tv_profile_nama.text = user.nama_travel
                    textView54.text = user.nama_penumpang
                    tv_profile_no_hp.text = user.no_hp
                    tv_provfile_email.text = user.email
                    tv_profile_alamat.text = user.alamat
                    tv_profile_nik.text = user.nik

                    Glide.with(activity!!)
                        .load(user.foto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(iv_profile)


                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })


        add_photo_penumpang2.setOnClickListener {
            startActivity(Intent(context,GantiFotoActivity::class.java))
        }



    }



    private fun gantiPassword() {
        if (et_pass_lama.text.isNotEmpty() &&
            et_pass_baru.text.isNotEmpty() &&
            et_pass_baru_ulang.text.isNotEmpty()
        ) {

            if (et_pass_baru.text.toString().equals(et_pass_baru_ulang.text.toString())) {

                val user = FirebaseAuth.getInstance().currentUser
                if (user != null && user.email != null) {

                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, et_pass_lama.text.toString())

                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(activity, "Berhasil Mengganti Password", Toast.LENGTH_SHORT)
                                    .show()
                                user.updatePassword(et_pass_baru.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            preferences.setValues("status", "2")
                                            Toast.makeText(view!!.context, "Berhasil Mengganti Password", Toast.LENGTH_SHORT)
                                                .show()
                                        auth.signOut()
                                            startActivity(Intent(activity, LoginActivity::class.java))
                                            activity!!.finish()
                                        }
                                    }
                            }else{
                                Toast.makeText(view!!.context, "Gagal Mengganti Password.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                }

            } else {
                Toast.makeText(activity, "Password Baru Tidak sama.", Toast.LENGTH_SHORT)
                    .show()

            }

        } else {
            Toast.makeText(activity, "Isi semua inputan.", Toast.LENGTH_SHORT).show()
        }
    }
}