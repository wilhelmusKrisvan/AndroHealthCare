package com.applicationproject.HealthyCare.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applicationproject.HealthyCare.R
import com.applicationproject.HealthyCare.adapter.RiwayatAdapter
import com.applicationproject.HealthyCare.model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
        var recycRiw : RecyclerView = root.findViewById(R.id.recycleRiw)
        var list: ArrayList<Booking> = ArrayList<Booking>()
        var riwayatAdapter = RiwayatAdapter(
            list,
            root.context,
            FirebaseAuth.getInstance().uid.toString()
        )
        var layoutManager = LinearLayoutManager(root.context)
        recycRiw.adapter = riwayatAdapter
        recycRiw.layoutManager = LinearLayoutManager(root.context)
        recycRiw.setHasFixedSize(true)

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
                riwayatAdapter.notifyDataSetChanged()
            }
        }
        db.addValueEventListener(listener)
        return root
    }
}