package com.sk1982.purduemap.models

import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.LatLngBounds
import com.sk1982.purduemap.api.ServiceBuilder
import com.sk1982.purduemap.utils.GeometryUtils
import com.squareup.moshi.JsonClass
import org.json.JSONObject

@JsonClass(generateAdapter = true)
data class PropertyTypedGeoJsonFeature<T : Any>(
    val type: String,
    val id: Int,
    val geometry: GeoJsonGeometry,
    val properties: T,

    @Transient
    private val coordinates: List<List<Double>> = GeometryUtils.flattenGeometry(geometry.coordinates),

    @Transient
    val bounds: LatLngBounds = LatLngBounds.Builder().apply {
        coordinates.forEach { include(LatLng(it[1], it[0])) }
    }.build(),
    @Transient
    val centroid: LatLng = if (geometry.type == "Polygon")
        GeometryUtils.polylabel((geometry.coordinates as List<List<List<Double>>>)[0]
            .map { DoubleArray(2).also { arr ->
                arr[0] = it[0]
                arr[1] = it[1]
            }}
            .toTypedArray())
            .let { LatLng(it[1], it[0]) }
        else GeometryUtils.centroid(coordinates),
    @Transient
    val geoJson: JSONObject = JSONObject(mapOf(
        "type" to type,
        "id" to id,
        "geometry" to JSONObject(geometryAdapter.toJson(geometry))
    ))
) {
    companion object {
        private val geometryAdapter = ServiceBuilder.moshi.adapter(GeoJsonGeometry::class.java)
    }
}
