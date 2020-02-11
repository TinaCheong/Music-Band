package com.tina.musicband.dialog

import android.view.View
import androidx.lifecycle.ViewModel

class BackgroundDialogViewModel : ViewModel() {

    private var selectedBackground : View? = null

    fun selectBackground(view: View){

        if (selectedBackground == view) return

        selectedBackground?.alpha = 0f
        selectedBackground = view
        selectedBackground?.alpha = 1f
    }
}