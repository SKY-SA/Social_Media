package com.sky.socialmedia.view

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sky.socialmedia.R
import com.sky.socialmedia.databinding.ActivityMapsBinding
import java.lang.Exception
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object: LocationListener {
            override fun onLocationChanged(p0: Location) {
                val currentLocation = LatLng(p0.latitude,p0.longitude)
                mMap.addMarker(MarkerOptions().position(currentLocation).title("my current location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15f))

                val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

                try {
                    val lastKnownLocation = geocoder.getFromLocation(p0.latitude,p0.longitude,1)
                    if (lastKnownLocation.size > 0){
                        println(lastKnownLocation.get(0).toString())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1F,locationListener)
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER )

            if(lastKnownLocation != null){
                val lastKnownLatLng = LatLng(lastKnownLocation.latitude,lastKnownLocation.longitude)
                mMap.addMarker(MarkerOptions().position(lastKnownLatLng).title("Current Your Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng,15F))
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if(requestCode== 1)
        {
            if (grantResults.size >0 ){
                if(ContextCompat.checkSelfPermission(this ,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1F,locationListener)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}