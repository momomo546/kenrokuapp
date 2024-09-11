package com.example.kenroku_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kenroku_app.R
import com.example.kenroku_app.model.services.google_map.KenrokuenMarker
import com.example.kenroku_app.model.services.google_map.KenrokuenPolyline
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class HomeViewModel : ViewModel() {

    private lateinit var mMap: GoogleMap
    private val TAG: String = "HomeViewModel"
    private lateinit var kenrokuenMarker: KenrokuenMarker

    fun initializeMap(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(16.5f)
        mMap.setMaxZoomPreference(20.0f)
        Log.d(TAG, "onMapReady")

        setMapBounds()
        addOverlaysAndCamera()
        addKenrokuenMarker()
        mMap.uiSettings.isZoomControlsEnabled = true

        val kenrokuenPolyline = KenrokuenPolyline(mMap)
        kenrokuenPolyline.setPolyline()
    }

    private fun setMapBounds() {
        // 移動の制限範囲
        val adelaideBounds = LatLngBounds(
            LatLng(36.5600, 136.6594),  // SW bounds
            LatLng(36.5648, 136.6653) // NE bounds
        )
        // 移動の制限の適用
        mMap.setLatLngBoundsForCameraTarget(adelaideBounds)
    }

    private fun addOverlaysAndCamera() {
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

        //カメラとズームの初期位置設定
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kenrokuenLatLng))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f))
    }

    private fun addKenrokuenMarker() {

    }
}