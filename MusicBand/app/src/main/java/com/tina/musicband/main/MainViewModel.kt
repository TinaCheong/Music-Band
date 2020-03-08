package com.tina.musicband.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tina.musicband.data.*
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.login.UserManager
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

    private val _profileAvatar = MutableLiveData<Int>()

    val profileAvatar: LiveData<Int>
        get() = _profileAvatar

    private val _isProfileAvatarPrepared = MutableLiveData<Boolean>()

    val isProfileAvatarPrepared: LiveData<Boolean>
        get() = _isProfileAvatarPrepared

    fun doneReadingProfileAvatar() {
        _isProfileAvatarPrepared.value = null
    }

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

    fun retrieveUser() {
        _status.value = LoadApiStatus.LOADING
        coroutineScope.launch {
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(UserManager.userToken.toString())
                .get()
        }
    }

    fun prepareSnapshotListener() {

        _status.value = LoadApiStatus.LOADING

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(UserManager.userToken.toString())
            .get()
            .addOnSuccessListener {
                it.toObject(User::class.java)?.let { user ->
                    userAvatarMap[user.userId] = user.avatar
                }

                FirebaseFirestore.getInstance()
                    .collection("posts")
                    .orderBy("createdTime", Query.Direction.DESCENDING)
                    .whereEqualTo("userId", UserManager.userToken.toString())
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                        if (querySnapshot != null) {

                            _posts.value = querySnapshot.toObjects(Posts::class.java)

                            FirebaseFirestore.getInstance().collection("users")
                                .document(UserManager.userToken.toString())
                                .collection("following")
                                .get()
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {

                                        _status.value = LoadApiStatus.DONE

                                        val following =
                                            it.result!!.toObjects(Following::class.java)


                                        for (j in 0 until following.size) {
                                            val follower = following[j]

                                            FirebaseFirestore.getInstance().collection("posts")
                                                .orderBy("createdTime", Query.Direction.DESCENDING)
                                                .whereEqualTo("userId", follower.userId)
                                                .get()
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {

                                                        val post =
                                                            it.result!!.toObjects(Posts::class.java)

                                                        /** GET AVATAR FROM FOLLOWER */
                                                        FirebaseFirestore.getInstance().collection("users")
                                                            .document(follower.userId)
                                                            .get()
                                                            .addOnSuccessListener {
                                                                it.toObject(User::class.java)
                                                                    ?.let { user ->
                                                                        userAvatarMap[user.userId] =
                                                                            user.avatar
                                                                    }

                                                                list.addAll(post)

                                                                if (j == following.count() - 1) {
                                                                    _posts.value?.let { previousList ->
                                                                        list.addAll(previousList)
                                                                    }
                                                                    _posts.value = list
                                                                }
                                                            }
                                                    }

                                                }
                                        }
                                    }

                                }
                        }

                    }
            }
    }

    fun readUserDataResult(userID: String){

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

    fun retrievePostByUserID(userID : String){

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
}
