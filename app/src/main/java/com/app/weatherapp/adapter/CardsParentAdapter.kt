package com.app.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherapp.R
import com.app.weatherapp.databinding.HorRowBinding
import com.app.weatherapp.model.MainList

class CardsParentAdapter(val context: Context, val cards: ArrayList<ArrayList<MainList>>) :
    RecyclerView.Adapter<CardsParentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardsParentAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.hor_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardsParentAdapter.ViewHolder, position: Int) {

        with(holder) {
            binding.parentRecycler.apply {
                layoutManager =
                    LinearLayoutManager(context)
                adapter = CardsAdapter(this@CardsParentAdapter.context, cards[position])
            }
        }
    }

    override fun getItemCount() = cards.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = HorRowBinding.bind(view)
    }
}