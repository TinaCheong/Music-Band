package com.tina.musicband.dialog

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.avatar.getAvatar
import com.tina.musicband.data.User
import com.tina.musicband.login.UserManager

class AvatarDialogViewModel : ViewModel() {

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