package com.applicationproject.HealthyCare

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.applicationproject.HealthyCare.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        displayProfile()

        imgEdtProfil.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,0)
        }
    }

    var selectPhoto: Uri? = null
    var link: String? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectPhoto = data.data

            val bitmap = selectPhoto
            //imgProfile.setImageURI(bitmap)
            edtprofile_imageView.setImageURI(bitmap)
        }
    }

    fun changeData(){
        
    }

    fun displayProfile(){
        var uid: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = FirebaseDatabase.getInstance().getReference("user/$uid")
        var disUser: TextView = findViewById(R.id.edtUsername)
        var disKTP: TextView = findViewById(R.id.edtKTP)
        var disEmail: TextView = findViewById(R.id.txtEdtEmail)
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(data: DataSnapshot) {
                val user = data.getValue(User::class.java)
                disUser.text = user!!.user
                disEmail.text = user!!.email
                disKTP.text = user!!.ktp
                link = user!!.img
                Picasso.get().load(link).into(edtprofile_imageView)
            }
        }
        db.addValueEventListener(listener)
    }
}
