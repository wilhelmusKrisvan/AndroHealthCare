package com.applicationproject.HealthyCare.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.applicationproject.HealthyCare.BookDokActivity
import com.applicationproject.HealthyCare.BookLabActivity
import com.applicationproject.HealthyCare.R
import com.applicationproject.HealthyCare.model.Booking
import com.applicationproject.HealthyCare.model.Dokter
import com.applicationproject.HealthyCare.model.Lab
import com.google.firebase.database.*
import java.text.DateFormat
import java.util.*

class HomeFragment : Fragment() {

//    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        var list: ArrayList<Booking> = ArrayList<Booking>()
        var listTampil: ArrayList<Booking> = ArrayList<Booking>()
        var db:DatabaseReference
        var dbDoc: DatabaseReference
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val tgl: TextView = root.findViewById(R.id.txtTgl)
        val nobook: TextView = root.findViewById(R.id.txtNoBook)
        val keluh: TextView = root.findViewById(R.id.txtKeluhLook)
        val nama: TextView = root.findViewById(R.id.txtNamaDok)
        val rs: TextView = root.findViewById(R.id.txtRS)
        checkData()
        db = FirebaseDatabase.getInstance().getReference("booking")

        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(data: DataSnapshot) {
                val child = data.children
                child.forEach {
                    val book = it.getValue(Booking::class.java)
                    list.add(book!!)
                }
                if(list.size >0 && list.get(0).status.equals("0")){
                    nobook.text = list.get(0).buid
                    tgl.text = list.get(0).tanggal + " jam ${list.get(0).jam}"
                    keluh.text = list.get(0).keluhan
                    dbDoc = FirebaseDatabase.getInstance().getReference("dokter/${list.get(0).duid}")
                    dbDoc.addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(p: DataSnapshot) {
                            val doc = p.getValue(Dokter::class.java)
                            nama.text= doc!!.nama
                            rs.text = doc!!.rs
                        }
                    })
                }else if(list.size >0 && list.get(0).status.equals("1")){
                    nobook.text = list.get(0).buid
                    tgl.text = list.get(0).tanggal + " jam ${list.get(0).jam}"
                    keluh.text = list.get(0).keluhan
                    dbDoc = FirebaseDatabase.getInstance().getReference("lab/${list.get(0).duid}")
                    dbDoc.addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(p: DataSnapshot) {
                            val lab = p.getValue(Lab::class.java)
                            nama.text= lab!!.nama
                        }
                    })
                }else{
                    val initSet: TextView = root.findViewById(R.id.txtTgl)
                    initSet.textSize = 30F
                    initSet.gravity = Gravity.CENTER
                    initSet.text = "ANDA BELUM BOOKING"
                }
            }

        }
        db.orderByChild("jam").addValueEventListener(listener)

        val btnDoc: ImageButton = root.findViewById(R.id.btnDokter)
        btnDoc.setOnClickListener {
            val i: Intent = Intent(activity, BookDokActivity::class.java)
            startActivity(i)
        }

        val btnLab: ImageButton = root.findViewById(R.id.btnLab)
        btnLab.setOnClickListener {
            val i: Intent = Intent(activity, BookLabActivity::class.java)
            startActivity(i)
        }


        return root
    }

    fun checkData(){
        var date: Date = Date()
        var db:DatabaseReference
        var dbRiwayat:DatabaseReference
        db = FirebaseDatabase.getInstance().getReference("booking")
        dbRiwayat= FirebaseDatabase.getInstance().getReference("riwayat")
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(data: DataSnapshot) {
                val child = data.children
                child.forEach {
                    val book = it.getValue(Booking::class.java)
                    if(book!!.jam <= DateFormat.getTimeInstance(DateFormat.SHORT).format(date) && book!!.status.equals("0")){
                        dbRiwayat.child(book.buid).setValue(book)
                        db.child(book.buid).removeValue()
                    }
                    else if(book!!.jam <= DateFormat.getTimeInstance(DateFormat.SHORT).format(date) && book!!.tanggal<=DateFormat.getDateInstance(DateFormat.SHORT).format(date) && book!!.status.equals("1")){
                        dbRiwayat.child(book.buid).setValue(book)
                        db.child(book.buid).removeValue()
                    }
                }
            }

        }
        db.addValueEventListener(listener)
    }
}