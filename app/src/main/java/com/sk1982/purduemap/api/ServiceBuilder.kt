package com.sk1982.purduemap.api

import android.util.Log
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.sk1982.purduemap.App
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ServiceBuilder {
    val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor {
            it.proceed(it.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.152 Safari/537.36")
                .header("Referer", "https://www.purdue.edu/")
                .header("Origin", "https://www.purdue.edu")
                .header("Accept", "application/json")
                .cacheControl(CacheControl.Builder()
                    .maxStale(30, TimeUnit.DAYS)
                    .build()
                )
                .build()
            )
        }
        .addNetworkInterceptor {
            val response = it.proceed(it.request())

            response.newBuilder()
                .header("Cache-Control", CacheControl.Builder()
                    .maxAge(7, TimeUnit.DAYS)
                    .build()
                    .toString()
                )
                .build()
        }
        .cache(Cache(
            File(App.context!!.cacheDir, "responses"),
                32 * 1024 * 1024
        ))
        .build()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(JSONObjectAdapter)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://services1.arcgis.com/mLNdQKiKsj5Z5YMN/arcgis/rest/services/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T = retrofit.create(service)
}