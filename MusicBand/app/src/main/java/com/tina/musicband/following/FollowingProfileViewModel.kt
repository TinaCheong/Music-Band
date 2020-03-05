package com.tina.musicband.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.data.Follower
import com.tina.musicband.data.Following
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FollowingProfileViewModel(private val repository: MusicBandRepository): ViewModel(){

    private val _followings = MutableLiveData<List<Following>>()

    val followings: LiveData<List<Following>>
        get() = _followings

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getFollowersResult()
    }

    fun getFollowersResult(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getFollowings()

            _followings.value = when (result) {
                is com.tina.musicband.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is com.tina.musicband.data.Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is com.tina.musicband.data.Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }



    }


}