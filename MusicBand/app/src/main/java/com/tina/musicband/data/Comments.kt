package com.tina.musicband.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comments(
    var comment: String = "",
    var username: String = "",
    var time: Long = 1
) : Parcelable