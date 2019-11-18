package com.applicationproject.HealthyCare

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionProvider
import android.view.View
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
            progressLogin.visibility = View.VISIBLE
            if(!txtLogEmail.text.toString().equals("") && !txtLogPassword.text.toString().equals("")){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(txtLogEmail.text.toString(),txtLogPassword.text.toString())
                    .addOnSuccessListener {
                        //tampil langsung ke menu utama...
                        if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                            progressLogin.visibility = View.INVISIBLE
                            Toast.makeText(baseContext, "LOGIN BERHASIL", Toast.LENGTH_LONG).show()
                            val i:Intent = Intent(baseContext, HomeActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                        else{
                            progressLogin.visibility = View.INVISIBLE
                            Toast.makeText(baseContext, "SILAHKAN VERIFIKASI EMAIL ANDA", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener {
                        //gagal
                        progressLogin.visibility = View.INVISIBLE
                        Toast.makeText(baseContext, "LOGIN GAGAL", Toast.LENGTH_LONG).show()
                    }
            }
            else{
                progressLogin.visibility = View.INVISIBLE
                Toast.makeText(baseContext, "EMAIL & PASSWORD KOSONG", Toast.LENGTH_LONG).show()
            }
        }

    }

}
