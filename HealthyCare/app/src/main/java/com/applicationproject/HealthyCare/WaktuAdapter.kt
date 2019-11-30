package com.applicationproject.HealthyCare

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.applicationproject.HealthyCare.model.Booking
import com.applicationproject.HealthyCare.model.Waktu
import com.google.firebase.database.*

class WaktuAdapter(var list : ArrayList<Waktu>, val context: Context, val uid:String) : RecyclerView.Adapter<WaktuAdapter.WaktuHolder>() {
    lateinit var db: DatabaseReference
    var duid = uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaktuAdapter.WaktuHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_waktu, parent, false)
        return WaktuHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WaktuAdapter.WaktuHolder, position: Int) {
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
                        holder.btnJam?.setBackgroundColor(Color.TRANSPARENT)
                        holder.btnJam?.isClickable = false
                    }
                    else{
                        if(holder.btnJam?.currentTextColor != Color.WHITE){
                            holder.btnJam?.setOnClickListener {
                                holder.btnJam?.setTextColor(Color.WHITE)
                                val i: Intent = Intent("jamData")
                                i.putExtra("jam", jam.jam)
                                LocalBroadcastManager.getInstance(context).sendBroadcast(i)
                            }
                        }else{
                            holder.btnJam?.setBackgroundColor(Color.rgb(22, 45, 56))
                            holder.btnJam?.isClickable = false
                        }
                    }
                }
            }
        }
        db.addValueEventListener(listener)
    }

    class WaktuHolder(val view: View): RecyclerView.ViewHolder(view){
        var btnJam : Button? = null

        init{
            btnJam = view.findViewById(R.id.jamBook) as Button
        }
    }
}