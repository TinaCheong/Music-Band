package com.tina.musicband.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainViewModel(private val repository: MusicBandRepository) : ViewModel(){

    private val _comments = MutableLiveData<List<Comments>>()

    val comments: LiveData<List<Comments>>
        get() = _comments

    private val _posts = MutableLiveData<List<Posts>>()

    val posts: LiveData<List<Posts>>
        get() = _posts

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
    // service to stop
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}