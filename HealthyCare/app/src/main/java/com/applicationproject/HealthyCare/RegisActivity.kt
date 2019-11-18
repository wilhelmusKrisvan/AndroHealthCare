package com.applicationproject.HealthyCare

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_regis.*
import java.util.*

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
            finish()
        }

        imgProfile.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,0)
        }

        btnRegis.setOnClickListener {
            if(!txtEmail.text.toString().equals("") && !txtPass.text.toString().equals("") && !txtKTP.text.toString().equals("") &&
                    !txtUser.text.toString().equals("")){
                progressRegis.visibility = View.VISIBLE
                uploadImageToFirebaseStorage()
            }
            else{
                progressRegis.visibility = View.VISIBLE
                Toast.makeText(baseContext, "DATA HARUS DIISI !!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    var selectPhoto: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectPhoto = data.data

            val bitmap = selectPhoto
            //imgProfile.setImageURI(bitmap)
            profile_image.setImageURI(bitmap)
        }
    }

    fun isiData(link: String){
        var isiKTP: TextView = findViewById(R.id.txtKTP)
        var isiUser: TextView = findViewById(R.id.txtUser)
        var isiEmail: TextView = findViewById(R.id.txtEmail)
        var isiPass: TextView = findViewById(R.id.txtPass)

        var user = User(isiKTP.text.toString(), isiUser.text.toString(), isiEmail.text.toString(), isiPass.text.toString(), link)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(isiEmail.text.toString(), isiPass.text.toString())
        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()?.addOnSuccessListener {
            var id: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
            db.child(id).setValue(user)
            progressRegis.visibility = View.INVISIBLE
            Toast.makeText(baseContext, "TERDAFTAR, Silahkan Verifikasi Email Anda", Toast.LENGTH_LONG).show()
            val i:Intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    fun uploadImageToFirebaseStorage(){
        if(selectPhoto == null){
            val ref = FirebaseStorage.getInstance().getReference("/image/user.png")
            ref.downloadUrl.addOnSuccessListener {
                isiData(it.toString())
            }
        }
        else{
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/image/$filename")
            ref.putFile(selectPhoto!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        isiData(it.toString())
                    }
                }
        }
    }

}
