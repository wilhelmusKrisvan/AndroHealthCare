package com.applicationproject.HealthyCare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)

        txtRegis.setOnClickListener {
            val i:Intent = Intent(baseContext, RegisActivity::class.java)
            startActivity(i)
        }

        btnLogin.setOnClickListener {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(txtEmail.text.toString(),txtPassword.text.toString())
                .addOnSuccessListener {
                    //tampil langsung ke menu utama...
                    Toast.makeText(baseContext, "LOGIN BERHASIL", Toast.LENGTH_LONG).show()
                    val i:Intent = Intent()
                    startActivity(i)
                }
                .addOnFailureListener {
                    //gagal
                    Toast.makeText(baseContext, "LOGIN GAGAL", Toast.LENGTH_LONG).show()
                }
        }

    }

}
