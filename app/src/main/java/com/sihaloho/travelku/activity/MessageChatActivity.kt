package com.sihaloho.travelku.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.sihaloho.travelku.R
import com.sihaloho.travelku.modul.Chat
import com.sihaloho.travelku.modul.ChatsAdapter
import com.sihaloho.travelku.modul.UserDriver
import com.sihaloho.travelku.modul.user
import com.sihaloho.travelku.notificationbooking.*
import kotlinx.android.synthetic.main.activity_message_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageChatActivity : AppCompatActivity() {

    var userIdVisit: String = ""
    var firebaseUser: FirebaseUser? = null
    var chatsAdapter: ChatsAdapter? = null
    var mChatList : List<Chat>? = null
    var reference: DatabaseReference? = null
    var notify = false
    lateinit var recycler_view_chats: RecyclerView
    var apiService: APIService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)
        setSupportActionBar(toolbar_message_chat)

        apiService = Client.Client.getClient("Https://fcm.googleapis.com/")!!.create(APIService::class.java)

        intent = intent
        userIdVisit = intent.getStringExtra("visit_id")
        firebaseUser = FirebaseAuth.getInstance().currentUser

        recycler_view_chats = findViewById(R.id.recycle_view_chats)
        recycle_view_chats.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recycle_view_chats.layoutManager = linearLayoutManager



        val refUserP = FirebaseDatabase.getInstance().reference.child("user").child(firebaseUser!!.uid)
        refUserP!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    reference = FirebaseDatabase.getInstance().reference
                        .child("userDriver").child(userIdVisit)
                    reference!!.addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val userD : UserDriver = p0.getValue(UserDriver::class.java)!!
                            username_mchat.text = userD.nama_penumpang
                            Glide.with(baseContext)
                                .load(userD.foto)
                                .into(profile_image_chat)

                            retrieveMessages(firebaseUser!!.uid, userIdVisit, userD.foto)
                        }

                    })


                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        val refUserD = FirebaseDatabase.getInstance().reference.child("userDriver").child(firebaseUser!!.uid)
        refUserD!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    val user: UserDriver = p0.getValue(UserDriver::class.java)!!

                    reference = FirebaseDatabase.getInstance().reference
                        .child("user").child(userIdVisit)
                    reference!!.addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val userD : user = p0.getValue(com.sihaloho.travelku.modul.user::class.java)!!
                            username_mchat.text = userD.nama_penumpang
                            Glide.with(baseContext)
                                .load(userD.foto)
                                .into(profile_image_chat)

                            retrieveMessages(firebaseUser!!.uid, userIdVisit, userD.foto)
                        }

                    })


                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })



        send_message_btn.setOnClickListener {
            notify = true
            val message = text_message.text.toString()
            if (message==""){
                Toast.makeText(this,"silahkan tulis pesanmu", Toast.LENGTH_SHORT).show()
            }
            else{
                sendMessageToUser(firebaseUser!!.uid, userIdVisit, message)
            }
            text_message.setText("")
        }

        attact_image_file_btn.setOnClickListener {
            notify = true
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"), 438)
        }

        seenMessage(userIdVisit)

    }

    private fun retrieveMessages(senderId: String, receiverId: String?, receiverImageUrl: String?) {

        mChatList = ArrayList()


        val reference = FirebaseDatabase.getInstance().reference.child("Chats")

        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mChatList as ArrayList<Chat>).clear()
                for (snapshot in p0.children){
                    val chat: Chat = snapshot.getValue(Chat::class.java)!!

                    if (chat.getReceiver().equals(senderId) && chat.getSender().equals(receiverId)
                        ||chat.getReceiver().equals(receiverId)&& chat.getSender().equals(senderId)){
                            (mChatList as ArrayList<Chat>).add(chat)
                        }
                    }
                    chatsAdapter = ChatsAdapter(this@MessageChatActivity,(mChatList as ArrayList<Chat>),
                        receiverImageUrl!!)
                    recycle_view_chats.adapter = chatsAdapter

            }

        })

    }

    private fun sendMessageToUser(senderId: String, receiverId: String?, message: String) {

        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key

        val messageHashMap = HashMap<String, Any?>()
        messageHashMap["sender"] = senderId
        messageHashMap["message"] = message
        messageHashMap["receiver"] = receiverId
        messageHashMap["isseen"] = false
        messageHashMap["url"] = ""
        messageHashMap["messageId"] = messageKey
        reference.child("Chats")
            .child(messageKey!!)
            .setValue(messageHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val chatsListReference = FirebaseDatabase.getInstance()
                        .reference
                        .child("ChatList")
                        .child(firebaseUser!!.uid)
                        .child(userIdVisit)
                    chatsListReference.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (!p0.exists()){
                                chatsListReference.child("id").setValue(userIdVisit)
                            }
                            chatsListReference.child("id").setValue(firebaseUser!!.uid)
                            val chatsListReceiverRef = FirebaseDatabase.getInstance()
                                .reference
                                .child("ChatList")
                                .child(userIdVisit)
                                .child(firebaseUser!!.uid)
                            chatsListReceiverRef.child("id").setValue(firebaseUser!!.uid)
                        }

                    })


                }
            }

        //notif
        val userreference = FirebaseDatabase.getInstance().reference
            .child("user").child(firebaseUser!!.uid)
        userreference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(user::class.java)

                if (p0.exists()){
                    if (notify){
                        sendnotification(receiverId,user!!.nama_penumpang,message)
                    }
                    notify = false
                }

                else{
                    //notif
                    val userreferenceD = FirebaseDatabase.getInstance().reference
                        .child("userDriver").child(firebaseUser!!.uid)
                    userreferenceD.addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val user = p0.getValue(UserDriver::class.java)
                            if (notify){
                                sendnotification(receiverId,user!!.nama_penumpang,message)
                            }
                            notify = false

                        }

                    })
                }


            }

        })


    }



    private fun sendnotification(receiverId: String?, namaPenumpang: String?, message: String) {
        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
        val query = ref.orderByKey().equalTo(receiverId)

        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children){
                    val token : Token? = dataSnapshot.getValue(Token::class.java)
                    val data =Data(
                        firebaseUser!!.uid,
                        R.mipmap.ic_launcher,
                        "$namaPenumpang: $message",
                        "Pesan Baru",
                        userIdVisit
                    )
                    val sender = Sender(data!!, token!!.getToken().toString())
                    apiService!!.sendNotification(sender)
                        .enqueue(object: Callback<MyResponse>{
                            override fun onResponse(
                                call: Call<MyResponse>,
                                response: Response<MyResponse>
                            ) {
                                if (response.code() == 200){
                                    if (response.body()!!.success !== 1){
                                        Toast.makeText(baseContext,"Failed, nothing happen", Toast.LENGTH_SHORT).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==438 && resultCode== Activity.RESULT_OK && data!=null && data!!.data!=null){
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("Mohon tunggu, sedang mengirim...")
            progressBar.show()


            val fileUri =data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance().reference
            val messageId = ref.push().key
            val filePath = storageReference.child("$messageId.jpg")

            var uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{task->
                if (!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener {task ->
                if (task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val messageHashMap = HashMap<String, Any?>()
                    messageHashMap["sender"] = firebaseUser!!.uid
                    messageHashMap["message"] = "mengirim foto"
                    messageHashMap["receiver"] = userIdVisit
                    messageHashMap["isseen"] = false
                    messageHashMap["url"] = url
                    messageHashMap["messageId"] = messageId

                    ref.child("Chats").child(messageId!!).setValue(messageHashMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                progressBar.dismiss()

                                //notif
                                val reference = FirebaseDatabase.getInstance().reference
                                    .child("user").child(firebaseUser!!.uid)
                                reference.addValueEventListener(object : ValueEventListener{
                                    override fun onCancelled(p0: DatabaseError) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        val user = p0.getValue(user::class.java)
                                        if (notify){
                                            sendnotification(userIdVisit,user!!.nama_penumpang,"mengirim foto")
                                        }
                                        notify = false

                                    }

                                })
                            }
                        }
                }

            }
        }
    }

    var seenListener: ValueEventListener? = null
    private fun seenMessage(userId : String){
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        seenListener = reference!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children )
                {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat!!.getReceiver().equals(firebaseUser!!.uid) && chat!!.getSender().equals(userId)){
                        val hashMap = HashMap<String, Any>()
                        hashMap["isseen"] = true
                        dataSnapshot.ref.updateChildren(hashMap)
                    }
                }
            }

        })
    }

    override fun onPause() {
        super.onPause()
        reference!!.removeEventListener(seenListener!!)
    }

}

//lollipop belom bisa push
