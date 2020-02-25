package com.tina.musicband.others

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.data.Songs
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.search.SearchMusicAdapter

class ProfileOthersViewModel(
    private val repository: MusicBandRepository ,
    private val arguments: Songs
) : ViewModel() {

    private val _song = MutableLiveData<Songs>().apply {
        value = arguments
    }

    val song: LiveData<Songs>
        get() = _song


}