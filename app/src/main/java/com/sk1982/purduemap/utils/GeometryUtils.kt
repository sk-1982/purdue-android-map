package com.sk1982.purduemap.utils

import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.LatLngBounds

object GeometryUtils {
    init {
        System.loadLibrary("native-lib")
    }

    private fun flattenGeometry(geometry: List<Any>, coordinates: MutableList<List<Double>>) {
        if (geometry.size == 2 && geometry[0] is Double) {
            coordinates.add(geometry as List<Double>)
        } else {
            geometry.forEach { flattenGeometry(it as List<Any>, coordinates) }
        }
    }

    fun flattenGeometry(geometry: List<Any>): List<List<Double>> {
        val coordinates = mutableListOf<List<Double>>()

        flattenGeometry(geometry, coordinates)

        return coordinates
    }

    external fun polylabel(vertices: Array<DoubleArray>): DoubleArray

    fun centroid(vertices: List<List<Double>>): LatLng {
        val bounds = LatLngBounds.builder()
            .apply {
                vertices.forEach {
                    include(LatLng(it[1], it[0]))
                }
            }
            .build()

        return bounds.center
    }
}