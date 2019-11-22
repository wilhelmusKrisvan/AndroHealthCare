package com.applicationproject.HealthyCare

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class DokterAdapter(val list : ArrayList<Dokter>, val context: Context) : RecyclerView.Adapter<DokterAdapter.DokterHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DokterAdapter.DokterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_card_doc, parent, false)
        return DokterHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DokterAdapter.DokterHolder, position: Int) {
        val doc = list.get(position)
//        holder.clickRelative?.setOnClickListener {
//            var i : Intent = Intent(context, class yg dituju)
//            context.startActivity(i)
//        }
    }

    class DokterHolder(val view: View): RecyclerView.ViewHolder(view){
        var txtNamaDocv : TextView? = null
        var txtRSv : TextView? = null
        var txtJdwlv : TextView? = null
        var clickRelative : RelativeLayout? = null

        init{
            txtNamaDocv = view.findViewById(R.id.txtNamaDok) as TextView
            txtRSv = view.findViewById(R.id.txtRS)  as TextView
            txtJdwlv = view.findViewById(R.id.txtJadwal)  as TextView
            clickRelative = view.findViewById(R.id.relativeList) as RelativeLayout
        }
    }
}