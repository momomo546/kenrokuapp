package com.example.bottom_navigation_view.ui.home

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
import com.example.bottom_navigation_view.MainActivity
import com.example.bottom_navigation_view.R
import com.example.bottom_navigation_view.ui.KenrokuenPolyline
import com.example.bottom_navigation_view.ui.dashboard.BadgeFlag
import com.example.bottom_navigation_view.ui.dashboard.KenrokuenMarker
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
    private val TAG: String = MainActivity::class.java.getSimpleName()
    private var isStart = false
    lateinit var kenrokuenMarker: KenrokuenMarker

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

        /*
        //ルート1
        val polyline1 = mMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(36.5634, 136.6627),
                    LatLng(36.5634, 136.6628),
                    LatLng(36.5633, 136.6630),
                    LatLng(36.5631, 136.6633),
                    LatLng(36.56295, 136.663445),
                    LatLng(36.562835, 136.663221),
                    LatLng(36.5618, 136.6635),
                    LatLng(36.5616, 136.6635),
                    LatLng(36.56155, 136.6632),
                    LatLng(36.5616, 136.6629),
                    LatLng(36.5622, 136.6623),
                    LatLng(36.5624, 136.66215),
                    LatLng(36.5625, 136.6618),
                    LatLng(36.5628, 136.66175),
                    LatLng(36.5634, 136.6627)))
        polyline1.tag = "A"
        //polyline1.remove()
        stylePolyline(polyline1)
        googleMap.setOnPolylineClickListener(this)
         */

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
        mMap.setMyLocationEnabled(true)

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
            getActivity()?.let {
                MapStyleOptions.loadRawResourceStyle(
                    it, R.raw.style_json
                )
            }
        )
        if (!success) {
            Log.e(TAG, "Style parsing failed.")
        }
    }
    fun addKenrokuenMarker() {
        kenrokuenMarker = KenrokuenMarker(requireContext(),mMap)
        kenrokuenMarker.addMarker()
        BadgeFlag.kenrokuenMarker = kenrokuenMarker
    }

    /*
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
    }*/

    /*fun addmarker_kenrokuen() {
        kenrokuenMarker = KenrokuenMarker(requireContext())
        kenrokuenMarker.addMarker(mMap)
        markerList = Vector()
        markerList.add(
            MarkerOptions()
                .position(markerPosition[0])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.renntimonnkyushi))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[1])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .title(getString(R.string.Yugao_tei))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[2])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.hakugadankimnotyouzubashi))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[3])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.hunsui))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[4])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.koumonbashi))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[5])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.midoritaki))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[6])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.kaisekitou))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[7])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(getString(R.string.hisagoike))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[8])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.tyouboudai))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[9])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.kotojitourou))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[10])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.toraisi))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[11])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(getString(R.string.karasakinomatsu))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[12])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.kasumigaike))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[13])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.oyashirazu))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[14])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .title(getString(R.string.utihasitei))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[15])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.sazaeyama))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[16])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .title(getString(R.string.shiguretei))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[17])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.gannkoubashi))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[18])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.shitihukujinyama))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[19])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title(getString(R.string.bairin))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[20])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(getString(R.string.kenrokuenkikuzakura))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[21])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.neagarinomatsu))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[22])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.meijikinemnohyou))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[23])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title(getString(R.string.hanamibashi))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[24])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.sekireijima))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[25])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.yamazakiyama))
        )
        markerList.add(
            MarkerOptions()
                .position(markerPosition[26])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.tatumiyousui))
        )

        for ((index, value) in markerList.withIndex()) {
            if(checkPointFlag[index]) value.icon(BitmapDescriptorFactory.fromResource(R.drawable.check_mark))
            val formattedIndex = String.format("%02d", index + 1)
            mMap.addMarker(value)?.tag = "marker_$formattedIndex"
        }
    }*/

}

