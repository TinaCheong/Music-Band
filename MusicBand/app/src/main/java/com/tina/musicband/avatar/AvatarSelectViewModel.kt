package com.tina.musicband.avatar

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.data.User
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class AvatarSelectViewModel(private val repository: MusicBandRepository) : ViewModel() {

    private var selectedIcon : View? = null
    private var user: User? = null

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _setting = MutableLiveData<Boolean>()

    val setting: LiveData<Boolean>
        get() = _setting

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun selectAvatar(view: View){

        user?.avatar = (view.tag as String).getAvatar()

        if (selectedIcon == view) return

        selectedIcon?.alpha = 0f
        selectedIcon = view
        selectedIcon?.alpha = 1f
    }

    fun setUser(user: User) {
        this.user = user
    }

    fun saveAvatar() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.changeAvatarAndBackground(user!!)) {
                is com.tina.musicband.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _setting.value = true
                }
                is com.tina.musicband.data.Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is com.tina.musicband.data.Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }

    }

    fun finishSetting(){

        _setting.value = null
    }
}