package com.tina.musicband.ext

import androidx.fragment.app.Fragment
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MusicBandApplication).repository
    return ViewModelFactory(repository)
}