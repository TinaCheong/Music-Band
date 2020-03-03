package com.tina.musicband.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tina.musicband.MusicBandViewModel
import com.tina.musicband.addevent.AddEventViewModel
import com.tina.musicband.avatar.AvatarSelectViewModel
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.dialog.AvatarDialogViewModel
import com.tina.musicband.dialog.BackgroundDialogViewModel
import com.tina.musicband.follower.FollowerProfileViewModel
import com.tina.musicband.login.LoginViewModel
import com.tina.musicband.main.MainViewModel
import com.tina.musicband.profile.ProfileViewModel
import com.tina.musicband.search.SearchMusicViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: MusicBandRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MusicBandViewModel::class.java) ->
                    MusicBandViewModel(repository)

                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repository)

                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(repository)

                isAssignableFrom(SearchMusicViewModel::class.java) ->
                    SearchMusicViewModel(repository)

                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(repository)

                isAssignableFrom(AddEventViewModel::class.java) ->
                    AddEventViewModel(repository)

                isAssignableFrom(AvatarSelectViewModel::class.java) ->
                    AvatarSelectViewModel(repository)

                isAssignableFrom(AvatarDialogViewModel::class.java) ->
                    AvatarDialogViewModel(repository)

                isAssignableFrom(BackgroundDialogViewModel::class.java) ->
                    BackgroundDialogViewModel(repository)

                isAssignableFrom(FollowerProfileViewModel::class.java) ->
                    FollowerProfileViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}