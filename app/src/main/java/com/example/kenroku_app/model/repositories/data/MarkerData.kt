package com.example.kenroku_app.model.repositories.data

import com.example.kenroku_app.model.services.google_map.KenrokuenMarker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Vector

class MarkerData {
    companion object {
        val markerPosition: List<LatLng> = listOf(
            LatLng(36.563055, 136.661066),
            LatLng(36.5630, 136.6612),
            LatLng(36.5628, 136.6612),
            LatLng(36.5633, 136.6618),
            LatLng(36.562967, 136.661734),
            LatLng(36.562773, 136.661128),
            LatLng(36.562699, 136.661026),
            LatLng(36.562209, 136.660697),
            LatLng(36.5633, 136.6630),
            LatLng(36.5632, 136.6627),
            LatLng(36.5632, 136.6626),
            LatLng(36.5628, 136.6630),
            LatLng(36.562526, 136.662802),
            LatLng(36.5627, 136.6622),
            LatLng(36.5625, 136.6623),
            LatLng(36.5626, 136.6621),
            LatLng(36.562029, 136.661318),
            LatLng(36.562700, 136.663407),
            //LatLng(36.562855, 136.663499),
            LatLng(36.5625, 136.6636),
            LatLng(36.561432, 136.662469),
            LatLng(36.5622, 136.6634),
            LatLng(36.561933, 136.663497),
            LatLng(36.562084, 136.663730),
            LatLng(36.5617, 136.6635),
            LatLng(36.561625, 136.663824),
            LatLng(36.5613, 136.6646),
            LatLng(36.5612, 136.6644)
        )
        var markerList: Vector<MarkerOptions> = Vector()
        var kenrokuenMarker: KenrokuenMarker? = null
    }

}