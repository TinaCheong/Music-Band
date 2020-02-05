package com.tina.musicband.factory

import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.main.MainViewModel
import com.tina.musicband.profile.ProfileViewModel
import com.tina.musicband.search.SearchMusicViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: MusicBandRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(repository)

                isAssignableFrom(SearchMusicViewModel::class.java) ->
                    SearchMusicViewModel(repository)

                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}