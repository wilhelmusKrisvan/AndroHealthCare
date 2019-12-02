package com.applicationproject.HealthyCare.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.applicationproject.HealthyCare.AlergiActivity
import com.applicationproject.HealthyCare.BookDetailActivity
import com.applicationproject.HealthyCare.CheckupActivity
import com.applicationproject.HealthyCare.R
import com.applicationproject.HealthyCare.model.Booking
import com.applicationproject.HealthyCare.model.Dokter
import com.google.firebase.database.*


class RiwayatAdapter(var list : ArrayList<Booking>, val context: Context, var uid: String) : RecyclerView.Adapter<RiwayatAdapter.RiwayatHolder>() {
    lateinit var dbDoc: DatabaseReference
    var jam: String? =null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_riwayat, parent, false)
        return RiwayatHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RiwayatHolder, position: Int) {
        val riw = list.get(position)
        holder.txtNamaKeluhan?.text = riw!!.keluhan
        holder.txtJdwl?.text = riw!!.tanggal + " jam ${riw!!.jam}"
        if (riw!!.status.equals("0")){
            dbDoc = FirebaseDatabase.getInstance().getReference("dokter/${riw!!.duid}")
            dbDoc.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p: DataSnapshot) {
                    val doc = p.getValue(Dokter::class.java)
                    holder.txtNamaDoc?.text = doc!!.nama + "(${doc!!.spesialis})"
                    holder.txtRS?.text = doc!!.rs
                }
            })
            holder.clickRelative?.setOnClickListener {
                val i: Intent = Intent(context, BookDetailActivity::class.java)
                i.putExtra("duiddet", riw!!.duid)
                i.putExtra("jamPick", riw!!.jam)
                i.putExtra("keluh", riw!!.keluhan)
                i.putExtra("tgl", riw!!.tanggal)
                i.putExtra("status", "0")
                context.startActivity(i)
            }
        }else{
            if(riw!!.keluhan.equals("Check Up Dasar")){
                holder.imgRiw?.setImageResource(R.mipmap.lab_foreground)
                holder.clickRelative?.setOnClickListener {
                    val i: Intent = Intent(context, CheckupActivity::class.java)
                    i.putExtra("tgl", riw!!.tanggal)
                    i.putExtra("status", riw!!.status)
                    context.startActivity(i)
                }
            }else{
                holder.clickRelative?.setOnClickListener {
                    holder.imgRiw?.setImageResource(R.mipmap.lab_foreground)
                    val i: Intent = Intent(context, AlergiActivity::class.java)
                    i.putExtra("tgl", riw!!.tanggal)
                    i.putExtra("status", riw!!.status)
                    context.startActivity(i)
                }
            }
        }
    }


    class RiwayatHolder(val view: View): RecyclerView.ViewHolder(view){
        var txtNamaKeluhan : TextView? = null
        var txtNamaDoc : TextView? = null
        var txtRS: TextView? = null
        var txtJdwl : TextView? = null
        var imgRiw : ImageView? = null
        var clickRelative : RelativeLayout? = null

        init{
            txtNamaKeluhan = view.findViewById(R.id.txtKeluh) as TextView
            txtNamaDoc = view.findViewById(R.id.txtNamaDokRiw) as TextView
            txtRS = view.findViewById(R.id.txtRSRiw)  as TextView
            txtJdwl = view.findViewById(R.id.txtJadwalRiw)  as TextView
            imgRiw = view.findViewById(R.id.ImageRiw) as ImageView
            clickRelative = view.findViewById(R.id.relativeListRiw) as RelativeLayout
        }
    }
}