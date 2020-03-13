package com.tina.musicband.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.data.User
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.login.UserManager
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class ProfileViewModel(private val repository: MusicBandRepository) : ViewModel() {

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

    private val _uploadStatus = MutableLiveData<Boolean>()

    val uploadStatus: LiveData<Boolean>
        get() = _uploadStatus

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        readUserDataResult()
        detectProfileDataChange()
    }

    private fun readUserDataResult(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.retrieveUsersData(UserManager.userToken.toString())

            _user.value = when (result) {
                is com.tina.musicband.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    MusicBandApplication.user = result.data
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

    fun retrievePostsCountResult(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.retrievePostsCount(UserManager.userToken.toString())

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

    fun retrieveFollowingsCountResult(){

        repository.retrieveFollowingsCount(UserManager.userToken.toString()) { followingCount ->
            _followingsCount.value = followingCount
        }
    }

    fun retrieveFollowersCountResult(){

        repository.retrieveFollowersCount(UserManager.userToken.toString()) { followerCount ->
            _followersCount.value = followerCount
        }
    }

    fun updateUserData(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val newUserData = mapOf("username" to _user.value?.username,
                "education" to _user.value?.education,
                "favouriteMusic" to _user.value?.favouriteMusic,
                "introduction" to _user.value?.introduction,
                "position" to _user.value?.position,
                "speciality" to _user.value?.speciality,
                "experience" to _user.value?.experience
            )

            val result = repository.updateUsersData(newUserData)

            _uploadStatus.value = when (result) {
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

    private fun detectProfileDataChange(){

        repository.detectProfileDataChange { user ->
            _user.value = user
        }

    }

    fun finishUpdate(){

        _uploadStatus.value = null

    }


}