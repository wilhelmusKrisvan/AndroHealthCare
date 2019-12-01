package com.applicationproject.HealthyCare

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.applicationproject.HealthyCare.model.Booking
import com.applicationproject.HealthyCare.model.Dokter
import com.google.firebase.database.*


class RiwayatAdapter(var list : ArrayList<Booking>, val context: Context, var uid: String) : RecyclerView.Adapter<RiwayatAdapter.RiwayatHolder>() {
    lateinit var dbDoc: DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatAdapter.RiwayatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_riwayat, parent, false)
        return RiwayatHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RiwayatAdapter.RiwayatHolder, position: Int) {
        val riw = list.get(position)
        holder.txtNamaKeluhan?.text = riw!!.keluhan
        holder.txtJdwl?.text = riw!!.tanggal + ", ${riw!!.jam}"
        dbDoc = FirebaseDatabase.getInstance().getReference("dokter/${riw!!.duid}")
        dbDoc.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p: DataSnapshot) {
                val doc = p.getValue(Dokter::class.java)
                holder.txtNamaDoc?.text = doc!!.nama + "($doc!!.spesialis)"
                holder.txtRS?.text = doc!!.rs
            }
        })
    }


    class RiwayatHolder(val view: View): RecyclerView.ViewHolder(view){
        var txtNamaKeluhan : TextView? = null
        var txtNamaDoc : TextView? = null
        var txtRS: TextView? = null
        var txtJdwl : TextView? = null
        var clickRelative : RelativeLayout? = null

        init{
            txtNamaKeluhan = view.findViewById(R.id.txtKeluh) as TextView
            txtNamaDoc = view.findViewById(R.id.txtNamaDokRiw) as TextView
            txtRS = view.findViewById(R.id.txtRSRiw)  as TextView
            txtJdwl = view.findViewById(R.id.txtJadwalRiw)  as TextView
            clickRelative = view.findViewById(R.id.relativeListRiw) as RelativeLayout
        }
    }
}