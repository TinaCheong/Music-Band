package com.tina.musicband.main

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import androidx.lifecycle.*
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.data.*
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(private val repository: MusicBandRepository) : ViewModel() {

    private val _comments = MutableLiveData<List<Comments>>()

    val comments: LiveData<List<Comments>>
        get() = _comments

    private val _posts = MutableLiveData<List<Posts>>()

    val posts: LiveData<List<Posts>>
        get() = _posts

    private val _userPosts = MutableLiveData<List<PostSealedItem>>()

    val userPosts: LiveData<List<PostSealedItem>>
        get() = _userPosts

    private val _like = MutableLiveData<Like>()

    val like: LiveData<Like>
        get() = _like

    val postItems = Transformations.map(_posts) { posts ->

        val items = mutableListOf<PostSealedItem>()

        posts.forEach {
            when (it.type) {
                POST_TYPES.MUSIC.value -> {
                    items.add(PostSealedItem.MusicItem(it))
                }

                POST_TYPES.EVENT.value -> {
                    items.add(PostSealedItem.EventItem(it))
                }
            }
        }

        items
    }

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    private val _commented = MutableLiveData<Boolean>()

    val commented: LiveData<Boolean>
        get() = _commented

    private val _setFab = MutableLiveData<Boolean>()

    val setFab: LiveData<Boolean>
        get() = _setFab

    private val _followingsID = MutableLiveData<MutableList<String>>()

    val followingsID: LiveData<MutableList<String>>
        get() = _followingsID

    private val _followingData = MutableLiveData<List<User>>()

    val followingData: LiveData<List<User>>
        get() = _followingData

    private val _likeStatus = MutableLiveData<Boolean>()
        .apply {
            value = true
        }

    val likeStatus: LiveData<Boolean>
        get() = _likeStatus


    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun leaveComment() {
        _commented.value = true
    }

    fun doneCommented() {
        _commented.value = null
    }

    fun hideFab() {
        _setFab.value = null
    }

    fun showFab() {
        _setFab.value = true
    }


    val userAvatarMap = mutableMapOf<String, Int>()
    val list = mutableListOf<Posts>()

    fun readUserDataResult(userID: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            _user.value = when (val result = repository.retrieveUsersData(userID)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    userAvatarMap[result.data.userId] = result.data.avatar
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

    private fun readFollowingDataResult(userIDs: List<String>) {

        if (userIDs.isEmpty()) {
            _followingData.value = listOf()
            return
        }

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            _followingData.value = when (val result = repository.retrieveAllUsersData(userIDs)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    for (user in result.data) {
                        userAvatarMap[user.userId] = user.avatar
                    }
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

    fun retrieveUserPostsInstantly(userIDs: MutableList<String>) {

        repository.retrievePostsDataInstantly(userIDs) { posts ->
            _posts.value = posts
        }
    }

    private fun getUserFollowingsID() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            _followingsID.value = when (val result =
                repository.getUsersFollowingsID()) {
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


    fun retrievePostByUserID(userID: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            _userPosts.value = when (val result = repository.retrieveUsersPosts(userID)) {
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

    val isUserDataReadyToGetPosts = MediatorLiveData<Boolean>().apply {
        addSource(_user) {
            value = checkIfUserAndFollowingsDataAreReady()
            if (_user.value != null) getUserFollowingsID()
        }
        addSource(_followingsID) {
            value = checkIfUserAndFollowingsDataAreReady()
            if (_followingsID.value != null) readFollowingDataResult(_followingsID.value!!)
        }
        addSource(_followingData){
            value = checkIfUserAndFollowingsDataAreReady() }
    }

    private fun checkIfUserAndFollowingsDataAreReady(): Boolean {
        return _user.value != null && _followingsID.value != null && _followingData.value != null
    }

    fun doneReadingPosts() {
        isUserDataReadyToGetPosts.value = null
    }

}
