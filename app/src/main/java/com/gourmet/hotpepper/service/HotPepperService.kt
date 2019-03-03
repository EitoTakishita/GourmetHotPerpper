package com.gourmet.hotpepper.service

import com.gourmet.hotpepper.models.Example
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface HotPepperService {

    @GET("hotpepper/gourmet/v1/")
    fun getGourmetDataByFreeWord(
        @Query("key") key: String,
        @Query("keyword") keyword: String,
        @Query("count") count: Int,
        @Query("order") order: Int,
        @Query("format") format: String
    ): Call<Example>

    @GET("hotpepper/gourmet/v1/")
    fun getGourmetDataInTokyo(
        @Query("key") key: String,
        @Query("middle_area") middleAreaCode: String,
        @Query("count") count: Int,
        @Query("order") order: Int,
        @Query("format") format: String
    ): Call<Example>

    @GET("hotpepper/gourmet/v1/")
    fun getGourmetDataByCurrentLocation(
        @Query("key") key: String,
        @Query("keyword") keyword: String,
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("count") count: Int,
        @Query("order") order: Int,
        @Query("format") format: String
    ): Call<Example>
}

fun createService(): HotPepperService {
    val httpLogging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val httpClientBuilder = OkHttpClient.Builder().addInterceptor(httpLogging)

    val hotPepperBaseUrl = "http://webservice.recruit.co.jp/"
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(hotPepperBaseUrl)
        .client(httpClientBuilder.build())
        .build()
    return retrofit.create(HotPepperService::class.java)
}