package com.applicationproject.HealthyCare.ui.home

import android.content.Intent
import android.graphics.Paint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.text.Layout
import android.text.style.AlignmentSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.applicationproject.HealthyCare.BookDokActivity
import com.applicationproject.HealthyCare.HomeActivity
import com.applicationproject.HealthyCare.R
import com.google.android.gms.location.*
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_bottmenubar.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : Fragment() {

//    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val initSet: TextView = root.findViewById(R.id.txtTgl)
        initSet.textSize = 30F
        initSet.gravity = Gravity.CENTER
        initSet.text = "ANDA BELUM BOOKING"

        var txtLoc: TextView = root.findViewById(R.id.txtCurLoc)
        var locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val locationCallback: LocationCallback = object: LocationCallback(){
            override fun onLocationResult(loc: LocationResult?) {
                loc?: return
                for (location in loc.locations){
                    var geocoder: Geocoder = Geocoder(activity, Locale.getDefault())
                    var addList: MutableList<Address>? = geocoder.getFromLocation(location.latitude,location.longitude,1)
                    txtLoc.text = addList?.get(0)?.getAddressLine(0)
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

        val btnDoc: ImageButton = root.findViewById(R.id.btnDokter)
        btnDoc.setOnClickListener {
            val i: Intent = Intent(activity, BookDokActivity::class.java)
            startActivity(i)
        }


        return root
    }
}