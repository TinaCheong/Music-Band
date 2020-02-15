package com.tina.musicband

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.util.CurrentFragmentType

class MusicBandViewModel(private val repository: MusicBandRepository) : ViewModel() {

    val currentFragmentType = MutableLiveData<CurrentFragmentType>()


}