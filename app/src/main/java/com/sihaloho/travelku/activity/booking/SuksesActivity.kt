package com.sihaloho.travelku.activity.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sihaloho.travelku.R
import com.sihaloho.travelku.activity.pengingat.AlarmActivity
import kotlinx.android.synthetic.main.activity_sukses.*

class SuksesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sukses)

        btn_pengingat.setOnClickListener {
            startActivity(Intent(this, AlarmActivity::class.java))
            finish()
        }
    }
}
