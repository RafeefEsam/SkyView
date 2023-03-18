package com.example.skyview.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.skyview.R
import com.example.skyview.databinding.DayItemBinding
import com.example.skyview.databinding.HourItemBinding
import com.example.skyview.model.Daily

class DailyRecyclerAdapter (var daily: List<Daily>, var context:Context):
 RecyclerView.Adapter<DailyRecyclerAdapter.DailyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val view : DayItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.day_item, parent, false)
        return DailyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val currentDay = daily[position]
        holder.itemBinding.day = currentDay
    }
    override fun getItemCount(): Int {
        return daily.size - 1
    }

    inner class DailyViewHolder(var itemBinding: DayItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){

    }

}