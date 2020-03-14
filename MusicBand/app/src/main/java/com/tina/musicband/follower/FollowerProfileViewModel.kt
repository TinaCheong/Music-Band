package com.tina.musicband.follower

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.data.Follower
import com.tina.musicband.data.Result
import com.tina.musicband.data.User
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.data.source.remote.MusicBandRemoteDataSource
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FollowerProfileViewModel (private val repository: MusicBandRepository): ViewModel(){

    private val _followers = MutableLiveData<List<Follower>>()

    val followers: LiveData<List<Follower>>
        get() = _followers

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

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

    private fun getFollowersResult(){

        repository.getFollowers { followerCount ->
                _followers.value = followerCount
            }
    }

    fun readUserDataResult(userID: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            _user.value = when (val result = repository.retrieveUsersData(userID)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
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