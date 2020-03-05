package com.tina.musicband.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.data.Songs
import com.tina.musicband.data.User
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchMusicViewModel(private val repository: MusicBandRepository) : ViewModel() {

    private val _songs = MutableLiveData<List<Songs>>()

    val songs: LiveData<List<Songs>>
        get() = _songs

    private val _selectedSong = MutableLiveData<Songs>()

    val selectedSong: LiveData<Songs>
        get() = _selectedSong

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getAllSongsResult()
    }


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

    private fun getAllSongsResult(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getAllSongs()

            _songs.value = when (result) {
                is com.tina.musicband.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is com.tina.musicband.data.Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is com.tina.musicband.data.Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }



    }

    fun selectSong(song: Songs){

        _selectedSong.value = song
    }

    fun doneNavigate(){

        _selectedSong.value = null
    }

}