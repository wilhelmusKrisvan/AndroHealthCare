package com.applicationproject.HealthyCare

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.applicationproject.HealthyCare.model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.labalergi.*
import java.util.*

class AlergiActivity : AppCompatActivity() {
    lateinit var db: DatabaseReference
    lateinit var dbPesan: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.labalergi)

        val pickTglBtn: Button = findViewById(R.id.btnPickDate)
        val txttgl : TextView = findViewById(R.id.txtTglAle)

        var jam: String = intent.getStringExtra("jam")
        var nama: String = intent.getStringExtra("nama")
        var id: String = intent.getStringExtra("lid")

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        pickTglBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                txttgl.setText("" + dayOfMonth + "/" + month + "/" + year)
            }, year, month, day)
            dpd.show()
        }

        btnAlergi.setOnClickListener {
            progressBarPesan.visibility = View.VISIBLE
            db = FirebaseDatabase.getInstance().getReference("booking")
            val listener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(data: DataSnapshot) {
                    val child = data.children
                    child.forEach{
                        val book = data.getValue(Booking::class.java)
                        if(book!!.jam.toString().equals(jam) && book!!.uid.toString().equals(
                                FirebaseAuth.getInstance().currentUser!!.uid)){
                            progressBarPesan.visibility = View.INVISIBLE
                            Toast.makeText(baseContext, "Booking Tabrakan Dengan Jadwal Anda", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            db.addValueEventListener(listener)
            var buid = UUID.randomUUID().toString()
            var book = Booking(buid, txttgl.text.toString(), jam,id, nama, FirebaseAuth.getInstance().currentUser?.uid.toString())
            dbPesan = FirebaseDatabase.getInstance().getReference("booking")
            dbPesan.child(buid).setValue(book)
                .addOnSuccessListener {
                    progressBarPesan.visibility = View.INVISIBLE
                    val i: Intent = Intent(baseContext, HomeActivity::class.java)
                    Toast.makeText(baseContext, "Pesanan Anda Telah dibukukan", Toast.LENGTH_LONG).show()
                    startActivity(i)
                    finish()
                }
        }
    }
}
