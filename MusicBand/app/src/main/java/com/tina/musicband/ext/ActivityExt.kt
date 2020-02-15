package com.tina.musicband.ext

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.factory.ViewModelFactory

fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as MusicBandApplication).repository
    return ViewModelFactory(repository)
}

fun Activity?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}