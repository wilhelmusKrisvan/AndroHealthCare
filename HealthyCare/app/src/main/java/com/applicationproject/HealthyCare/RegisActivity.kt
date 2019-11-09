package com.applicationproject.HealthyCare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_regis.*

class RegisActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_regis)

        txtLogin.setOnClickListener {
            val i: Intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(i)
        }

        btnRegis.setOnClickListener {
            //fungsi insert data


            //prepare untuk set stlh regis berhasil...
            val i: Intent = Intent()
            startActivity(i)
        }
    }
}
