package com.tina.musicband.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Songs(
    var songTitle: String = "No title",
    var songDuration: String = "",
    var songLink: String = "",
    var songId : String = ""
) : Parcelable