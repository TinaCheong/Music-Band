package com.tina.musicband.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.data.User
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.login.UserManager


class ProfileViewModel(private val repository: MusicBandRepository) : ViewModel() {

    private val _checkAvatar = MutableLiveData<Boolean>()
    val checkAvatar: LiveData<Boolean>
        get() = _checkAvatar

    var profileAvatar = MutableLiveData<Int>()

    var profileBackground = MutableLiveData<Int>()


    fun getAvatarAndBackground() {

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(UserManager.userToken.toString())
            .get()
            .addOnSuccessListener {

                val user = it.toObject(User::class.java)

                user?.let { user ->

                    profileAvatar.value = user.avatar
                    profileBackground.value = user.background

                }

                loadAvatar()
            }

    }

    fun loadAvatar(){
        _checkAvatar.value = null
    }


}