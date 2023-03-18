package com.example.skyview.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.skyview.R
import com.example.skyview.databinding.AlarmItemBinding
import com.example.skyview.model.AlarmPojo
import kotlin.math.log

class AlarmAdapter(
    var alarmsList: List<AlarmPojo>,
    private val onClick: (AlarmPojo) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view : AlarmItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val currentAlarm = alarmsList[position]
        holder.itemBinding.alarmData = currentAlarm
        holder.itemBinding.removeAlarm.setOnClickListener {
            onClick(currentAlarm)
        }
    }


    override fun getItemCount(): Int {
        Log.i("AlertFragment", "getItemCount: alarnlist ${alarmsList.size}")
        return alarmsList.size
    }

    inner class AlarmViewHolder(var itemBinding: AlarmItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){
    }
}