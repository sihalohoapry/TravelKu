package com.sihaloho.travelku.ui.help


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.MasukanUser
import com.sihaloho.travelku.modul.UserDriver
import com.sihaloho.travelku.modul.user
import kotlinx.android.synthetic.main.fragment_laporan.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class LaporanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_laporan, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLoading(false)


        val firebaseuser = FirebaseAuth.getInstance().currentUser
        val dbuser = FirebaseDatabase.getInstance().getReference("user").child(firebaseuser!!.uid)
        dbuser.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user : user = p0.getValue(user::class.java)!!
                    val nama = user.nama_penumpang.toString()
                    tv_nama_dibantu.setText("Hai $nama , silahkan hubungi kami untuk membuat laporan")
                }
            }

        })

        val firebaseuserD = FirebaseAuth.getInstance().currentUser
        val dbuserD = FirebaseDatabase.getInstance().getReference("userDriver").child(firebaseuserD!!.uid)
        dbuserD.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user : UserDriver = p0.getValue(UserDriver::class.java)!!
                    val nama = user.nama_penumpang.toString()
                    tv_nama_dibantu.setText("Hai $nama , silahkan hubungi kami untuk membuat laporan")
                }
            }

        })

        //send email
        textView61.setOnClickListener {
            val send = Intent(Intent.ACTION_SEND)
            send.type = "text/html"
            send.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("trav3lku@gmail.com"))
            startActivity(send)
        }
        iv_email.setOnClickListener {
            val send = Intent(Intent.ACTION_SEND)
            send.type = "text/html"
            send.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("trav3lku@gmail.com"))
            startActivity(send)
        }


        iv_cara_booking.setOnClickListener {
            startActivity(Intent(activity,CaraBookingActivity::class.java))
        }
        textView62.setOnClickListener {
            startActivity(Intent(activity,CaraBookingActivity::class.java))
        }
        tv_nama_dibantu2.setOnClickListener {
            startActivity(Intent(activity,CaraBookingActivity::class.java))
        }



        iv_cara_buat_jadwal.setOnClickListener {
            startActivity(Intent(activity,CaraBuatJadwalActivity::class.java))
        }
        textView60.setOnClickListener {
            startActivity(Intent(activity,CaraBuatJadwalActivity::class.java))
        }
        tv_nama_dibantu3.setOnClickListener {
            startActivity(Intent(activity,CaraBuatJadwalActivity::class.java))
        }



        iv_berimasukan.setOnClickListener {
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("Tuliskan Masukanmu")
            val view = layoutInflater.inflate(R.layout.dialog_masukan, null)
            val username = view.findViewById<EditText>(R.id.et_masukan)
            builder.setView(view)
            builder.setPositiveButton("kirim", DialogInterface.OnClickListener { dialog, which ->
                masukan(username)
            })
            builder.setNegativeButton(
                "Keluar",
                DialogInterface.OnClickListener { dialog, which -> })
            builder.show()

        }
        textView63.setOnClickListener {
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("Tuliskan Masukanmu")
            val view = layoutInflater.inflate(R.layout.dialog_masukan, null)
            val username = view.findViewById<EditText>(R.id.et_masukan)
            builder.setView(view)
            builder.setPositiveButton("kirim", DialogInterface.OnClickListener { dialog, which ->
                masukan(username)
            })
            builder.setNegativeButton(
                "Keluar",
                DialogInterface.OnClickListener { dialog, which -> })
            builder.show()

        }
        tv_nama_dibantu4.setOnClickListener {
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("Tuliskan Masukanmu")
            val view = layoutInflater.inflate(R.layout.dialog_masukan, null)
            val username = view.findViewById<EditText>(R.id.et_masukan)
            builder.setView(view)
            builder.setPositiveButton("kirim", DialogInterface.OnClickListener { dialog, which ->
                masukan(username)
            })
            builder.setNegativeButton(
                "Keluar",
                DialogInterface.OnClickListener { dialog, which -> })
            builder.show()

        }

    }

    private fun masukan(masuk: EditText) {
        showLoading(true)


        val firebaseuser = FirebaseAuth.getInstance().currentUser
        val dbuser = FirebaseDatabase.getInstance().getReference("user").child(firebaseuser!!.uid)
        dbuser.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user : user = p0.getValue(user::class.java)!!
                    val uid_user = user.uid.toString()
                    val email = user.email.toString()
                    val nama_user = user.nama_penumpang.toString()
                    val uid_masukan = UUID.randomUUID().toString()
                    val db = FirebaseDatabase.getInstance().getReference("MasukanUser/$uid_masukan")
                    val Tmasuk = masuk.text.toString()
                    db.setValue(
                        MasukanUser(
                            uid_masukan,
                            uid_user,
                            email,
                            nama_user,
                            Tmasuk

                        ))
                        .addOnSuccessListener {
                            showLoading(false)
                            Toast.makeText(context, "Trimaksih banyak atas Masukan anda,kami akan terus memperbaiki aplikasi ini",
                                Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {

                        }
                }
            }

        })


        val firebaseuserDriver = FirebaseAuth.getInstance().currentUser
        val dbuserDriver = FirebaseDatabase.getInstance().getReference("userDriver").child(firebaseuserDriver!!.uid)
        dbuserDriver.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user : UserDriver = p0.getValue(UserDriver::class.java)!!
                    val uid_user = user.uid_driver.toString()
                    val uid_masukan = UUID.randomUUID().toString()

                    val email = user.email.toString()
                    val nama_user = user.nama_penumpang.toString()
                    val db = FirebaseDatabase.getInstance().getReference("MasukanUser/$uid_masukan")
                    val Tmasuk = masuk.text.toString()
                    db.setValue(
                        MasukanUser(


                            uid_masukan,
                            uid_user,
                            email,
                            nama_user,
                            Tmasuk


                        ))
                        .addOnSuccessListener {
                            showLoading(false)
                            Toast.makeText(context, "Trimaksih banyak atas Masukan anda,kami akan terus memperbaiki aplikasi ini",
                                Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {

                        }
                }
            }

        })



    }
    private fun showLoading(state: Boolean) {
        if (state) {
            prBarmasukan.visibility = View.VISIBLE
        } else {
            prBarmasukan.visibility = View.GONE
        }
    }



}
