package com.sk1982.purduemap.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoJsonGeometry(
        val type: String,
        val coordinates: List<Any>
)