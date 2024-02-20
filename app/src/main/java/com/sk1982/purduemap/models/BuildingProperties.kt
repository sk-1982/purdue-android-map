package com.sk1982.purduemap.models

import com.squareup.moshi.Json

data class BuildingProperties(
    @Json(name = "BLDG_ABBR") val abbreviation: String,
    @Json(name = "BUILDING_N") val name: String
)