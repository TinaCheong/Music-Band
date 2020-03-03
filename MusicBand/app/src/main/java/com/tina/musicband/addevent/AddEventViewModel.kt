package com.tina.musicband.addevent

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.data.Posts
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.main.POST_TYPES
import com.tina.musicband.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEventViewModel(private val repository: MusicBandRepository) : ViewModel() {

    private val _posts = MutableLiveData<Posts>().apply {
        value = Posts()
    }

    val posts: LiveData<Posts>
        get() = _posts

    private val _imagePath = MutableLiveData<Uri>()

    val imagePath: LiveData<Uri>
        get() = _imagePath

    private val _uploadStatus = MutableLiveData<Boolean>()

    val uploadStatus: LiveData<Boolean>
        get() = _uploadStatus

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val selectedDate = MutableLiveData<String>()
        .apply {
            value = "Select a day"
        }

    val uploadHint = MutableLiveData<String>()
        .apply {
            value = "No file selected"
        }

    fun setEventTime(date: Date){
        val myFormat = "yyyy/MM/dd"
        val sdf = SimpleDateFormat(myFormat, Locale.TAIWAN)
        _posts.value?.date = date.time.toString()
        selectedDate.value = sdf.format(date)
    }

    private fun publishPost(post: Posts){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.publishPost(post)) {
                is com.tina.musicband.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                }
                is com.tina.musicband.data.Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is com.tina.musicband.data.Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }

    }

    private fun uploadData() {

        _status.value = LoadApiStatus.LOADING
        Toast.makeText(
            MusicBandApplication.instance.applicationContext, "Uploading please wait...", Toast.LENGTH_SHORT
        ).show()

        coroutineScope.launch {

            when (val result = repository.uploadImage(imagePath.value!!)) {
                is com.tina.musicband.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _posts.value?.let {
                     publishPost(Posts(type = POST_TYPES.EVENT.value,
                         date = selectedDate.value,
                         image = result.data,
                         title = it.title,
                         description = it.description))
                    }

                    _uploadStatus.value = true
                }
                is com.tina.musicband.data.Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is com.tina.musicband.data.Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun uploadPostDataToFirebase(){

        if (imagePath.value == null) {
            Toast.makeText(
                MusicBandApplication.instance.applicationContext, "Please select an image", Toast.LENGTH_SHORT
            ).show()
        } else {
            uploadData()
        }


    }

    fun finishUpload(){

        _uploadStatus.value = null
    }

    fun setImagePath(uri: Uri?){

        _imagePath.value = uri
    }



    }

