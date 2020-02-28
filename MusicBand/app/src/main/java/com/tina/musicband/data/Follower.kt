package com.tina.musicband.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Follower(
    var userId: String = ""
) : Parcelable