package com.tina.musicband.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.data.Songs
import com.tina.musicband.data.User
import com.tina.musicband.data.source.MusicBandRepository

class SearchMusicViewModel(private val repository: MusicBandRepository) : ViewModel() {

    private val _selectedSong = MutableLiveData<Songs>()

    val selectedSong: LiveData<Songs>
        get() = _selectedSong

    fun selectSong(song: Songs){

        _selectedSong.value = song

    }

    fun doneNavigate(){

        _selectedSong.value = null

    }

    private val _songs = MutableLiveData<List<Songs>>()
    val songs: LiveData<List<Songs>>
        get() = _songs

    fun fetchSongsByID(userID: String) {

        if (userID.isEmpty()) return

        FirebaseFirestore.getInstance().collection("songs")
            .whereEqualTo("userId", userID)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    _songs.value = it.result!!.toObjects(Songs::class.java)
                }

            }
    }

}