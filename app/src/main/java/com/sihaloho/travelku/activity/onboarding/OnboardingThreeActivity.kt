package com.sihaloho.travelku.activity.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sihaloho.travelku.R
import com.sihaloho.travelku.activity.signin.LoginActivity
import kotlinx.android.synthetic.main.activity_onboarding_three.*

class OnboardingThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_three)

        btn_lanjut_login.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}
