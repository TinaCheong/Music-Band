package com.tina.musicband.addmusic

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Result
import com.tina.musicband.data.Songs
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.main.POST_TYPES
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class AddMusicViewModel(private val repository: MusicBandRepository) : ViewModel() {

    private val _posts = MutableLiveData<Posts>().apply {
        value = Posts()
    }

    val posts: LiveData<Posts>
        get() = _posts

    private val _songPath = MutableLiveData<Uri>()

    val songPath: LiveData<Uri>
        get() = _songPath

    private val _imagePath = MutableLiveData<Uri>()

    private val _imageLink = MutableLiveData<String>()

    private val _songLink = MutableLiveData<String>()

    private val _song = MutableLiveData<Songs>().apply {
        value = Songs()
    }

    val song: LiveData<Songs>
        get() = _song

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _isMusicPostPublished = MutableLiveData<Boolean>()

    private val _isSongPublished = MutableLiveData<Boolean>()

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun publishMusicPost() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.publishMusicPost(
                Posts(
                    type = POST_TYPES.MUSIC.value,
                    composer = _posts.value?.composer!!,
                    title = _posts.value?.title,
                    description = _posts.value?.description,
                    song = Songs(
                        songTitle = _posts.value?.title!!,
                        songLink = _songLink.value!!,
                        cover = _imageLink.value,
                        songDuration = getDurationFromMilli(findSongDuration(_songPath.value))
                    )
                )
            )
                ) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _isMusicPostPublished.value = true
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun publishSong() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.publishSong(
                Songs(
                    songTitle = _posts.value?.title!!,
                    songLink = _songLink.value!!,
                    cover = _imageLink.value,
                    songDuration = getDurationFromMilli(
                        findSongDuration(_songPath.value)
                    )
                )
            )) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _isSongPublished.value = true
                }

                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    private fun uploadImage() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            Toast.makeText(
                MusicBandApplication.instance.applicationContext,
                "Uploading please wait...",
                Toast.LENGTH_SHORT
            ).show()

            _imageLink.value = when (val result = repository.uploadImage(_imagePath.value!!)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
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

    private fun uploadSong() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            _songLink.value = when (val result = repository.uploadSong(songPath.value!!)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
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

    private fun getDurationFromMilli(duration: Int): String {
        val date = Date(duration.toLong())
        val simpleDataFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        val time = simpleDataFormat.format(date)
        return time
    }

    private fun findSongDuration(audioUri: Uri?): Int {
        var timeInMilliSec = 0

        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(MusicBandApplication.instance.applicationContext, audioUri)
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            timeInMilliSec = Integer.parseInt(time)

            retriever.release()
            return timeInMilliSec

        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }

    fun uploadAudioToFirebase() {

        if (_songPath.value == null || _imagePath.value == null) {

            Toast.makeText(
                MusicBandApplication.instance.applicationContext,
                "Please select a file",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            uploadImage()
        }
    }

    fun setSongPath(uri: Uri?) {
        _songPath.value = uri
    }

    fun setImagePath(uri: Uri?) {
        _imagePath.value = uri
    }

    val isMusicPostReadyToPost = MediatorLiveData<Boolean>().apply {
        addSource(_imageLink) {
            value = checkIfSongAndImageLinksAreReady()
            if (_imageLink.value != null) uploadSong()
        }
        addSource(_songLink) { value = checkIfSongAndImageLinksAreReady() }
    }

    private fun checkIfSongAndImageLinksAreReady(): Boolean {
        return _imageLink.value != null && _songLink.value != null
    }

    fun donePostingMusic() {
        isMusicPostReadyToPost.value = null
    }

    val isMusicPostingFinished = MediatorLiveData<Boolean>().apply {
        addSource(_isMusicPostPublished) { value = checkIfMusicPostHasBeenPublished() }
        addSource(_isSongPublished) { value = checkIfMusicPostHasBeenPublished() }
    }

    fun confirmMusicPostingFinished() {
        isMusicPostingFinished.value = null
    }

    private fun checkIfMusicPostHasBeenPublished(): Boolean {
        return _isMusicPostPublished.value == true && _isSongPublished.value == true
    }

}
