package com.sihaloho.travelku.ui.home.daftar_penumpang

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.sihaloho.travelku.R
import com.sihaloho.travelku.activity.MessageChatActivity
import com.sihaloho.travelku.modul.user
import kotlinx.android.synthetic.main.activity_detail_driver.tv_nama_driver_detail
import kotlinx.android.synthetic.main.activity_detail_penumpang.*

class DetailPenumpangActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_penumpang)

        val data = intent.getParcelableExtra<user>("data")
        tv_nama_driver_detail.text =data.nama_penumpang
        tv_user_level_detail.text =data.user_level
        tv_nama_travel.text =data.nama_penumpang
        tv_alamat.text =data.alamat
        tv_no_telpon.text =data.no_hp
        tv_email.text =data.email

        Glide.with(this)
            .load(data.foto)
            .into(iv_driver_detail)

        btn_hub_driver.setOnClickListener {

            val options = arrayOf<CharSequence>(
                "Chat Penumpang",
                "Telfon Penumpang"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Hubungi Penumpang?")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, pos ->
                if (pos == 0){
                    val intent = Intent(this, MessageChatActivity::class.java)
                    intent.putExtra("visit_id", data.uid)
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
