package com.app.weatherapp

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.app.weatherapp.adapter.CardsAdapter
import com.app.weatherapp.databinding.ActivityMainBinding
import com.app.weatherapp.model.MainList
import com.app.weatherapp.model.WeatherServiceResponse
import com.app.weatherapp.utils.setSafeOnClickListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), OnMenuItemClickListener<PowerMenuItem> {
    lateinit var powerMenu: PowerMenu
    var cityId = "6290252"
    private val viewModel: WeatherViewModel by viewModels()
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (isOnline(this)) {
            binding.switchOnline.isChecked = true
            getDataFromService()
        } else {
            binding.switchOnline.isChecked = false
            getDataFromRaw()
        }

        initListeners()
    }

    private fun initListeners() {
        binding.switchOnline.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                if (isOnline(this)) {
                    getDataFromService()
                } else {
                    binding.switchOnline.isChecked = false
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                }
            } else {
                getDataFromRaw()
            }
        }
        binding.cityTv.setSafeOnClickListener {
            powerMenu = PowerMenu.Builder(this)
                .addItemList(arrayListOf(PowerMenuItem("Serbia"), PowerMenuItem("Belgrade")))
                .setAnimation(MenuAnimation.DROP_DOWN)
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setWidth(binding.cityTv.measuredWidth)
                .setTextColor(ContextCompat.getColor(this, R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(this, R.color.black))
                .setOnMenuItemClickListener(this)
                .build()

            powerMenu.showAsDropDown(binding.cityTv)
        }

        binding.refreshLayout.setOnRefreshListener {
            if (isOnline(this)) {
                getDataFromRaw()
            } else {
                getDataFromService()
            }
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun getDataFromRaw() {
        var jsonString = ""
        var raw: InputStream? = if (cityId == "6290252") {
            resources.openRawResource(R.raw.data)
        } else {
            resources.openRawResource(R.raw.data_belgrade)
        }

        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        raw.use { rawData ->
            val reader: Reader = BufferedReader(InputStreamReader(rawData, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }
        jsonString = writer.toString()
        val objType = object : TypeToken<WeatherServiceResponse>() {
        }.type
        var temp: WeatherServiceResponse = Gson().fromJson(jsonString, objType)
        initData(temp.list)
    }

    private fun getDataFromService() {
        viewModel.init()
        viewModel.getWeatherResponseLiveData()!!
            .observe(
                this
            ) { weatherResponse ->
                if (weatherResponse != null && !weatherResponse.list.isNullOrEmpty()) {
                    initData(weatherResponse.list)
                }
            }

        viewModel.searchWeather(cityId)
    }

    private fun initData(weatherResponse: List<MainList>) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDateTime: String = sdf.format(Date())
        val todayList = ArrayList<MainList>()

        for (i in weatherResponse.indices) {
            if (currentDateTime == weatherResponse[i].dtTxt.substring(0, 10)) {
                todayList.add(weatherResponse[i])
            }
        }

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = CardsAdapter(this@MainActivity, todayList)
            onFlingListener = null
            LinearSnapHelper().attachToRecyclerView(this)
        }

        Log.i("test", "test")
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    override fun onItemClick(position: Int, item: PowerMenuItem?) {
        cityId = if (position == 0) {
            binding.cityTv.text = "Serbia"
            "6290252"
        } else {
            binding.cityTv.text = "Belgrade"
            "5063781"
        }
        if (isOnline(this)) {
            getDataFromService()
        } else {
            getDataFromRaw()
        }
        powerMenu.dismiss()
    }

}