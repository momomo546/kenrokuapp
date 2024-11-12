package com.example.kenroku_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kenroku_app.R
import com.example.kenroku_app.model.services.google_map.GoogleMapMarker
import com.example.kenroku_app.model.services.google_map.GoogleMapPolyline
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class HomeViewModel : ViewModel() {

    private lateinit var mMap: GoogleMap
    private val TAG: String = "HomeViewModel"
    private lateinit var googleMapMarker: GoogleMapMarker
    // 移動の制限範囲
    val latLngBounds = LatLngBounds(
        LatLng(36.5600, 136.6594),  // SW bounds
        LatLng(36.5648, 136.6653) // NE bounds
    )
    val yamanakaLatLngBounds = LatLngBounds(
        LatLng(36.227973, 136.358795),  // SW bounds
        LatLng(36.253658, 136.377376) // NE bounds
    )
    val yamanaka = true
    val MINZOOM = 16.5f
    val MINZOOM2 = 15.5f
    val MAXZOOM = 20.0f

    fun initializeMap(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(MINZOOM2)
        mMap.setMaxZoomPreference(MAXZOOM)
        Log.d(TAG, "onMapReady")

        setMapBounds(yamanakaLatLngBounds)
        addOverlaysAndCamera()
        addKenrokuenMarker()
        mMap.uiSettings.isZoomControlsEnabled = true

        val googleMapPolyline = GoogleMapPolyline(mMap)
        googleMapPolyline.setPolyline()
    }

    private fun setMapBounds(latLngBounds: LatLngBounds) {
        // 移動の制限の適用
        mMap.setLatLngBoundsForCameraTarget(latLngBounds)
    }

    private fun addOverlaysAndCamera() {
        if(yamanaka){
            yamanakaConfig()
            return
        }
        //オーバーレイの地図を設定
        val kenrokuenLatLng = LatLng(36.5625, 136.66227)
        //地図
        val kenrokuenMap = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.map_kenrokuen))
            .position(kenrokuenLatLng, 528f, 528f)
        //背景
        val kenrokuenMapWhite = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.white))
            .position(kenrokuenLatLng, 2000f, 2000f)
        mMap.addGroundOverlay(kenrokuenMapWhite)
        mMap.addGroundOverlay(kenrokuenMap)

        setInitialCameraPosition(kenrokuenLatLng)
    }

    private fun yamanakaConfig() {
        val yamanakaLatLng = LatLng(36.246801, 136.373319)
        setInitialCameraPosition(yamanakaLatLng)
    }

    private fun setInitialCameraPosition(latLng: LatLng) {
        //カメラとズームの初期位置設定
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f))
    }
    private fun addKenrokuenMarker() {

    }
}