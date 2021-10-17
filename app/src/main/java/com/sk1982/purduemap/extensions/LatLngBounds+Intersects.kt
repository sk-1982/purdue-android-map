package com.sk1982.purduemap.extensions

import com.google.android.libraries.maps.model.LatLngBounds

fun LatLngBounds.intersects(other: LatLngBounds): Boolean {
    val xmin1 = southwest.longitude
    val xmin2 = other.southwest.longitude

    val xmax1 = northeast.longitude
    val xmax2 = other.northeast.longitude

    val ymin1 = southwest.latitude
    val ymin2 = other.southwest.latitude

    val ymax1 = northeast.latitude
    val ymax2 = other.northeast.latitude

    return xmax1 >= xmin2 && xmax2 >= xmin1 && ymax1 >= ymin2 && ymax2 >= ymin1
}