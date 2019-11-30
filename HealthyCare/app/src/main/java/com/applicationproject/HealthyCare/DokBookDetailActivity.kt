package com.applicationproject.HealthyCare

import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.applicationproject.HealthyCare.model.Dokter
import com.applicationproject.HealthyCare.model.User
import com.applicationproject.HealthyCare.model.Waktu
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_doc_book_detail.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_search_doc.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.util.*
import android.widget.Toast
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.applicationproject.HealthyCare.model.Booking
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_book_detail.*


class DokBookDetailActivity : AppCompatActivity() {
    lateinit var db: DatabaseReference
    lateinit var dbPesan:DatabaseReference
    lateinit var idDoc: String
    lateinit var time1: String
    lateinit var time2:String
    var pilihJam:String? = null
    var cal: Calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_book_detail)
        idDoc = intent.getStringExtra("duid")
        time1 = intent.getStringExtra("jamStart")
        time2 = intent.getStringExtra("jamEnd")

        LocalBroadcastManager.getInstance(this).registerReceiver(dataReceiver, IntentFilter("jamData"))
        if(pilihJam == null){
            btnPesan.setBackgroundColor(Color.TRANSPARENT)
            btnPesan.isClickable = false
        }
        displayDoc()
        var list: ArrayList<Waktu> = ArrayList<Waktu>()
        var waktuAdapter = WaktuAdapter(list,this,idDoc)
        var layoutManager = GridLayoutManager(this,3)
        recycleWaktu.adapter = waktuAdapter
        recycleWaktu.layoutManager = layoutManager
        recycleWaktu.setHasFixedSize(true)

        val date1 = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(cal.time)
        var format: String  = "MM/dd/yy hh:mm"
        var sdf: SimpleDateFormat = SimpleDateFormat(format)
        var dateObj1: Date = sdf.parse(date1+" "+time1)
        var dateObj2: Date = sdf.parse(date1+" "+time2)

        var dif: Long = dateObj1.time
        while(dif < dateObj2.time){
            var slot: Date = Date(dif)
            list.add(Waktu(slot.toString().substring(11,16)))
            dif += 3600000
        }
    }


    fun displayDoc(){
        db = FirebaseDatabase.getInstance().getReference("dokter/$idDoc")
        var nama_doc: TextView = findViewById(R.id.txtNamaDokDtl)
        var jdwl_doc: TextView = findViewById(R.id.txtJadwalDtl)
        var spesial_doc: TextView = findViewById(R.id.txtSpesialDtl)
        var rs_doc: TextView = findViewById(R.id.txtRSDtl)
        var hari: TextView = findViewById(R.id.showHari)
        var tanggal: TextView = findViewById(R.id.showTanggal)
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

        //hari.text = DateFormat.getDateInstance(DateFormat.DAY_OF_WEEK_FIELD).format(cal.time)
        tanggal.text = DateFormat.getDateInstance(DateFormat.FULL).format(cal.time)
    }

    var dataReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var keluhan: TextView = findViewById(R.id.txtKeluh)
            pilihJam = intent!!.getStringExtra("jam")
            btnPesan.setBackgroundColor(Color.rgb(22, 45, 56))
            btnPesan.isClickable = true
            btnPesan.setOnClickListener {
                if(keluhan.text.equals("")){
                    val i: Intent = Intent(baseContext, BookDetailActivity::class.java)
                    i.putExtra("duiddet", idDoc)
                    i.putExtra("jamPick", pilihJam)
                    i.putExtra("keluh", keluhan.text.toString())
                    startActivity(i)
                }
                else{
                    Toast.makeText(baseContext, "Isikan Keluhan Anda", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}