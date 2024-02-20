package com.sk1982.purduemap.api

import com.haroldadmin.cnradapter.NetworkResponse
import com.sk1982.purduemap.models.BuildingProperties
import com.sk1982.purduemap.models.PropertyTypedGeoJsonFeatureCollection
import org.json.JSONObject
import retrofit2.http.GET

private const val queryString = "/FeatureServer/0/query?f=geojson&where=1%3D1&returnGeometry=true&outFields=*";

interface APIInterface {
    @GET("BuildingShapesZip4$queryString")
    suspend fun getBuildings(): NetworkResponse<PropertyTypedGeoJsonFeatureCollection<BuildingProperties>, Any>

    @GET("Parking2020$queryString")
    suspend fun getParking(): NetworkResponse<JSONObject, Any>

    @GET("TentSites$queryString")
    suspend fun getSymbols(): NetworkResponse<JSONObject, Any>

    @GET("BldgAccessibleEntry$queryString")
    suspend fun getAccessibleEntry(): NetworkResponse<JSONObject, Any>
}

val API = ServiceBuilder.buildService(APIInterface::class.java)