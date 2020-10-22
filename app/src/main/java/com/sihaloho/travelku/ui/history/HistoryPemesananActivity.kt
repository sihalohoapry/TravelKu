package com.sihaloho.travelku.ui.history

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.sihaloho.travelku.R
import com.sihaloho.travelku.activity.MessageChatActivity
import com.sihaloho.travelku.modul.TravelModulFavDB
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_history_pemesanan.*

class HistoryPemesananActivity : AppCompatActivity() {

    private var movFavItem: TravelModulFavDB? = null
    private var uiddriver: String? = null
    private var nodriver: String? = null

    companion object{
        const val EXTRA_MOVIE = "extra_movie"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_pemesanan)

        movFavItem = intent?.getParcelableExtra(EXTRA_MOVIE)

        show(movFavItem)



        btn_pesan.setOnClickListener {
            val options = arrayOf<CharSequence>(
                "Chat Driver",
                "Telfon Driver"
            )

            val builder : AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Hubungi Driver?")

            builder.setItems(options, DialogInterface.OnClickListener{
                    dialog, which ->

                if (which == 0){

                    val intent = Intent(this, MessageChatActivity::class.java)
                    intent.putExtra("visit_id", uiddriver)
                    this.startActivity(intent)
                }

                else if(which == 1){
                    val nomer = nodriver
                    val telpon = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$nomer"))
                    startActivity(telpon)
                }
            })
            builder.show()
        }


    }

    private fun show(movie: TravelModulFavDB?) {
        movie?.let {

            tv_detail_berangkat.text = it.keberangkatan
            tv_detail_tujuan.text = it.tujuan
            tv_detail_tgl.text = it.tanggal
            tv_detail_jam.text = it.jam
            tv_detail_harga.text = it.Harga
            tv_detail_bpkb.text = it.no_polisi
            tv_detail_merek_mobil.text = it.merek_mobil
            tv_detail_namadriver.text = it.user_driver
            Picasso.get().load(it.foto_mobil).into(imageView9)

            uiddriver = it.uid_driver
            nodriver = it.no_driver



        }
    }

}
