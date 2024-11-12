package com.example.kenroku_app.model.services.google_map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class GoogleMapPolyline(private val mMap:GoogleMap):GoogleMap.OnPolylineClickListener {
    //ルート1
    private val polyline1 = mMap.addPolyline(
        PolylineOptions()
            .clickable(true)
            .add(
                LatLng(36.5634, 136.6627),
                LatLng(36.5634, 136.6628),
                LatLng(36.5633, 136.6630),
                LatLng(36.562956, 136.663448),
                LatLng(36.562850, 136.663228),
                LatLng(36.562477, 136.663509),
                LatLng(36.562333, 136.663406),
                LatLng(36.5616, 136.6635),
                LatLng(36.56155, 136.6632),
                LatLng(36.5616, 136.6629),
                LatLng(36.5622, 136.6623),
                LatLng(36.5624, 136.66215),
                LatLng(36.5626, 136.66223),
                LatLng(36.5629, 136.66205),
                LatLng(36.5634, 136.6627)
            )
    )
    init{
        polyline1.tag = "A"
        //polyline1.remove()
    }
    fun setPolyline(){
        stylePolyline(polyline1)
        mMap.setOnPolylineClickListener(this)
    }
    // [START maps_poly_activity_style_polyline]
    private val COLOR_DARK_GREEN_ARGB = -0xc771c4
    private val COLOR_LIGHT_GREEN_ARGB = -0x7e387c
    private val COLOR_DARK_ORANGE_ARGB = -0xa80e9
    private val COLOR_LIGHT_ORANGE_ARGB = -0x657db
    private val POLYLINE_STROKE_WIDTH_PX = 12

    /**
     * Styles the polyline, based on type.
     * @param polyline The polyline object that needs styling.
     */
    private fun stylePolyline(polyline: Polyline) {
        // Get the data object stored with the polyline.
        val type = polyline.tag?.toString() ?: ""
        when (type) {
            "A" -> {
                polyline.color = COLOR_LIGHT_ORANGE_ARGB
            }
            "B" -> {
                polyline.color = COLOR_LIGHT_GREEN_ARGB
            }
        }
        polyline.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
        polyline.jointType = JointType.ROUND
    }
    // [END maps_poly_activity_style_polyline]

    override fun onPolylineClick(p0: Polyline) {
        TODO("Not yet implemented")
    }
}