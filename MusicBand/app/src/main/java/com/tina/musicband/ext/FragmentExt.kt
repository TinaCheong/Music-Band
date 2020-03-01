package com.tina.musicband.ext

import androidx.fragment.app.Fragment
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.data.Songs
import com.tina.musicband.factory.ProfileOthersViewModelFactory
import com.tina.musicband.factory.ViewModelFactory
import com.tina.musicband.others.ProfileOthersViewModel

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MusicBandApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(userID: String): ProfileOthersViewModelFactory {
    val repository = (requireContext().applicationContext as MusicBandApplication).repository
    return ProfileOthersViewModelFactory(repository, userID)
}