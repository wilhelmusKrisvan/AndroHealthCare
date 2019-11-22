package com.applicationproject.HealthyCare

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_book_dok.*
import java.io.IOException
import java.util.*

class BookDokActivity : AppCompatActivity() {
    lateinit var lm: LocationManager
    lateinit var ll: LocationListener
//    private lateinit var mMap: GoogleMap
//    private var marker: Marker? = null
    private var Iloc: Location? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_dok)
        var txtLoc: TextView = findViewById(R.id.txtCurLoc)
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
                    var geocoder: Geocoder =Geocoder(baseContext, Locale.getDefault())
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


//        var isGPS_On: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//        if(isGPS_On){
//            ll = object : LocationListener {
//                override fun onLocationChanged(location: Location?) {
//                    var longitude: Double = location!!.longitude
//                    var latitude: Double = location!!.latitude
//                    try{
//                        var geocoder: Geocoder =Geocoder(baseContext, Locale.getDefault())
//                        var addList: MutableList<Address>? = geocoder.getFromLocation(latitude,longitude,1)
//                        txtLoc.text = addList?.get(0)?.getAddressLine(0)
//                    }catch (e:IOException){
//                        e.printStackTrace()
//                    }
//                }
//
//                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//
//                }
//
//                override fun onProviderEnabled(provider: String?) {
//
//                }
//
//                override fun onProviderDisabled(provider: String?) {
//
//                }
//
//            }
//        }
//        else if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
//        }
//        else{
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0F, ll)
//        }

    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0F,ll)
//                txtLoc.text = "Getting Location"
//            }else{
//                txtLoc.text = "No Current Location"
//            }
//        }
//    }

}
