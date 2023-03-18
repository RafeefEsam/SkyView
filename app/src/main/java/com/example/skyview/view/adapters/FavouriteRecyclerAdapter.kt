package com.example.skyview.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.skyview.R
import com.example.skyview.databinding.FavouritItemBinding
import com.example.skyview.databinding.FavouriteItem2Binding
import com.example.skyview.model.FavoriteModel
import com.example.skyview.model.LocationData
import com.example.skyview.model.MyResponse

class FavouriteRecyclerAdapter(var myResponseList: List<MyResponse>,
                               private val onClick: (lat:Double, lon:Double) -> Unit,
                                private val onClickItem:(location:LocationData) -> Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val ITEM_TYPE_1 : Int = 1
    private val ITEM_TYPE_2 : Int = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == ITEM_TYPE_1){
            val view : FavouritItemBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.favourit_item, parent, false)
            return FavouriteViewHolder(view)
        }else{
            val view : FavouriteItem2Binding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.favourite_item_2, parent, false)
            return FavouriteViewHolder2(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val response = myResponseList[position]
        if(holder.itemViewType == ITEM_TYPE_1){
            val viewHolder1 = holder as FavouriteViewHolder
            viewHolder1.itemBinding.responseItem = response
            viewHolder1.itemBinding.remove.setOnClickListener {
                onClick(response.lat, response.lon)
            }
            viewHolder1.itemBinding.item1Click.setOnClickListener {
                onClickItem(LocationData(response.lat, response.lon))
            }
        }else{
            val viewHolder2 = holder as FavouriteViewHolder2
            viewHolder2.itemBinding.responseItem = response
            viewHolder2.itemBinding.remove.setOnClickListener {
                onClick(response.lat, response.lon)
            }
            viewHolder2.itemBinding.item2Click.setOnClickListener {
                onClickItem(LocationData(response.lat, response.lon))
            }
        }
    }



    override fun getItemCount(): Int {
        return myResponseList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position % 2 == 0) {
            return ITEM_TYPE_1
        } else {
            return ITEM_TYPE_2
        }
    }



    inner class FavouriteViewHolder(var itemBinding: FavouritItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){
    }

    inner class FavouriteViewHolder2(var itemBinding: FavouriteItem2Binding):
        RecyclerView.ViewHolder(itemBinding.root){
    }
}