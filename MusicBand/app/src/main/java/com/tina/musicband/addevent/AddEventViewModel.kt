package com.tina.musicband.addevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.data.Posts
import com.tina.musicband.data.source.MusicBandRepository
import java.text.SimpleDateFormat
import java.util.*

class AddEventViewModel(private val repository: MusicBandRepository) : ViewModel() {

    private val _posts = MutableLiveData<Posts>()

    val posts: LiveData<Posts>
        get() = _posts

    val selectedDate = MutableLiveData<String>()
        .apply {
            value = "Select a day"
        }

    fun setEventTime(date: Date){
        val myFormat = "yyyy/MM/dd"
        val sdf = SimpleDateFormat(myFormat, Locale.TAIWAN)
        _posts.value?.date = date.time.toString()
        selectedDate.value = sdf.format(date)
    }

}