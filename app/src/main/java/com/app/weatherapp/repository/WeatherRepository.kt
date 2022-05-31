package com.app.weatherapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.weatherapp.model.WeatherServiceResponse
import com.app.weatherapp.retrofit.WeatherService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherRepository {
    private val WEATHER_BASE_URL = "http://api.openweathermap.org/"
    private val API_KEY = "8f3d064e5cfb90c7a013ab68a28c27e8"

    private var weatherService: WeatherService? = null
    private var weatherResponseLiveData: MutableLiveData<WeatherServiceResponse>? = null

    init {
        weatherResponseLiveData = MutableLiveData<WeatherServiceResponse>()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        weatherService = Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }

    fun getWeather(cityId: String?) {
        weatherService?.getWeather(cityId, API_KEY)
            ?.enqueue(object : Callback<WeatherServiceResponse?> {
                override fun onResponse(
                    call: Call<WeatherServiceResponse?>?,
                    response: Response<WeatherServiceResponse?>
                ) {
                    if (response.body() != null) {
                        weatherResponseLiveData!!.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<WeatherServiceResponse?>?, t: Throwable?) {
                    weatherResponseLiveData!!.postValue(null)
                }
            })
    }

    fun getWeatherResponseLiveData(): LiveData<WeatherServiceResponse>? {
        return weatherResponseLiveData
    }
}