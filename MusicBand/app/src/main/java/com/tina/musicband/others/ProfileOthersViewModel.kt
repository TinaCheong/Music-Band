package com.tina.musicband.others

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.data.User
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileOthersViewModel(private val repository: MusicBandRepository , private val arguments: String) : ViewModel() {

    private val _userID = MutableLiveData<String>().apply {
        value = arguments
    }

    val userID: LiveData<String>
        get() = _userID

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    private val _postsCount = MutableLiveData<Int>()

    val postsCount: LiveData<Int>
        get() = _postsCount

    private val _followersCount = MutableLiveData<Int>()

    val followersCount: LiveData<Int>
        get() = _followersCount

    private val _followingsCount = MutableLiveData<Int>()

    val followingCount: LiveData<Int>
        get() = _followingsCount

    private val _checkFollowStatus = MutableLiveData<Boolean>()

    val checkFollowStatus: LiveData<Boolean>
        get() = _checkFollowStatus

    private val _followStatus = MutableLiveData<Boolean>()

    val followStatus: LiveData<Boolean>
        get() = _followStatus

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        readUserDataResult()
        retrievePostsCountResult()
        retrieveFollowersCountResult()
        retrieveFollowingsCountResult()
        checkIfUserIsFollowed()

    }

    private fun readUserDataResult(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.retrieveUsersData(userID.value!!)

            _user.value = when (result) {
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

    private fun retrievePostsCountResult(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.retrievePostsCount(userID.value!!)

            _postsCount.value = when (result) {
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

    private fun retrieveFollowingsCountResult(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.retrieveFollowingsCount(userID.value!!)

            _followingsCount.value = when (result) {
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

    private fun retrieveFollowersCountResult(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.retrieveFollowersCount(userID.value!!)

            _followersCount.value = when (result) {
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

    private fun checkIfUserIsFollowed(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.checkIfUserIsFollowed(userID.value!!)

            _checkFollowStatus.value = when (result) {
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

    fun followUser(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.followUser(userID.value!!)

            _checkFollowStatus.value = when (result) {
                is com.tina.musicband.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    Toast.makeText(MusicBandApplication.instance.applicationContext, "Follow Success", Toast.LENGTH_SHORT).show()
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

    fun unfollowUser(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.unfollowUser(userID.value!!)

            _checkFollowStatus.value = when (result) {
                is com.tina.musicband.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    Toast.makeText(MusicBandApplication.instance.applicationContext, "Unfollow User", Toast.LENGTH_SHORT).show()
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

    fun setUserID(userID: String){

        _userID.value = userID

    }


}