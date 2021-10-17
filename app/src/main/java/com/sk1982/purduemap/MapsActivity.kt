package com.sk1982.purduemap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager

import com.google.maps.android.data.geojson.GeoJsonLayer
import com.haroldadmin.cnradapter.NetworkResponse
import com.markodevcic.peko.Peko
import com.markodevcic.peko.PermissionResult
import com.sk1982.purduemap.api.API
import com.sk1982.purduemap.extensions.intersects
import com.sk1982.purduemap.utils.BitmapUtils.createPureTextIcon
import com.sk1982.purduemap.utils.Coroutines
import kotlinx.coroutines.*

class MapsActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            Coroutines.launch {
                map = it
                setupMap()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun zoomToCurrentLocation(): Boolean {
        val result = Peko.requestPermissionsAsync(this@MapsActivity, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        var hasLocation = false

        if (result is PermissionResult.Granted) {
            map.isMyLocationEnabled = true
            val location =
                if (locationManager.isProviderEnabled(GPS_PROVIDER)) locationManager.getLastKnownLocation(
                    GPS_PROVIDER)
                else locationManager.getLastKnownLocation(NETWORK_PROVIDER)

            if (location != null) {
                hasLocation = true
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude), 16.75f))
            }
        }

        return hasLocation
    }

    private suspend fun setupMap() = coroutineScope {
        if (!zoomToCurrentLocation())
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.42934772466086, -86.92030431841776), 14f))

        val markerManager = MarkerManager(map)
        val groundOverlayManager = GroundOverlayManager(map)
        val polygonManager = PolygonManager(map)
        val polylineManager = PolylineManager(map)

        val buildings = API.getBuildings()

        val layers = (buildings as? NetworkResponse.Success)?.let {
            it.body.features.map {
                val marker = map.addMarker(MarkerOptions()
                    .position(it.centroid)
                    .title(it.properties.name)
                    .icon(createPureTextIcon(it.properties.abbreviation))
                )

                it to GeoJsonLayer(map, it.geoJson, markerManager, polygonManager, polylineManager, groundOverlayManager).apply {
                    defaultPolygonStyle.strokeWidth = 2f
                    defaultPolygonStyle.fillColor = Color.argb(200, 213, 195, 165)
                    defaultPolygonStyle.zIndex = Float.MAX_VALUE
                    setOnFeatureClickListener {
                        marker.showInfoWindow()
                        // defaultPolygonStyle.fillColor = Color.rgb(177, 129, 11)
                    }
                    marker.tag = this
                }
            }
        }

        map.setOnCameraMoveListener {
            val region = map.projection.visibleRegion
            layers?.forEach { (feature, layer) ->
                if (region.latLngBounds.intersects(feature.bounds)) {
                    layer.addLayerToMap()
                } else {
                    layer.removeLayerFromMap()
                }
            }
        }
    }

}