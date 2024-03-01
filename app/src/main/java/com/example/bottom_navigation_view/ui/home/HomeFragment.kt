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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener{
    private lateinit var mMap: GoogleMap
    private val TAG: String = MainActivity::class.java.getSimpleName()
    private var isStart = false

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
        mMap = googleMap;
        mMap.setMinZoomPreference(16.5f);
        mMap.setMaxZoomPreference(20.0f);
        Log.d("debug", "onMapReady")

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
        val kenrokuenMap_white = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.white))
            .position(kenrokuenLatLng, 2000f, 2000f)
        mMap.addGroundOverlay(kenrokuenMap_white)
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
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.setMyLocationEnabled(true)

        addmarker_kenrokuen()

        // カメラの初期位置
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kenrokuenLatLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f));

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
            LatLng(36.562855, 136.663499),
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
    }

    fun addmarker_kenrokuen() {
        val marker01 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[0])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.renntimonnkyushi))
        )
        val marker02 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[1])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .title(getString(R.string.Yugao_tei))
        )
        val marker03 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[2])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.hakugadankimnotyouzubashi))
        )
        val marker04 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[3])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.hunsui))
        )
        val marker05 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[4])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.koumonbashi))
        )
        val marker06 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[5])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.midoritaki))
        )
        val marker07 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[6])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.kaisekitou))
        )
        val marker08 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[7])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(getString(R.string.hisagoike))
        )
        val marker09 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[8])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.tyouboudai))
        )
        val marker10 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[9])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.kotojitourou))
        )
        val marker11 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[10])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.toraisi))
        )
        val marker12 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[11])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(getString(R.string.karasakinomatsu))
        )
        val marker13 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[12])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.kasumigaike))
        )
        val marker14 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[13])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.oyashirazu))
        )
        val marker15 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[14])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .title(getString(R.string.utihasitei))
        )
        val marker16 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[15])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.sazaeyama))
        )
        val marker17 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[16])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .title(getString(R.string.shiguretei))
        )
        val marker18 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[17])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.gannkoubashi))
        )
        val marker19 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[18])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.shitihukujinyama))
        )
        val marker20 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[19])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title(getString(R.string.bairin))
        )
        val marker21 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[20])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(getString(R.string.kenrokuenkikuzakura))
        )
        val marker22 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[21])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.neagarinomatsu))
        )
        val marker23 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[22])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.meijikinemnohyou))
        )
        val marker24 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[23])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title(getString(R.string.hanamibashi))
        )
        val marker25 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[24])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.sekireijima))
        )
        val marker26 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[25])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.yamazakiyama))
        )
        val marker27 = mMap.addMarker(
            MarkerOptions()
                .position(markerPosition[26])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getString(R.string.tatumiyousui))
        )

        marker01?.tag = "marker_01"
        marker02?.tag = "marker_02"
        marker03?.tag = "marker_03"
        marker04?.tag = "marker_04"
        marker05?.tag = "marker_05"
        marker06?.tag = "marker_06"
        marker07?.tag = "marker_07"
        marker08?.tag = "marker_08"
        marker09?.tag = "marker_09"
        marker10?.tag = "marker_10"
        marker11?.tag = "marker_11"
        marker12?.tag = "marker_12"
        marker13?.tag = "marker_13"
        marker14?.tag = "marker_14"
        marker15?.tag = "marker_15"
        marker16?.tag = "marker_16"
        marker17?.tag = "marker_17"
        marker18?.tag = "marker_18"
        marker19?.tag = "marker_19"
        marker20?.tag = "marker_20"
        marker21?.tag = "marker_21"
        marker22?.tag = "marker_22"
        marker23?.tag = "marker_23"
        marker24?.tag = "marker_24"
        marker25?.tag = "marker_25"
        marker26?.tag = "marker_26"
        marker27?.tag = "marker_27"
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

