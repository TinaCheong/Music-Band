package com.tina.musicband.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

@Parcelize
data class Posts(
    var type: String = "",
    var userName: String? = "",
    var userId: String? = "",
    var avatar: String = "",
    var postId: String = "",
    var createdTime: Long = 1,
    var title: String = "None",
    var date: String? = "",
    var description: String = "None",
    var image: String = "",
    var composer: String = "None",
    var song: Songs = Songs("No Title", "", "", "", "")
) : Parcelable