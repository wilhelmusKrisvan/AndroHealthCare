package com.applicationproject.HealthyCare

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.applicationproject.HealthyCare.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_regis.*

class ProfileActivity : AppCompatActivity() {
    lateinit var db: DatabaseReference
    lateinit var dbupdate : DatabaseReference
    var email: String = FirebaseAuth.getInstance().currentUser?.email.toString()
    var uid: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        displayProfile()

        imgEdtProfil.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,0)
        }

        txtSave.setOnClickListener {
            if (!edtUsername.text.toString().equals("") && !edtKTP.text.toString().equals("") && !edtPass.text.toString().equals("") && !edtConfirmPass.text.toString().equals("")){
                if (edtPass.text.toString().equals(edtConfirmPass.text.toString())){
                    uploadEditanToFirebaseStorage()
                    FirebaseAuth.getInstance().currentUser!!.updatePassword(edtPass.text.toString())
                }else{
                    Toast.makeText(baseContext, "NOT MATCH lho!", Toast.LENGTH_LONG).show()
                }
            }else if(!edtUsername.text.toString().equals("") && !edtKTP.text.toString().equals("") && edtPass.text.toString().equals("") && edtConfirmPass.text.toString().equals("")){
                uploadEditanToFirebaseStorage()
            }else{
                Toast.makeText(baseContext, "Ada data yang kosong lho!", Toast.LENGTH_LONG).show()
            }
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

    fun changeData(link: String){
        db = FirebaseDatabase.getInstance().getReference("user/$uid")
        var isiEmail : TextView = findViewById(R.id.txtEdtEmail)
        var isiUname: TextView = findViewById(R.id.edtUsername)
        var isiKTP: TextView = findViewById(R.id.edtKTP)
        var isiPass: TextView = findViewById(R.id.edtPass)
        var isiConfirmPass: TextView = findViewById(R.id.edtConfirmPass)

        //progressRegis.visibility = View.VISIBLE
        var user = User(
            isiKTP.text.toString(),
            isiUname.text.toString(),
            isiEmail.text.toString(),
            isiPass.text.toString(),
            link
        )

        db.setValue(user)
            .addOnSuccessListener {
                //progressRegis.visibility = View.INVISIBLE
                val i:Intent = Intent(baseContext, HomeActivity::class.java)
                startActivity(i)
                finish()
            }
    }

    fun displayProfile(){
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

    fun uploadEditanToFirebaseStorage(){
        if(selectPhoto == null){
            val ref = FirebaseStorage.getInstance().getReference("/image/$email")
            ref.downloadUrl.addOnSuccessListener {
                changeData(it.toString())
            }
        }
        else{
            val filename = FirebaseAuth.getInstance().currentUser?.email.toString()
            val ref = FirebaseStorage.getInstance().getReference("/image/$filename")
            ref.putFile(selectPhoto!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        changeData(it.toString())
                    }
                }
        }
    }
}
