package com.tina.musicband.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var username: String? = "",
    var userId: String? = "",
    var fbToken: String = "",
    var googleToken: String = "",
    var background: String = "",
    var avatar: String = "",
    var speciality: String = "",
    var education: String = "",
    var position: String = "",
    var favouriteMusic: String = "",
    var introduction: String = "",
    var experience: String = "",
    var songs: Songs = Songs("", "","", "", ""),
    var posts: Posts = Posts("","","","","","",123,"","", "","","",
        song = Songs("","","","",""))

): Parcelable