package com.tina.musicband.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: MusicBandRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}