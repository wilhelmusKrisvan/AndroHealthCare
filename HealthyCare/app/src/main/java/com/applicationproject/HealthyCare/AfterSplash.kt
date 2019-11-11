package com.applicationproject.HealthyCare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_after_splash.*

class AfterSplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_splash)

        btnLogin.setOnClickListener {
            val i: Intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(i)
        }
        btnRegis.setOnClickListener {
            val i: Intent = Intent(baseContext, RegisActivity::class.java)
            startActivity(i)
        }
    }
}
