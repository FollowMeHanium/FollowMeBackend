package com.ghdev.followme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ghdev.followme.R

class PlaceDetailFragment : Fragment() {
    lateinit var hotPlaceRecyclerViewAdapter: HotPlaceRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place_detail, container, false)

    }
}