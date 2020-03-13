package com.tina.musicband.ext

fun Int.createTime(): String {
    var timeLevel: String
    val min = this / 1000 / 60
    val sec = this / 1000 % 60
    timeLevel = String.format("%02d:%02d", min, sec)

    return timeLevel
}