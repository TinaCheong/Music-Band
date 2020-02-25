package com.tina.musicband.avatar

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.R
import com.tina.musicband.data.User
import com.tina.musicband.login.UserManager

enum class AVATAR(val value: Int){
    WOMAN_WITH_BROWN_HAIR(0),
    WOMAN_WITH_RED_HAIR(1),
    WOMAN_WITH_BLACK_HAIR(2),
    MAN_WITH_DARK_BROWN_HAIR(3),
    MAN_WITH_GRAY_HAIR(4),
    MAN_WITH_BROWN_HAIR(5)
}

fun AVATAR.getDrawable(): Drawable {
    return when(this) {
        AVATAR.WOMAN_WITH_BROWN_HAIR -> MusicBandApplication.instance.applicationContext.resources.getDrawable(
            R.drawable.ic_woman_1)
        AVATAR.WOMAN_WITH_RED_HAIR -> MusicBandApplication.instance.applicationContext.resources.getDrawable(
            R.drawable.ic_woman_2)
        AVATAR.WOMAN_WITH_BLACK_HAIR -> MusicBandApplication.instance.applicationContext.resources.getDrawable(
            R.drawable.ic_woman_3)
        AVATAR.MAN_WITH_DARK_BROWN_HAIR -> MusicBandApplication.instance.applicationContext.resources.getDrawable(
            R.drawable.ic_man_1)
        AVATAR.MAN_WITH_GRAY_HAIR -> MusicBandApplication.instance.applicationContext.resources.getDrawable(
            R.drawable.ic_man_2)
        AVATAR.MAN_WITH_BROWN_HAIR -> MusicBandApplication.instance.applicationContext.resources.getDrawable(
            R.drawable.ic_man_3)

    }
}

fun Int.getAvatarDrawable() : Drawable {
    return when(this) {
        AVATAR.WOMAN_WITH_BROWN_HAIR.value -> AVATAR.WOMAN_WITH_BROWN_HAIR.getDrawable()
        AVATAR.WOMAN_WITH_RED_HAIR.value -> AVATAR.WOMAN_WITH_RED_HAIR.getDrawable()
        AVATAR.WOMAN_WITH_BLACK_HAIR.value -> AVATAR.WOMAN_WITH_BLACK_HAIR.getDrawable()
        AVATAR.MAN_WITH_DARK_BROWN_HAIR.value -> AVATAR.MAN_WITH_DARK_BROWN_HAIR.getDrawable()
        AVATAR.MAN_WITH_GRAY_HAIR.value -> AVATAR.MAN_WITH_GRAY_HAIR.getDrawable()
        else -> AVATAR.MAN_WITH_BROWN_HAIR.getDrawable()
    }
}

fun String.getDrawable() : Drawable {
    return this.toInt().getAvatarDrawable()
}

fun String.getAvatar() : Int {
    return if (this.toInt() < 0 || this.toInt() > 5) AVATAR.MAN_WITH_BROWN_HAIR.value else this.toInt()
}

class AvatarSelectViewModel : ViewModel() {

    private var selectedIcon : View? = null
    private var user: User? = null

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

    fun save() {
        user?.let {
            FirebaseFirestore.getInstance().collection("users")
                .document(UserManager.userToken!!)
                .update("avatar", it.avatar)
        }

    }
}