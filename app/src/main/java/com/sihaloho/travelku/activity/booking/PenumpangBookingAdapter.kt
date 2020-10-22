package com.sihaloho.travelku.activity.booking



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
import com.sihaloho.travelku.modul.BookingPenumpang

class PenumpangBookingAdapter(
    private var data: List<BookingPenumpang>,
    private val listener: (BookingPenumpang) -> Unit
) : RecyclerView.Adapter<PenumpangBookingAdapter.LeagueViewHolder>() {

    lateinit var ContextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.item_penumpang_booking, parent, false)

        return LeagueViewHolder(
            inflatedView
        )
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val tvNama: TextView = view.findViewById(R.id.tv_penumpang_booking)
        private val ivBooking: ImageView = view.findViewById(R.id.iv_penumpang_booking)


        fun bindItem(data: BookingPenumpang, listener: (BookingPenumpang) -> Unit, context: Context, position: Int) {

            tvNama.text = data.nama_penumpang_booking
            Glide.with(context)
                .load(data.foto_penumpang_booking)
                .apply(RequestOptions.circleCropTransform())
                .into(ivBooking)

            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}