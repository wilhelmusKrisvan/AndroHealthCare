package com.applicationproject.HealthyCare

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.applicationproject.HealthyCare.model.Dokter

class DokterAdapter(var list : ArrayList<Dokter>, val context: Context) : RecyclerView.Adapter<DokterAdapter.DokterHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DokterAdapter.DokterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_card_doc, parent, false)
        return DokterHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DokterAdapter.DokterHolder, position: Int) {
        val doc = list.get(position)
        holder.txtNamaDocv?.text = doc.nama
        holder.txtJdwlv?.text = doc.jamStart + " - " + doc.jamEnd
        holder.txtRSv?.text = doc.rs
        holder.clickRelative?.setOnClickListener {
            var i : Intent = Intent(context, DokBookDetailActivity::class.java)
            i.putExtra("duid", doc.id.toString())
            i.putExtra("jamStart", doc.jamStart.toString())
            i.putExtra("jamEnd", doc.jamEnd.toString())
            context.startActivity(i)
        }
    }

    fun filterList(filteredList: ArrayList<Dokter>){
        list = filteredList
        notifyDataSetChanged()
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