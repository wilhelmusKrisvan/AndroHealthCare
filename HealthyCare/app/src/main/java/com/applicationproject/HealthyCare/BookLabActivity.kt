package com.applicationproject.HealthyCare

import android.content.Intent
import android.location.*
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.TextView
import com.applicationproject.HealthyCare.model.Lab
import com.google.android.gms.location.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_book_lab.*
import java.util.*

class BookLabActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_lab)
        lateinit var db: DatabaseReference
        lateinit var lm: LocationManager
        lateinit var ll: LocationListener
        var txtLoc: TextView = findViewById(R.id.txtCurLoc2)
        var locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val locationCallback: LocationCallback = object: LocationCallback(){
            override fun onLocationResult(loc: LocationResult?) {
                loc?: return
                for (location in loc.locations){
                    var geocoder: Geocoder = Geocoder(baseContext, Locale.getDefault())
                    var addList: MutableList<Address>? = geocoder.getFromLocation(location.latitude,location.longitude,1)
                    txtLoc.text = addList?.get(0)?.thoroughfare + " " + addList?.get(0)?.featureName + ", "+ addList?.get(0)?.adminArea
//                    Iloc = location
//                    val myloc = LatLng(location.latitude, location.longitude)
//                    if(marker==null){
//                        marker = mMap.addMarker(MarkerOptions().position(myloc).title("My Location"))
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc,16F))
//                    }else{
//                        marker?.position = myloc
//                    }
                }
            }
        }
        locationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )

        imgCu.setOnClickListener {
            var jam: String? =null
            var nama: String? =null
            var id: String? =null
            val i : Intent= Intent(baseContext,CheckupActivity::class.java)
            db = FirebaseDatabase.getInstance().getReference("lab")
            db.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val child = p0.children
                    child.forEach{
                        var lab =  it.getValue(Lab::class.java)
                        if (lab!!.nama.toString().equals("Check Up Dasar")){
                            jam = lab!!.jamStart
                            nama =lab!!.nama
                            id = lab!!.lid
                            i.putExtra("jam", jam)
                            i.putExtra("nama", nama)
                            i.putExtra("lid", id)
                            startActivity(i)
                        }
                    }
                }
            })

        }

        imgAlergi.setOnClickListener {
//            val i : Intent= Intent(baseContext, AlergiActivity::class.java)
//            db = FirebaseDatabase.getInstance().getReference("lab")
//            db.addValueEventListener(object : ValueEventListener{
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//
//                override fun onDataChange(p0: DataSnapshot) {
//                    val child = p0.children
//                    child.forEach{
//                        var lab =  it.getValue(Lab::class.java)
//                        if (lab!!.nama.toString().equals("Uji Saring Alergi")){
//                            i.putExtra("jam", lab!!.jamStart)
//                            i.putExtra("nama", lab!!.nama)
//                            i.putExtra("lid", lab!!.lid)
//                        }
//                    }
//                }
//            })
//            startActivity(i)
        }
    }
}
