package com.sihaloho.travelku.ui.home.daftar_travel


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sihaloho.travelku.activity.booking.PenumpangBookingAdapter
import com.sihaloho.travelku.R
import com.sihaloho.travelku.activity.MessageChatActivity
import com.sihaloho.travelku.activity.booking.BookingSuksesActivity
import com.sihaloho.travelku.activity.booking.DetailPenumpangBookingctivity
import com.sihaloho.travelku.activity.booking.SuksesActivity
import com.sihaloho.travelku.database.DatabaseContract
import com.sihaloho.travelku.database.PesanankuHelper
import com.sihaloho.travelku.modul.*
import com.sihaloho.travelku.notificationbooking.*
import kotlinx.android.synthetic.main.activity_detail_travel.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailTravelActivity : AppCompatActivity() {



    lateinit var data: TravelModul
    lateinit var etLokasi : String
    lateinit var mDatabaseTravel: DatabaseReference
    private var dataListBooking = ArrayList<BookingPenumpang>()
    private var detailTravel: TravelModul? = null
    private var movFavItem: TravelModulFavDB? = null
    private var position: Int = 0
    private lateinit var movFavHelper: PesanankuHelper
    var notify = false
    var apiService: APIService? = null

    companion object {
        const val RESULT_ADD = 111
        const val EXTRA_MOVIE_FAV = "extra_movie_fav"
        const val EXTRA_POSITION_MOVIE = "extra_position_movie"
        const val EXTRA_MOVIE = "extra_movie"


        lateinit var uid_booking : String
        lateinit var nama_driver : String
        lateinit var tanggal : String
        lateinit var jam : String
        lateinit var berangkat : String
        lateinit var tujuan : String
        lateinit var uid_driver : String
        lateinit var uid_travel : String



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_travel)


        apiService = Client.Client.getClient("https://fcm.googleapis.com/")!!.create(APIService::class.java)


        //cekuser
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refUsers = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUser!!.uid)
        refUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    btn_pesan.visibility = View.GONE
                    btn_hub_driver.visibility = View.GONE




                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        btn_hub_driver.setOnClickListener {

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
                    intent.putExtra("visit_id", data.uid_driver)
                    this.startActivity(intent)
                }

                else if(which == 1){
                    val nomer = data.no_hp_driver
                    val telpon = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$nomer"))
                    startActivity(telpon)
                }
            })
            builder.show()

        }

        movFavHelper = PesanankuHelper.getInstance(applicationContext)

        detailTravel = intent?.getParcelableExtra(EXTRA_MOVIE)
        movFavItem = intent?.getParcelableExtra(EXTRA_MOVIE_FAV)


        //Get data travel
        data = intent.getParcelableExtra<TravelModul>("data")
        show(data!!)





        rv_penumpang_booking.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        getDataPenumpangBooking()



        btn_pesan.setOnClickListener {


            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Sebaiknya anda menghubungi Driver terlebih dahulu, \nIngin Menghubungi Driver?")
                .setPositiveButton(
                    "Hubungi Driver",
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                        Toast.makeText(
                            this,
                            "Silahakan Klik Tombol Hubungi Driver",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                .setNegativeButton(
                    "Pesan Sekarang",
                    DialogInterface.OnClickListener { dialog, which ->

                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Lokasi Penjemputan")
                        val view = layoutInflater.inflate(R.layout.dialog_lokasi_penjemputan, null)
                        val username = view.findViewById<EditText>(R.id.et_lokasi_penjemputan)
                        builder.setView(view)
                        builder.setPositiveButton("Pesan Sekarang", DialogInterface.OnClickListener { dialog, which ->
                            notify = true
                            lokasiPenjemputan(username)
                        })
                        builder.setNegativeButton(
                            "Keluar",
                            DialogInterface.OnClickListener { dialog, which -> })
                        builder.show()

                    }).show()

        }



    }



    private fun lokasiPenjemputan(username: EditText) {
        if (username.text.toString().isEmpty()) {
            return
        }else{

            etLokasi = username.text.toString()
            getdataPenumpang(etLokasi)
            val intent = Intent(this,
                SuksesActivity::class.java)
            startActivity(intent)

        }
    }

    private fun show(data: TravelModul) {


        val statusTravel = data.status_travel.toString()
        if ( statusTravel == "Sudah Berangkat"){
            btn_pesan.visibility = View.GONE
        }
        val statusPenumpang = data.status_penumpang.toString()
        if ( statusPenumpang == "Sudah Penuh"){
            btn_pesan.visibility = View.GONE
        }

        Glide.with(this)
            .load(data.foto_mobil)
            .into(imageView9)


        tv_detail_berangkat.text = data.keberangkatan
        tv_detail_tujuan.text = data.tujuan
        tv_detail_tgl.text = data.tanggal
        tv_detail_jam.text = data.jam
        tv_detail_harga.text = "Rp"+data.Harga
        tv_detail_bpkb.text = data.no_polisi
        tv_detail_merek_mobil.text = data.merek_mobil
        tv_detail_namadriver.text = data.user_driver
        tv_detail_statuspenumpang.text = data.status_penumpang
        tv_detail_status_travel.text = data.status_travel
    }


    private fun getdataPenumpang(et : String) {

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("user/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val currentUser = p0.getValue(user::class.java)

                val ass = et
                uid_booking = UUID.randomUUID().toString()
                uid_travel = data.uid_travel.toString()
                nama_driver = data.user_driver.toString()
                uid_driver = data.uid_driver.toString()
                tanggal = data.tanggal.toString()
                jam = data.jam.toString()
                berangkat = data.keberangkatan.toString()
                tujuan = data.tujuan.toString()
                val dataPenumpangBooking =
                    FirebaseDatabase.getInstance().getReference("DataBooking/$uid_booking")

                val uid_penumpang_booking = FirebaseAuth.getInstance().uid.toString()
                val foto_penumpangBoking = currentUser?.foto
                val nama_penumpang_booking = currentUser?.nama_penumpang
                val nohp_penumpang_booking = currentUser?.no_hp

                dataPenumpangBooking.setValue(
                    BookingPenumpang(
                        uid_booking,
                        uid_penumpang_booking,
                        uid_travel,
                        nama_penumpang_booking.toString(),
                        foto_penumpangBoking.toString(),
                        nama_driver,
                        tanggal,
                        jam,
                        berangkat,
                        tujuan,
                        nohp_penumpang_booking.toString(),
                        uid_driver,
                        ass


                    )


                ).addOnSuccessListener {
                    Toast.makeText(baseContext,
                        "Berasil Memesan Travel", Toast.LENGTH_SHORT
                    ).show()

                    btn_pesan.visibility = View.INVISIBLE

                    addToFavorite(data)
                    showNotif()

                    sendnotiftodriver(ass)


                }



            }

        })


    }

    private fun sendnotiftodriver(ass: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val uiddriver = data.uid_driver
        val tujuanpenumpang = ass
        val refrence = FirebaseDatabase.getInstance().reference
            .child("user").child(firebaseUser!!.uid)
        refrence.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(user::class.java)
                if (notify){
                    sendNotification(uiddriver!!, user!!.nama_penumpang, tujuanpenumpang)
                }
                notify = false

            }

        })

    }

    private fun sendNotification(uiddriver: String, namaPenumpang: String?, tujuanpenumpang: String) {
        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
        val query = ref.orderByKey().equalTo(uiddriver)

        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children){
                    val uidDRiver = data.uid_driver.toString()
                    val token : Token? = dataSnapshot.getValue(Token::class.java)
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    val data = Data(
                        firebaseUser!!.uid,
                        R.mipmap.ic_launcher,
                        "$namaPenumpang" + "\nLokasi Jemput $tujuanpenumpang",
                        "Penumpang Booking atas nama",
                        uidDRiver

                    )
                    val sender = Sender(data!!, token!!.getToken().toString())
                    apiService!!.sendNotification(sender)
                        .enqueue(object : Callback<MyResponse>{
                            override fun onResponse(
                                call: Call<MyResponse>,
                                response: Response<MyResponse>
                            ) {
                                if (response.code()==200){
                                    if (response.body()!!.success !==1){
                                        Toast.makeText(this@DetailTravelActivity,"Failed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<MyResponse>, t: Throwable) {

                            }
                        })
                }
            }

        })

    }


    private fun showNotif() {

        val NOTIFICATION_CHANNEL_ID = "channel_travelku_notif"
        val context = this.applicationContext
        var notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelName = "TravelKu Notif Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }



        val mIntent = Intent(this, BookingSuksesActivity::class.java)
        val bundle = Bundle()
        mIntent.putExtras(bundle)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.logotravelkupng)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.drawable.logotravelkupng
                )
            )
            .setTicker("notif bwa starting")
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setLights(Color.RED, 3000, 3000)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentTitle("Sukses Booking Travel")
            .setContentText("Mobil travel berhasil kamu dapatkan. " +
                    "\nklik notif ini untuk mendapatkan bukti booking mu")

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115, builder.build())

    }




    //untuk history pemesanan
    private fun addToFavorite(travel: TravelModul) {
        movFavItem?.foto_mobil = travel.foto_mobil
        movFavItem?.foto_STNK = travel.foto_STNK
        movFavItem?.no_polisi = travel.no_polisi
        movFavItem?.keberangkatan = travel.keberangkatan
        movFavItem?.tujuan = travel.tujuan
        movFavItem?.tanggal = travel.tanggal
        movFavItem?.jam = travel.jam
        movFavItem?.Harga = travel.Harga
        movFavItem?.user_driver = travel.user_driver
        movFavItem?.uid_driver = travel.uid_driver
        movFavItem?.no_driver = travel.no_hp_driver

        val intent = Intent()
        intent.putExtra(EXTRA_MOVIE_FAV, movFavItem)
        intent.putExtra(EXTRA_POSITION_MOVIE, position)

        val values = ContentValues()
        values.apply {
            put(DatabaseContract.PesananKu.FOTOMOBIL, travel.foto_mobil)
            put(DatabaseContract.PesananKu.FOTOSTNK, travel.foto_STNK)
            put(DatabaseContract.PesananKu.NOPOLISI, travel.no_polisi)
            put(DatabaseContract.PesananKu.KEBERANGKATAN, travel.keberangkatan)
            put(DatabaseContract.PesananKu.TUJUAN, travel.tujuan)
            put(DatabaseContract.PesananKu.TANGGAL, travel.tanggal)
            put(DatabaseContract.PesananKu.JAM, travel.jam)
            put(DatabaseContract.PesananKu.HARGA, travel.Harga)
            put(DatabaseContract.PesananKu.MEREKMOBIL, travel.merek_mobil)
            put(DatabaseContract.PesananKu.USERDRIVER, travel.user_driver)
            put(DatabaseContract.PesananKu.UIDDRIVER, travel.uid_driver)
            put(DatabaseContract.PesananKu.NODRIVER, travel.no_hp_driver)

        }

        movFavHelper.open()
        val result = movFavHelper.insert(values)
        if (result > 0) {
            setResult(RESULT_ADD, intent)
            Toast.makeText(this, "Menambahkan Ke History Pemesanan", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "Sudah Memesan", Toast.LENGTH_SHORT)
                .show()
        }


    }


    //untuk rv_penumpang_booking
    private fun getDataPenumpangBooking() {
        val z = data.uid_travel.toString()
        mDatabaseTravel = FirebaseDatabase.getInstance().getReference("DataBooking")
        val query: Query = mDatabaseTravel.orderByChild("uid_travel").startAt(z).endAt(z + "\uF8FF")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataListBooking.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val daftarBooking = getdataSnapshot.getValue(BookingPenumpang::class.java!!)
                    dataListBooking.add(daftarBooking!!)
                }
                if (dataListBooking.isNotEmpty()) {

                    rv_penumpang_booking.adapter = PenumpangBookingAdapter(dataListBooking) {

                            val firebaseUser = FirebaseAuth.getInstance().currentUser
                            val refUsers = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUser!!.uid)

                            refUsers.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(p0: DataSnapshot) {

                                    if (p0.exists()){

                                        btn_pesan.visibility = View.GONE
                                        if (firebaseUser.uid == data.uid_driver ){
                                            rv_penumpang_booking.adapter =
                                                PenumpangBookingAdapter(
                                                    dataListBooking
                                                ) {
                                                    val intent = Intent(
                                                        baseContext,
                                                        DetailPenumpangBookingctivity::class.java
                                                    ).putExtra("data", it)
                                                    startActivity(intent)
                                                }
                                        }else{
                                            return
                                        }




                                    }


                                }

                                override fun onCancelled(p0: DatabaseError) {

                                }

                            })

                        }



                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })

    }



}

