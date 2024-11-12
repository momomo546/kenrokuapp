package com.example.kenroku_app.model.repositories.data

import com.example.kenroku_app.model.services.google_map.GoogleMapMarker
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
        val yamanakaMarkerPosition: List<LatLng> = listOf(
            LatLng(36.246981, 136.372885), // 山中座・菊の湯（おんな湯）
            LatLng(36.246835, 136.373205), // 総湯 菊の湯（おとこ湯）
            LatLng(36.24100747241926, 136.37160339553094), // こおろぎ橋
            LatLng(36.24547368327724, 136.37563996669536), // あやとりはし
            LatLng(36.250127637823404, 136.37578738203896), // 黒屋橋
            LatLng(36.256304333033746, 136.37159700902436), // 山中うるし座
            LatLng(36.25414698709651, 136.37263513138743), // 桂清水
            LatLng(36.249919984803924, 136.3761843489637), // 芭蕉堂
            LatLng(36.25161162089192, 136.3723512679929), // 白山神社
            LatLng(36.24970729332296, 136.37173102990954), // 国分山 医王寺
            LatLng(36.244937486434175, 136.37583436669524), // 鶴仙渓 川床
            LatLng(36.22958920754874, 136.3666297685451), // 三又大杉
            LatLng(36.246222930311845, 136.37392991087435), // 芭蕉の館
            LatLng(36.2294677452656, 136.3652514532019), // 徳正寺の椿清水
            LatLng(36.23469422958989, 136.365275526252), // 工芸の館（ろくろの里）
            LatLng(36.26615012980106, 136.37994688886923), // 長谷部神社
            LatLng(36.24103989989474, 136.3721610096297)  // 無限庵
        )

        var markerList: Vector<MarkerOptions> = Vector()
        var googleMapMarker: GoogleMapMarker? = null
    }

}