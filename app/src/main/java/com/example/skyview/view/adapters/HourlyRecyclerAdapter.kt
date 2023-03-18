package com.example.skyview.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.skyview.R
import com.example.skyview.databinding.HourItemBinding
import com.example.skyview.model.Hourly
import java.util.*

class HourlyRecyclerAdapter (var hourly : List<Hourly>) :
    RecyclerView.Adapter<HourlyRecyclerAdapter.HourlyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val view : HourItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.hour_item, parent, false)
        return HourlyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hourly.size / 2
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val currentHour = hourly[position]
        holder.itemBinding.hour = currentHour
    }

    inner class HourlyViewHolder(var itemBinding: HourItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){

    }
}