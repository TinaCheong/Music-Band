package com.tina.musicband.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tina.musicband.data.Songs
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.others.ProfileOthersViewModel

@Suppress("UNCHECKED_CAST")
class ProfileOthersViewModelFactory(
    private val repository: MusicBandRepository,
    private val userID : String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ProfileOthersViewModel::class.java) ->
                    ProfileOthersViewModel(repository, userID)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}