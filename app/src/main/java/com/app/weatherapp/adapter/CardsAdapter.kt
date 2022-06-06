package com.app.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherapp.R
import com.app.weatherapp.databinding.HorRowBinding
import com.app.weatherapp.databinding.HorRowChildBinding
import com.app.weatherapp.model.MainList
import com.bumptech.glide.Glide

class CardsAdapter(val context: Context, val cards: ArrayList<MainList>) :
    RecyclerView.Adapter<CardsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.hor_row_child, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardsAdapter.ViewHolder, position: Int) {

        with(holder) {
            binding.tempTv.text = "Temperature: " + cards[position].main.temp.toString()
            binding.minTempTv.text = "Min Temperature: " + cards[position].main.tempMin.toString()
            binding.maxTempTv.text = "Max Temperature: " + cards[position].main.tempMax.toString()
            binding.maxTempTv.text = "Max Temperature: " + cards[position].main.tempMax.toString()
            if (cards[position].weather.isNullOrEmpty()) {
                binding.weatherTv.visibility = GONE
                binding.weatherDescTv.visibility = GONE
                binding.weatherIv.visibility = GONE
            } else {
                binding.weatherTv.visibility = VISIBLE
                binding.weatherDescTv.visibility = VISIBLE
                binding.weatherIv.visibility = VISIBLE

                Glide
                    .with(context)
                    .load("http://openweathermap.org/img/wn/${cards[position].weather[0].icon}.png")
                    .centerCrop()
                    .into(binding.weatherIv)

                binding.weatherTv.text = cards[position].weather[0].main + ", "
                binding.weatherDescTv.text = cards[position].weather[0].description.capitalize()
                binding.timeTv.text = cards[position].dtTxt
            }
        }
    }

    override fun getItemCount() = cards.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = HorRowChildBinding.bind(view)
    }
}