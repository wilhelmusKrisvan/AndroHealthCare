package com.applicationproject.HealthyCare.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.applicationproject.HealthyCare.DokterAdapter
import com.applicationproject.HealthyCare.HomeActivity
import com.applicationproject.HealthyCare.R
import com.applicationproject.HealthyCare.RiwayatAdapter
import com.applicationproject.HealthyCare.model.Booking
import com.applicationproject.HealthyCare.model.Dokter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_search_doc.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_profile.*

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        notificationsViewModel =
//            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history, container, false)
//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        notificationsViewModel.text.observe(this, Observer {
//            textView.text = it
//        })
        lateinit var db: DatabaseReference
        var list: ArrayList<Booking> = ArrayList<Booking>()
        var riwayatAdapter = RiwayatAdapter(list, context, FirebaseAuth.getInstance().uid.toString())
        var layoutManager = LinearLayoutManager(context)
        recycleRiw.adapter = riwayatAdapter
        recycleRiw.layoutManager = LinearLayoutManager(context)
        recycleRiw.setHasFixedSize(true)

        db = FirebaseDatabase.getInstance().getReference("riwayat")
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(data: DataSnapshot) {
                val child = data.children
                child.forEach {
                    var riw = it.getValue(Booking::class.java)
                    if(riw!!.uid.toString().equals(FirebaseAuth.getInstance().currentUser!!.uid)){
                        list.add(riw)
                    }
                }
            }
        }
        db.addValueEventListener(listener)
        return root
    }
}