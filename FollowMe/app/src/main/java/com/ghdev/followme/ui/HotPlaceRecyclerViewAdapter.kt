package com.ghdev.followme.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.data.test.PlaceInfo

class HotPlaceRecyclerViewAdapter (val dataList: ArrayList<PlaceInfo>)
    : RecyclerView.Adapter<HotPlaceRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HotPlaceRecyclerViewAdapter.Holder {
                
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hot_place, viewGroup, false)

        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size


    override fun onBindViewHolder(holder: HotPlaceRecyclerViewAdapter.Holder, position: Int) {
        holder.placename.text = dataList[position].name
        holder.address.text = dataList[position].address

        /*//glide
        Glide.with(ctx).load(dataList[position].img)
            .placeholder(R.drawable.ic_home_black)
            .into(holder.imgurl)*/

        holder.container.setOnClickListener {
            //##detailview로 갈 수 있도록 함

        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgurl = itemView.findViewById(R.id.img_hot_place_item) as ImageView
        var placename = itemView.findViewById(R.id.tv_place_name_item) as TextView
        var address = itemView.findViewById(R.id.tv_place_address_item) as TextView
        var container = itemView.findViewById(R.id.cl_hot_place_container) as ConstraintLayout
    }

}

