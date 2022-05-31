package com.app.weatherapp.retrofit

import com.app.weatherapp.model.WeatherServiceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("data/2.5/forecast")
    fun getWeather(
        @Query("id") query: String?,
        @Query("appid") author: String?
    ): Call<WeatherServiceResponse?>?
}