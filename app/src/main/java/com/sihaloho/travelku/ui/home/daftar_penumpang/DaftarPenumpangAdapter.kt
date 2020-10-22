package com.sihaloho.travelku.ui.home.daftar_penumpang

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.UserDriver
import com.sihaloho.travelku.modul.user

class DaftarPenumpangAdapter(
    private var data: List<user>,
    private val listener: (user) -> Unit
) : RecyclerView.Adapter<DaftarPenumpangAdapter.LeagueViewHolder>() {

    lateinit var ContextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.item_daftar_driver, parent, false)

        return LeagueViewHolder(
            inflatedView
        )
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val tvDaftardriver: TextView = view.findViewById(R.id.tv_nama_daftar_driver)
        private val tvNohp: TextView = view.findViewById(R.id.tv_nohp)

        private val tvImage: ImageView = view.findViewById(R.id.iv_profil_daftar_draver)

        fun bindItem(data: user, listener: (user) -> Unit, context: Context, position: Int) {

            tvDaftardriver.text = data.nama_penumpang
            tvNohp.text = data.no_hp

            Glide.with(context)
                .load(data.foto)
                .apply(RequestOptions.circleCropTransform())
                .into(tvImage)

            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}