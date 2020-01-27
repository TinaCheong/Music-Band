package com.tina.musicband.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

data class Posts(
    val type: String,
    val userName: String,
    val userId: String,
    val postId: String ,
    val createdTime: Timestamp,
    val title: String,
    val date: Timestamp,
    val description: String,
    val image: String,
    val composer: String,
    val musicLink: String,
    val like: Int
)