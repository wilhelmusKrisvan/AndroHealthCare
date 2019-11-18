package com.applicationproject.HealthyCare

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.view.Window
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        val handler:Handler = Handler()
        handler.postDelayed(Runnable {
//            startActivity(Intent(applicationContext, AfterSplash::class.java))
//            finish()
            if(FirebaseAuth.getInstance().currentUser != null){
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }else{
                startActivity(Intent(applicationContext, AfterSplash::class.java))
                finish()
            }
        }, 2000)
    }

}
