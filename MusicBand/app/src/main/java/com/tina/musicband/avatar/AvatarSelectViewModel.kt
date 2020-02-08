package com.tina.musicband.avatar

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AvatarSelectViewModel : ViewModel() {

    private var selectedIcon : View? = null

    fun selectAvatar(view: View){

        if (selectedIcon == view) return

        selectedIcon?.alpha = 0f
        selectedIcon = view
        selectedIcon?.alpha = 1f
    }
}