package com.applicationproject.HealthyCare

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
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
import kotlinx.android.synthetic.main.labcheckup.*
import java.text.DateFormat
import java.util.*

class CheckupActivity : AppCompatActivity() {
    lateinit var db: DatabaseReference
    lateinit var dbPesan: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.labcheckup)


        var status: String = intent.getStringExtra("status")


        val pickTglBtn: Button = findViewById(R.id.btnPickDate2)
        val txttgl : TextView = findViewById(R.id.txtTglCu)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        if(status.equals("1")){
            var tgl: String = intent.getStringExtra("tgl")
            pickTglBtn.visibility=View.INVISIBLE
            pickTglBtn.isClickable=false
            btnCheck.visibility=View.INVISIBLE
            btnCheck.isClickable=false
            txttgl.text = tgl
        }else{
            var jam: String = intent.getStringExtra("jam")
            var nama: String = intent.getStringExtra("nama")
            var id: String = intent.getStringExtra("lid")
            pickTglBtn.setOnClickListener {
                val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    txttgl.setText("" + dayOfMonth + "/" + month + "/" + year)
                }, year, month, day)
                dpd.show()
            }
            btnCheck.setTextColor(Color.WHITE)
            btnCheck.setOnClickListener{
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
                                Toast.makeText(baseContext, "Booking Tabrakan Dengan Jadwal Anda", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                db.addValueEventListener(listener)
                var buid = UUID.randomUUID().toString()
                var book = Booking(buid, txttgl.text.toString(), jam,id, nama, FirebaseAuth.getInstance().currentUser?.uid.toString(),"1")
                dbPesan = FirebaseDatabase.getInstance().getReference("booking")
                dbPesan.child(buid).setValue(book)
                    .addOnSuccessListener {
                        val i: Intent = Intent(baseContext, HomeActivity::class.java)
                        Toast.makeText(baseContext, "Pesanan Anda Telah dibukukan", Toast.LENGTH_LONG).show()
                        startActivity(i)
                        finish()
                    }
            }
        }
    }
}
