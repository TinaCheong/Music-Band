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
    var eventId: String = "",
    var createdTime: Long = 1,
    var title: String = "",
    var date: String? = "",
    var description: String = "",
    var image: String = "",
    var composer: String = "",
    var like: Int = 1,
    var song: Songs = Songs("", "", "", "", "")
) : Parcelable