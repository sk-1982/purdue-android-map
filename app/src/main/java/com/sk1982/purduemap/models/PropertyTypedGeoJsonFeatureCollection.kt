package com.sk1982.purduemap.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PropertyTypedGeoJsonFeatureCollection<T : Any>(
        val type: String,
        val features: List<PropertyTypedGeoJsonFeature<T>>
)