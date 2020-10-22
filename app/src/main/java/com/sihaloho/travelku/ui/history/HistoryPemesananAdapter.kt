package com.sihaloho.travelku.ui.history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sihaloho.travelku.R
import com.sihaloho.travelku.database.PesanankuHelper
import com.sihaloho.travelku.modul.TravelModulFavDB
import kotlinx.android.synthetic.main.item_history_pemesanan.view.*

class HistoryPemesananAdapter
    : RecyclerView.Adapter<HistoryPemesananAdapter.CardViewViewHolder>() {

    private var onItemClickCallback : OnItemClickCallback? = null
    private lateinit var movFavHelper: PesanankuHelper

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: TravelModulFavDB)
    }

    var listMovie = ArrayList<TravelModulFavDB>()
        set(listMovie) {
            if (listMovie.size > 0) {
                this.listMovie.clear()
            }
            this.listMovie.addAll(listMovie)
            Log.d("CHECK", "listMovie: $listMovie")
            notifyDataSetChanged()
        }

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movFavItem: TravelModulFavDB) {
            with(itemView) {

                Glide.with(this)
                    .load(movFavItem.foto_mobil)
                    .into(iv_mobil)

                tv_history_brangkat.text = movFavItem.keberangkatan
                tv_history_tujuan.text = movFavItem.tujuan
                tv_history__tanggal.text = movFavItem.tanggal


                btn_detail.setOnClickListener {
                    movFavHelper = PesanankuHelper.getInstance(context!!)
                    movFavHelper.open()
                    movFavHelper.deleteById(movFavItem.id.toString())
                    removeItem(adapterPosition)
                }
                btndetail.setOnClickListener{onItemClickCallback?.onItemClicked(movFavItem)}
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_history_pemesanan, viewGroup, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listMovie.size
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }


    fun removeItem(position: Int) {
        this.listMovie.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listMovie.size)
    }
}