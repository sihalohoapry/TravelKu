package com.sihaloho.travelku.ui.home.daftar_driver

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.sihaloho.travelku.R
import com.sihaloho.travelku.activity.MessageChatActivity
import com.sihaloho.travelku.modul.UserDriver
import kotlinx.android.synthetic.main.activity_detail_driver.*

class DetailDriverActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_driver)

        val data = intent.getParcelableExtra<UserDriver>("data")
        tv_nama_driver_detail.text =data.nama_penumpang
        tv_user_level_detail.text =data.user_level_driver
        tv_alamat.text = data.alamat
        tv_email.text = data.email
        tv_no_telpon.text = data.no_hp
        tv_nama_travel.text = data.nama_travel

        Glide.with(this)
            .load(data.foto)
            .into(iv_driver_detail)

        btn_hub_driver.setOnClickListener {

            val options = arrayOf<CharSequence>(
                "Chat Driver",
                "Telfon Driver"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Hubungi dengan?")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, pos ->
                if (pos == 0){
                    val intent = Intent(this, MessageChatActivity::class.java)
                    intent.putExtra("visit_id", data.uid_driver)
                    this.startActivity(intent)
                }
                if (pos==1){
                    val nomer = data.no_hp
                    val telpon = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$nomer"))
                    startActivity(telpon)
                }
            })
            builder.show()


        }

    }
}
