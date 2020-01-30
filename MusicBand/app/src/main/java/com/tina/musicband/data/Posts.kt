package com.tina.musicband.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

data class Posts(
    var type: String,
    var userName: String,
    var userId: String,
    var postId: String ,
    var createdTime: Long,
    var title: String,
    var date: String,
    var description: String,
    var image: String,
    var composer: String,
    var musicLink: String,
    var like: Int,
    var comments: Comments
)