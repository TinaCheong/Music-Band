package com.tina.musicband.addmusic

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tina.musicband.R
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.LayoutAddMusicMainBinding
import com.tina.musicband.login.UserManager
import com.tina.musicband.main.POST_TYPES
import com.tina.musicband.network.LoadApiStatus
import java.lang.Exception
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class AddMusicFragment : Fragment() {

    lateinit var binding: LayoutAddMusicMainBinding
    private var audioUri: Uri? = null
    private var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private lateinit var imageStorageReference: StorageReference
    private lateinit var songStorageReference: StorageReference
    private val songId = FirebaseFirestore.getInstance().collection("songs").document().id
    private val songsReference = FirebaseFirestore.getInstance().collection("songs").document(songId)
    private val postId = FirebaseFirestore.getInstance().collection("posts").document().id
    private val postsReference = FirebaseFirestore.getInstance().collection("posts").document(postId)
    private var status = LoadApiStatus.LOADING

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.layout_add_music_main, container, false)

        binding.lifecycleOwner = this

        //Navigate to the Main Page
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addMusicBorder.setOnClickListener {
            openAudioFile()
        }

        binding.submitButton.setOnClickListener {
            uploadAudioToFirebase()
        }

        binding.imageBorder.setOnClickListener {
            openImageFile()
        }


        return binding.root
    }

    fun openAudioFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("audio/*")
        startActivityForResult(intent, 101)
    }

    fun openImageFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, 102)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && data?.getData() != null) {

            data.getData().let {
                audioUri = it
            }
            val fileName = getFileName(audioUri)
            binding.musicUploadHint.setText(fileName)
            binding.musicTitleShow.setText(fileName)
            binding.addMusicIcon.visibility = View.INVISIBLE
            binding.musicUploadHint.visibility = View.INVISIBLE
            binding.musicTitleShow.visibility = View.VISIBLE

        }

        if (requestCode == 102 && resultCode == RESULT_OK && data?.getData() != null) {

            data.getData().let {
                imageUri = it
            }
            Glide.with(this).load(imageUri).centerCrop().into(binding.imageBorder)
            binding.coverUploadHint.visibility = View.GONE
            binding.coverUploadIcon.visibility = View.GONE
        }

    }

    private fun getFileName(audioUri: Uri?): String {
        var result: String? = null
        var cursor: Cursor? = null
        if (audioUri?.getScheme().equals("content")) {
            audioUri?.let {
                cursor = context?.getContentResolver()?.query(it, null, null, null)
            }

            try {
                cursor?.let {
                    if (it.moveToFirst()) {
                        result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            } finally {
                cursor?.close()
            }

        }


        if (result == null) {
            result = audioUri?.getPath()
            val cut = result?.lastIndexOf("/")
            if (cut != -1) {
                cut?.let {
                    result = result?.substring(it.plus(1))
                }
            }


        }
        return result.toString()
    }

    private fun uploadAudioToFirebase() {

        if (binding.musicUploadHint.visibility == View.VISIBLE || binding.coverUploadHint.visibility == View.VISIBLE) {

            Toast.makeText(
                activity, "Please select a file", Toast.LENGTH_SHORT
            ).show()

        }
        else {

            setProgressBar()
            uploadFile()
        }
    }

    private fun uploadFile() {

        var durationText = ""

        if (audioUri != null) {

            status

            Toast.makeText(
                activity, "Uploading please wait...", Toast.LENGTH_SHORT
            ).show()

            val durationMillis = findSongDuration(audioUri)

            if (durationMillis == 0) {
                durationText = "NA"
            }

            durationText = getDurationFromMilli(durationMillis)
            Log.i("Tnaaaa", "durationMillis = $durationMillis")

            imageStorageReference = storageRef.child("images/" + UUID.randomUUID().toString())
            songStorageReference = storageRef.child("songs/" + UUID.randomUUID().toString())

            imageUri?.apply {
               imageStorageReference
                    .putFile(this)
                    .addOnSuccessListener {
                        imageStorageReference.downloadUrl.addOnSuccessListener { cover ->
                            val coverUri = cover.toString()

                            Log.i("Tina", "this=$this")
                            Log.i("Tina", "coverUri=$coverUri")

                            audioUri?.apply {
                                songStorageReference.putFile(this)
                                    .addOnSuccessListener {
                                        songStorageReference
                                            .downloadUrl.addOnSuccessListener { music ->

                                            val songList = Songs(
                                                songTitle = binding.musicTitleEdit.text.toString(),
                                                songDuration = durationText,
                                                songLink = music.toString(),
                                                songId = songId,
                                                cover = coverUri,
                                                userId = UserManager.userToken
                                            )

                                            songsReference.set(songList)


                                            updatePosts(
                                                Posts(
                                                    userId = UserManager.userToken,
                                                    userName = UserManager.userName,
                                                    type = POST_TYPES.MUSIC.value,
                                                    postId = postId,
                                                    composer = binding.musicComposerEdit.text.toString(),
                                                    title = binding.musicTitleEdit.text.toString(),
                                                    description = binding.musicDescriptionEdit.text.toString(),
                                                    avatar = "",
                                                    createdTime = Calendar.getInstance().timeInMillis,
                                                    date = "20200308",
                                                    image = coverUri,
                                                    song = songList)
                                            )

                                            Toast.makeText(
                                                activity,
                                                "Upload Succeeded",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()

                                            findNavController().navigate(R.id.action_global_mainFragment)

                                        }.addOnFailureListener {
                                            Log.d("FirebaseStorage", "Failed to get userID uri")
                                        }

                                        status = LoadApiStatus.DONE

                                    }.addOnFailureListener {
                                    Toast.makeText(
                                        activity,
                                        "Song Upload Failed",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                            }

                        }.addOnFailureListener {
                            Log.d("FirebaseStorage", "Failed to get image uri")
                        }
                    }.addOnFailureListener {
                    Toast.makeText(activity, "Image Upload Failed", Toast.LENGTH_SHORT)
                        .show()
                }


            }
        }else {
            Toast.makeText(activity, "No file selected to upload", Toast.LENGTH_SHORT).show()
        }


    }

    private fun updatePosts(posts: Posts){

        postsReference.set(posts).addOnSuccessListener {
            Toast.makeText(activity, "Updated to News Feed", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(activity, "Failed to Update News Feed", Toast.LENGTH_SHORT).show()
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
            retriever.setDataSource(activity, audioUri)
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            timeInMilliSec = Integer.parseInt(time)

            retriever.release()
            return timeInMilliSec

        } catch (e: Exception) {

            e.printStackTrace()

            return -1
        }

    }

    private fun setProgressBar(){

        if(status == LoadApiStatus.LOADING){
            binding.addMusicProgressBar.visibility = View.VISIBLE
            binding.submitButton.visibility = View.INVISIBLE
        }


    }

//    private fun getFileExtension(audioUri: Uri): String? {
//
//        val contentResolver = context?.contentResolver
//        val mine = MimeTypeMap.getSingleton()
//
//        return mine.getExtensionFromMimeType(contentResolver?.getType(audioUri))
//
//    }


}