package com.sihaloho.travelku.ui.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.TravelModul
import com.sihaloho.travelku.ui.home.daftar_travel.DetailTravelActivity

class HistoryJadwalAdapter(
    private var data: List<TravelModul>,
    private val listener: (TravelModul) -> Unit
) : RecyclerView.Adapter<HistoryJadwalAdapter.LeagueViewHolder>() {

    lateinit var ContextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.item_history_jadwal, parent, false)

        return LeagueViewHolder(
            inflatedView
        )
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val tvTujuan: TextView = view.findViewById(R.id.tv_history_tujuan)
        private val tvKeberangkatan: TextView = view.findViewById(R.id.tv_history_brangkat)
        private val tvTanggal: TextView = view.findViewById(R.id.tv_history__tanggal)
        private val edit : Button = view.findViewById(R.id.btn_detail)
        private val tvImage: ImageView = view.findViewById(R.id.iv_mobil)

        fun bindItem(data: TravelModul, listener: (TravelModul) -> Unit, context: Context, position: Int) {

            tvTujuan.text = data.tujuan
            tvKeberangkatan.text = data.keberangkatan
            tvTanggal.text = data.tanggal

            Glide.with(context)
                .load(data.foto_mobil)
                .into(tvImage)


            edit.setOnClickListener {
                listener(data)

            }

        }

    }

}