package com.tina.musicband.login

import android.content.Context
import com.tina.musicband.MusicBandApplication

object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_TOKEN = "user_token"
    private const val USER_NAME = "user_name"

    var userToken: String? = null
        get() = MusicBandApplication.instance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_TOKEN, null)
        set(value) {
            field = when (value) {
                null -> {
                    MusicBandApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_TOKEN)
                        .apply()
                    null
                }
                else -> {
                    MusicBandApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_TOKEN, value)
                        .apply()
                    value
                }
            }
        }

    var userName: String? = null
        get() = MusicBandApplication.instance
            .getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
            .getString(USER_TOKEN, null)
        set(value) {
            field = when (value) {
                null -> {
                    MusicBandApplication.instance
                        .getSharedPreferences(USER_NAME, Context.MODE_PRIVATE).edit()
                        .remove(USER_TOKEN)
                        .apply()
                    null
                }
                else -> {
                    MusicBandApplication.instance
                        .getSharedPreferences(USER_NAME, Context.MODE_PRIVATE).edit()
                        .putString(USER_TOKEN, value)
                        .apply()
                    value
                }
            }
        }
}