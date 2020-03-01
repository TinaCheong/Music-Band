package com.tina.musicband.others

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.data.source.MusicBandRepository

class ProfileOthersViewModel(
    private val repository: MusicBandRepository ,
    private val arguments: String
) : ViewModel() {

    private val _userID = MutableLiveData<String>().apply {
        value = arguments
    }

    val userID: LiveData<String>
        get() = _userID


}