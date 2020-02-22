package com.tina.musicband.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Like
import com.tina.musicband.data.Posts

class MusicItemViewModel : ViewModel(){

    private val _comments = MutableLiveData<List<Comments>>()

    val comments: LiveData<List<Comments>>
        get() = _comments

    private val _posts = MutableLiveData<List<Posts>>()

    val posts: LiveData<List<Posts>>
        get() = _posts

    private val _like = MutableLiveData<Like>()

    val like: LiveData<Like>
        get() = _like


    private val _commented = MutableLiveData<Boolean>()

    val commented: LiveData<Boolean>
        get() = _commented


    fun leaveComment(){
        _commented.value = true
    }

    fun doneCommented(){
        _commented.value = null
    }
}