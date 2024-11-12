package com.example.kenroku_app.model.services.google_map

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import com.example.kenroku_app.R
import com.example.kenroku_app.model.repositories.data.AchieveData
import com.example.kenroku_app.model.repositories.data.MarkerData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Vector

class GoogleMapMarker(val context: Context, val mMap: GoogleMap) {
    private val assetManager = context.assets
    //private val inputStream = assetManager.open("markerList.json") //Jsonファイル
    private val inputStream = assetManager.open("yamanakaMarkerList.json")
    private val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    private val str: String = bufferedReader.readText() //データ
    var markerList: Vector<MarkerOptions> = Vector()
    private var markerPosition: Vector<LatLng> = Vector()
    private var addMarkerList: Vector<Marker?> = Vector()

    init{
        val jsonObject = JSONObject(str)
        val jsonArray = jsonObject.getJSONArray("markerList")

        for (i in 0 until jsonArray.length()) {
            val jsonData = jsonArray.getJSONObject(i)
            val latLng = LatLng(jsonData.getDouble("lat"),jsonData.getDouble("lng"))
            val name = jsonData.getString("name")
            //val iconColor = jsonData.getString("icon")
            //val resourceText = context.resources.getIdentifier("$name", "string", context.packageName)
            val resourceText = context.getString(R.string.yamanakaza)
            markerPosition.add(latLng)
            markerList.add(
                MarkerOptions()
                    .position(markerPosition[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    //.title(context.getString(resourceText))
                    .title(resourceText)
            )
        }
        MarkerData.markerList = markerList
    }

    fun addMarker(){
        for ((index, value) in markerList.withIndex()) {
            if (AchieveData.checkPointFlag[index]) value.icon(
                BitmapDescriptorFactory.fromResource(
                    R.drawable.check_mark
                )
            )
            val formattedIndex = String.format("%02d", index + 1)
            val marker = mMap.addMarker(value)
            marker?.tag = "marker_$formattedIndex"
            addMarkerList.add(marker)
        }
    }
    fun removeMarker(index:Int){
        addMarkerList[index]?.remove()
    }
    fun resetMarker(index:Int){
        addMarkerList[index] = mMap.addMarker(MarkerData.markerList[index])
        markerList[index] = MarkerData.markerList[index]
        val mediaPlayer = MediaPlayer.create(context, R.raw.rappa)
        mediaPlayer.start()
        val toast = Toast.makeText(context, markerList[index].title+"を通過しました！", Toast.LENGTH_LONG)
        toast.show()
    }
}