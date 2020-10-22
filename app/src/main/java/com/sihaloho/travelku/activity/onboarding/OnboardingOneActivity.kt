package com.sihaloho.travelku.activity.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sihaloho.travelku.R
import com.sihaloho.travelku.activity.signin.LoginActivity
import com.sihaloho.travelku.utlis.Preferences
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnboardingOneActivity : AppCompatActivity() {

    lateinit var  preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        preferences = Preferences(this)

        if(preferences.getValues("onboarding").equals("1")){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        if(preferences.getValues("onboarding").equals("2")){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btn_lanjut.setOnClickListener{
            finishAffinity()
            startActivity(Intent(this, OnboardingTwoActivity::class.java))
            overridePendingTransition(R.anim.slide_in, R.anim.slide_in_out)
        }

        btn_lewati.setOnClickListener{
            finishAffinity()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}
