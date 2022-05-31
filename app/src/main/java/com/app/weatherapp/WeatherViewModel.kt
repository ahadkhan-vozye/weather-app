package com.app.weatherapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.app.weatherapp.model.WeatherServiceResponse
import com.app.weatherapp.repository.WeatherRepository


class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private var weatherRepository: WeatherRepository? = null
    private var weatherResponseLiveData: LiveData<WeatherServiceResponse>? = null

    fun init() {
        weatherRepository = WeatherRepository()
        weatherResponseLiveData = weatherRepository?.getWeatherResponseLiveData()
    }

    fun searchWeather(cityId: String) {
        weatherRepository?.getWeather(cityId)
    }

    fun getWeatherResponseLiveData(): LiveData<WeatherServiceResponse>? {
        return weatherResponseLiveData
    }
}