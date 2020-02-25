package com.tina.musicband.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Following(
    var userId: String = ""
) : Parcelable