package com.ghdev.followme.data.test

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceInfo(
    var img : String,
    var name : String,
    var address : String
):Parcelable