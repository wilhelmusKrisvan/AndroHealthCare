package com.applicationproject.HealthyCare.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.applicationproject.HealthyCare.R
import com.applicationproject.HealthyCare.model.Booking
import com.applicationproject.HealthyCare.model.Waktu
import com.google.firebase.database.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class WaktuAdapter(var list : ArrayList<Waktu>, val context: Context, val uid:String) : RecyclerView.Adapter<WaktuAdapter.WaktuHolder>() {
    lateinit var db: DatabaseReference
    var duid = uid
    var date: Date = Date()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaktuHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_waktu, parent, false)
        return WaktuHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WaktuHolder, position: Int) {
        val jam = list.get(position)
        holder.btnJam?.text = jam.jam
        db = FirebaseDatabase.getInstance().getReference("booking")
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(data: DataSnapshot) {
                val child = data.children
                child.forEach{
                    var wkt = it.getValue(Booking::class.java)
                    if (wkt!!.jam.equals(jam.jam) && wkt.duid.equals(duid)){
                        holder.btnJam?.setBackgroundColor(Color.rgb(189, 189, 189))
                        holder.btnJam?.isClickable = false
                    }
//                    else{
//                        if(holder.btnJam?.currentTextColor != Color.WHITE){

//                        }else{
//                            holder.btnJam?.setBackgroundColor(Color.rgb(22, 45, 56))
//                            holder.btnJam?.isClickable = false
//                        }
//                    }
                }
            }
        }
        db.addValueEventListener(listener)
        if(jam.jam<DateFormat.getTimeInstance(DateFormat.SHORT).format(date)){
            holder.btnJam?.setBackgroundColor(Color.rgb(189, 189, 189))
            holder.btnJam?.isClickable = false
        }
        else{
            holder.btnJam?.setOnClickListener {
                holder.btnJam?.setTextColor(Color.WHITE)
                val i: Intent = Intent("jamData")
                i.putExtra("jam", jam.jam)
                LocalBroadcastManager.getInstance(context).sendBroadcast(i)
            }
        }
    }

    class WaktuHolder(val view: View): RecyclerView.ViewHolder(view){
        var btnJam : Button? = null

        init{
            btnJam = view.findViewById(R.id.jamBook) as Button
        }
    }
}