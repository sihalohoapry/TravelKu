package com.sihaloho.travelku.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.sihaloho.travelku.R
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

class ViewFullmageActivity : AppCompatActivity() {

    private var image_viewer: ImageView? = null
    private var imageUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_fullmage)

        imageUrl = intent.getStringExtra("url")
        image_viewer = findViewById(R.id.image_viewer)

        Picasso.get().load(imageUrl).into(image_viewer)

    }
}
