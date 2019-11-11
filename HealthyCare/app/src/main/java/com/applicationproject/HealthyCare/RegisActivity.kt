package com.applicationproject.HealthyCare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_regis.*

class RegisActivity : AppCompatActivity() {
    lateinit var db:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_regis)

        db = FirebaseDatabase.getInstance().getReference("user")
        txtLogin.setOnClickListener {
            val i: Intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(i)
        }

        btnRegis.setOnClickListener {
            if(!txtEmail.text.toString().equals("") && !txtPass.text.toString().equals("") && !txtKTP.text.toString().equals("") &&
                    !txtUser.text.toString().equals("")){
                isiData()
                Toast.makeText(baseContext, "TERDAFTAR", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(baseContext, "EMAIL ATAU PASSWORD KOSONG", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun isiData(){
        var isiKTP: TextView = findViewById(R.id.txtKTP)
        var isiUser: TextView = findViewById(R.id.txtUser)
        var isiEmail: TextView = findViewById(R.id.txtEmail)
        var isiPass: TextView = findViewById(R.id.txtPass)

        var user = User(isiKTP.text.toString(), isiEmail.text.toString(), isiPass.text.toString(), isiUser.text.toString())
        db.child(isiKTP.text.toString()).setValue(user)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(isiEmail.text.toString(), isiPass.text.toString())


    }
}
