package com.tina.musicband.dialog

import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.R
import com.tina.musicband.data.User
import com.tina.musicband.login.UserManager

enum class BACKGROUND(val value: Int){
    SQUARE(0),
    LINE(1),
    TRIANGLE(2),
    FLOWER(3),
    }

fun BACKGROUND.getDrawable(): Drawable {
    return when(this) {
        BACKGROUND.SQUARE -> MusicBandApplication.instance.applicationContext.resources.getDrawable(
            R.drawable.bg_square)
        BACKGROUND.LINE -> MusicBandApplication.instance.applicationContext.resources.getDrawable(
            R.drawable.bg_line)
        BACKGROUND.TRIANGLE -> MusicBandApplication.instance.applicationContext.resources.getDrawable(
            R.drawable.bg_triangle)
        BACKGROUND.FLOWER -> MusicBandApplication.instance.applicationContext.resources.getDrawable(
            R.drawable.bg_flower)
    }
}

fun Int.getBackgroundDrawable() : Drawable {
    return when(this) {
        BACKGROUND.SQUARE.value -> BACKGROUND.SQUARE.getDrawable()
        BACKGROUND.LINE.value -> BACKGROUND.LINE.getDrawable()
        BACKGROUND.TRIANGLE.value -> BACKGROUND.TRIANGLE.getDrawable()
        else -> BACKGROUND.FLOWER.getDrawable()
    }
}

fun String.getBackground() : Int {
    return if (this.toInt() < 0 || this.toInt() > 3 ) BACKGROUND.FLOWER.value else this.toInt()
}


class BackgroundDialogViewModel : ViewModel() {

    private var selectedBackground : View? = null
    private var user: User? = null

    fun selectBackground(view: View){

        user?.background = (view.tag as String).getBackground()

        if (selectedBackground == view) return

        selectedBackground?.alpha = 0f
        selectedBackground = view
        selectedBackground?.alpha = 1f
    }

    fun setUser(user: User) {
        this.user = user
    }

    fun save() {
        user?.let {
            FirebaseFirestore.getInstance().collection("users")
                .document(UserManager.userToken!!)
                .update("background", it.background)
        }

    }
}