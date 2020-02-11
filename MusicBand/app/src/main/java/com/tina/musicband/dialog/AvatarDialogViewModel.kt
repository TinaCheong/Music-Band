package com.tina.musicband.dialog

import android.view.View
import androidx.lifecycle.ViewModel

class AvatarDialogViewModel : ViewModel() {

    private var selectedIcon : View? = null

    fun selectAvatar(view: View){

        if (selectedIcon == view) return

        selectedIcon?.alpha = 0f
        selectedIcon = view
        selectedIcon?.alpha = 1f
    }
}