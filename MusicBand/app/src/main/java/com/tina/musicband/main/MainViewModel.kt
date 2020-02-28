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

class MainViewModel(private val repository: MusicBandRepository) : ViewModel() {

    private val _comments = MutableLiveData<List<Comments>>()

    val comments: LiveData<List<Comments>>
        get() = _comments

    private val _posts = MutableLiveData<List<Posts>>()

    val posts: LiveData<List<Posts>>
        get() = _posts

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

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

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

    val _likeStatus = MutableLiveData<Boolean>()
        .apply {
            value = true
        }

    val likeStatus: LiveData<Boolean>
        get() = _likeStatus


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

    init {
        prepareSnapshotListener()
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

//    fun checkLike(){
//        _likeStatus.value = null
//    }
//
//    fun doneLike(){
//        _likeStatus.value = true
//    }

    val userAvatarMap = mutableMapOf<String, Int>()
    val list = mutableListOf<Posts>()

    fun prepareSnapshotListener() {

        _status.value = LoadApiStatus.LOADING

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(UserManager.userToken.toString())
            .get().addOnSuccessListener {
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
                                                                it.toObject(User::class.java)?.let { user ->
                                                                    userAvatarMap[user.userId] =
                                                                        user.avatar
                                                                }

                                                                list.addAll(post)

                                                                if(j == following.count() - 1){
                                                                    _posts.value?.let { previousList ->
                                                                        list.addAll(previousList)
                                                                    }

                                                                    _posts.value = list
                                                                }
                                                            }

                                                    }

//                                                    _status.value =
//                                                        LoadApiStatus.DONE
                                                }

                                        }


                                    }

                                }

                        }

                    }

            }


    }

    fun getProfileAvatar() {

        FirebaseFirestore.getInstance().collection("users")
            .document(UserManager.userToken.toString())
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    val user = it.result!!.toObject(User::class.java)

                    user?.let { user ->

                        userAvatarMap[user.userId] = user.avatar
                        _profileAvatar.value = userAvatarMap[user.userId]
                        _isProfileAvatarPrepared.value = true
                    }


                }
            }
    }
}