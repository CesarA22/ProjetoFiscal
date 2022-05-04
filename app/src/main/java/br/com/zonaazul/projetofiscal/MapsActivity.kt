package br.com.zonaazul.projetofiscal

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null

    private val places = arrayListOf(
        Place("Rota 1 - Começo", LatLng(-22.885751, -47.069082), "Rua Frei Antônio de Pádua, 1286-1436 - Jardim Guanabara\nCampinas - SP, 13073-330"),
        Place("Rota 1 - Final", LatLng(-22.889942, -47.067472), "R. Dr. Albano de Almeida Lima, 164 - Jardim Chapadão\nCampinas - SP, 13073-131"),
        Place("Rota 2 - Começo", LatLng(-22.891105, -47.081092), "Praça Tiro de Guerra, 2655 - Jardim Chapadão\nCampinas - SP, 13070-167"),
        Place("Rota 2 - Final", LatLng(-22.893540, -47.079167), "R. Irmã Maria Inês, 52 - Jardim Chapadão\nCampinas - SP, 13070-030"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        addMarkers(googleMap)

        googleMap.setOnMapLoadedCallback {
            val bounds = LatLngBounds.builder()

            places.forEach{
                bounds.include(it.latLng)
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
        }

        mMap!!.addPolyline(
            PolylineOptions().add(LatLng(-22.891105, -47.081092),
                LatLng(-22.891573, -47.080351),
                LatLng(-22.892037, -47.080826),
                LatLng(-22.893540, -47.079167))
                .width // below line is use to specify the width of poly line.
                    (5f) // below line is use to add color to our poly line.
                .color(Color.RED) // below line is to make our poly line geodesic.
                .geodesic(true)
        )
        // on below line we will be starting the drawing of polyline.
        mMap!!.addPolyline(
            PolylineOptions().add(LatLng(-22.885751, -47.069082),
                LatLng(-22.887850, -47.070469),
                LatLng(-22.889942, -47.067472))
                .width // below line is use to specify the width of poly line.
                    (5f) // below line is use to add color to our poly line.
                .color(Color.RED) // below line is to make our poly line geodesic.
                .geodesic(true)
        )
    }

    private fun addMarkers(googleMap: GoogleMap){
        places.forEach { place ->
            googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .snippet(place.adress)
                    .position(place.latLng)
            )
        }
    }

    data class Place(
        val name: String,
        val latLng: LatLng,
        val adress: String
    )
}