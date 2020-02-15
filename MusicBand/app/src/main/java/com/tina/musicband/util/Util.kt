package com.tina.musicband.util

import com.tina.musicband.MusicBandApplication

object Util {

    fun getString(resourceId: Int): String {
        return MusicBandApplication.instance.getString(resourceId)
    }

}