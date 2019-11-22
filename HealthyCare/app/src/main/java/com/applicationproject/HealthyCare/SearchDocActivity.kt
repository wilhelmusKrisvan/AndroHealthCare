package com.applicationproject.HealthyCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_search_doc.*

class SearchDocActivity : AppCompatActivity() {
    var list: ArrayList<Dokter> = ArrayList<Dokter>()
    var dokterAdapter = DokterAdapter(list,this)
    var layoutManager = LinearLayoutManager(this)
    lateinit var spesialis: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_doc)
        recycleDoc.adapter = dokterAdapter
        recycleDoc.layoutManager = layoutManager
        recycleDoc.setHasFixedSize(true)




    }
}
