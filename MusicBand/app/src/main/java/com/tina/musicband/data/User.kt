package com.tina.musicband.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var username: String? = "",
    var userId: String = "",
    var fbToken: String = "",
    var googleToken: String = "",
    var background: Int = 0,
    var avatar: Int = 0,
    var speciality: String = "None",
    var education: String = "None",
    var position: String = "None",
    var favouriteMusic: String = "None",
    var introduction: String = "None",
    var experience: String = "None"

): Parcelable