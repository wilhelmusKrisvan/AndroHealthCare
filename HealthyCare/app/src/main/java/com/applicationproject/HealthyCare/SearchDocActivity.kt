package com.applicationproject.HealthyCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.applicationproject.HealthyCare.adapter.DokterAdapter
import com.applicationproject.HealthyCare.model.Dokter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_search_doc.*

class SearchDocActivity : AppCompatActivity() {
    var list: ArrayList<Dokter> = ArrayList<Dokter>()
    var dokterAdapter = DokterAdapter(list, this)
    var layoutManager = LinearLayoutManager(this)
    lateinit var spesialis: String
    lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_doc)
        recycleDoc.adapter = dokterAdapter
        recycleDoc.layoutManager = LinearLayoutManager(this)
        recycleDoc.setHasFixedSize(true)

        var spesial: String = intent.getStringExtra("spesialis")

        db = FirebaseDatabase.getInstance().getReference("dokter")
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(data: DataSnapshot) {
                val child = data.children
                child.forEach {
                    val doc = it.getValue(Dokter::class.java)
                    if(doc!!.spesialis.equals(spesial)){
                        list.add(doc!!)
                    }
                }
                dokterAdapter.notifyDataSetChanged()
            }
        }
        db.addValueEventListener(listener)

        txtSearchDoc.addTextChangedListener( object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
        })
    }

    fun filter(search: String){
        var filterList: ArrayList<Dokter> = ArrayList<Dokter>()

        for(doc: Dokter in list){
            if(doc.nama.toLowerCase().contains(search.toLowerCase()) || doc.rs.toLowerCase().contains(search.toLowerCase())){
                filterList.add(doc)
            }
        }
        dokterAdapter.filterList(filterList)
    }
}
