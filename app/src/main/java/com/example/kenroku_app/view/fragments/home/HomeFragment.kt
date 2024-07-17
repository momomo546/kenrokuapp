package com.example.kenroku_app.view.fragments.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.kenroku_app.R
import com.example.kenroku_app.view.activities.MainActivity
import com.example.kenroku_app.model.repositories.data.MarkerData
import com.example.kenroku_app.model.services.google_map.KenrokuenMarker
import com.example.kenroku_app.model.services.google_map.KenrokuenPolyline
import com.example.kenroku_app.model.services.google_map.MarkerDetailFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker

class HomeFragment : Fragment(), OnMapReadyCallback{
    private lateinit var mMap: GoogleMap
    private val TAG: String = MainActivity::class.java.simpleName
    private var isStart = false
    private lateinit var kenrokuenMarker: KenrokuenMarker

    // Fragmentで表示するViewを作成するメソッド
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // 先ほどのレイアウトをここでViewとして作成します
        Log.d("debug", "onCreateView")
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        Log.d("debug", "onViewCreated")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (isStart) return
        isStart = true
        mMap = googleMap
        mMap.setMinZoomPreference(16.5f)
        mMap.setMaxZoomPreference(20.0f)
        Log.d("debug", "onMapReady")

        val kenrokuenPolyline = KenrokuenPolyline(mMap)
        kenrokuenPolyline.setPolyline()

        // 移動の制限範囲
        val adelaideBounds = LatLngBounds(
            LatLng(36.5600, 136.6594),  // SW bounds
            LatLng(36.5648, 136.6653) // NE bounds
        )

        // 移動の制限
        mMap.setLatLngBoundsForCameraTarget(adelaideBounds)

        // オーバーレイとマーカーの追加
        val kenrokuenLatLng = LatLng(36.5625, 136.66227)
        val kenrokuenMap = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.map_kenrokuen))
            .position(kenrokuenLatLng, 528f, 528f)
        val kenrokuenMapWhite = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.white))
            .position(kenrokuenLatLng, 2000f, 2000f)
        mMap.addGroundOverlay(kenrokuenMapWhite)
        mMap.addGroundOverlay(kenrokuenMap)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return
        }
        mMap.isMyLocationEnabled = true

        addKenrokuenMarker()

        // カメラの初期位置
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kenrokuenLatLng))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f))

        // 情報ウィンドウの設定
        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null
            }
            override fun getInfoContents(marker: Marker): View? {
                // マーカーをクリックしたときの内容を設定
                val customInfoWindow = layoutInflater.inflate(R.layout.custom_info_window, null)

                val titleTextView = customInfoWindow.findViewById<TextView>(R.id.titleTextView)
                val detailButton = customInfoWindow.findViewById<Button>(R.id.detailButton)

                titleTextView.text = marker.title

                /*detailButton.setOnClickListener {
                    val markerId = marker.tag as? String

                    val bundle = Bundle()
                    bundle.putString("id", markerId)

                    val receivingFragment = MarkerDetailFragment()
                    receivingFragment.arguments = bundle

                    // NavControllerを取得
                    val navController = view?.findNavController()

                    // 画面遷移
                    navController?.navigate(R.id.action_navigation_home_to_markerDetailFragment, bundle)
                }*/

                mMap.setOnInfoWindowClickListener {
                    val markerId = marker.tag as? String

                    val bundle = Bundle()
                    bundle.putString("id", markerId)

                    val receivingFragment = MarkerDetailFragment()
                    receivingFragment.arguments = bundle

                    // NavControllerを取得
                    val navController = view?.findNavController()

                    // 画面遷移
                    navController?.navigate(R.id.action_navigation_home_to_markerDetailFragment, bundle)
                }

                return customInfoWindow
            }
        })

        val success = mMap.setMapStyle(
            activity?.let {
                MapStyleOptions.loadRawResourceStyle(
                    it, R.raw.style_json
                )
            }
        )
        if (!success) {
            Log.e(TAG, "Style parsing failed.")
        }
    }
    private fun addKenrokuenMarker() {
        kenrokuenMarker = KenrokuenMarker(requireContext(),mMap)
        kenrokuenMarker.addMarker()
        MarkerData.kenrokuenMarker = kenrokuenMarker
    }
}

