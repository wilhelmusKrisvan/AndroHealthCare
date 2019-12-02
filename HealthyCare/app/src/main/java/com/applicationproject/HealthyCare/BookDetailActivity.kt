package com.applicationproject.HealthyCare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.applicationproject.HealthyCare.model.Booking
import com.applicationproject.HealthyCare.model.Dokter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_book_detail.*
import java.text.DateFormat
import java.util.*

class BookDetailActivity : AppCompatActivity() {
    lateinit var idDoc:String
    lateinit var db: DatabaseReference
    lateinit var dbBookCek: DatabaseReference
    lateinit var dbPesan: DatabaseReference
    var cal: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        idDoc = intent.getStringExtra("duiddet")
        var jam = intent.getStringExtra("jamPick")
        var keluh = intent.getStringExtra("keluh")
        var stat = intent.getStringExtra("status")
        displayDoc()
        txtJamIni.text = jam
        showKeluhan.text = keluh

        if(stat.equals("0")){
            btnKonfirmasi.isClickable =false
            btnKonfirmasi.visibility = View.INVISIBLE
        }
        else{
            btnKonfirmasi.setOnClickListener {
                progressBarPesan.visibility = View.VISIBLE
                db = FirebaseDatabase.getInstance().getReference("booking")
                val listener = object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }
                    override fun onDataChange(data: DataSnapshot) {
                        val child = data.children
                        child.forEach{
                            val book = data.getValue(Booking::class.java)
                            if(book!!.jam.toString().equals(jam) && book!!.uid.toString().equals(FirebaseAuth.getInstance().currentUser!!.uid)){
                                progressBarPesan.visibility = View.INVISIBLE
                                Toast.makeText(baseContext, "Booking Tsbrsksn Dengan Jadwal Anda", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                db.addValueEventListener(listener)
                var buid = UUID.randomUUID().toString()
                var pickTanggal = DateFormat.getDateInstance().format(cal.time)
                var book = Booking(buid, pickTanggal, jam,idDoc, keluh, FirebaseAuth.getInstance().currentUser?.uid.toString())
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

    fun displayDoc(){
        db = FirebaseDatabase.getInstance().getReference("dokter/$idDoc")
        var nama_doc: TextView = findViewById(R.id.txtNamaDokDtlIni)
        var jdwl_doc: TextView = findViewById(R.id.txtJadwalDtlIni)
        var spesial_doc: TextView = findViewById(R.id.txtSpesialDtlIni)
        var rs_doc: TextView = findViewById(R.id.txtRSDtlIni)
        var hari: TextView = findViewById(R.id.showHariIni)
        var tanggal: TextView = findViewById(R.id.showTanggalIni)
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(data: DataSnapshot) {
                val doc = data.getValue(Dokter::class.java)
                nama_doc.text = doc!!.nama
                jdwl_doc.text = doc!!.jamStart + " - " + doc!!.jamEnd
                spesial_doc.text = doc!!.spesialis
                rs_doc.text = doc!!.rs
            }
        }
        db.addValueEventListener(listener)
        tanggal.text = DateFormat.getDateInstance(DateFormat.FULL).format(cal.time)
    }
}