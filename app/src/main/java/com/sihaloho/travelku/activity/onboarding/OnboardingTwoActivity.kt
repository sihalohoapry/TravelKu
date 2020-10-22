package com.sihaloho.travelku.activity.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sihaloho.travelku.R
import kotlinx.android.synthetic.main.activity_onboarding_two.*

class OnboardingTwoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_two)

        btn_lanjut_ketiga.setOnClickListener {
            startActivity(Intent(this, OnboardingThreeActivity::class.java))
            overridePendingTransition(R.anim.slide_in, R.anim.slide_in_out)
            finish()
        }

    }
}
