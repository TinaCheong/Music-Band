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
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.LayoutAddMusicMainBinding
import java.lang.Exception
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class AddMusicFragment : Fragment() {

    lateinit var binding: LayoutAddMusicMainBinding
    private var audioUri: Uri? = null
    private var musicStorageRef: StorageReference =
        FirebaseStorage.getInstance().getReference("songs")
    lateinit var storageReference: StorageReference
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

        //Change the visibility of toolbar and bottom navigation
        (activity as MainActivity).binding.toolbar.visibility = View.GONE
        (activity as MainActivity).binding.bottomNavigation.visibility = View.GONE

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

        if (binding.musicUploadHint.toString().equals("No file selected")) {
            Toast.makeText(
                activity, "Please select a song", Toast.LENGTH_SHORT
            ).show()
        } else {
            uploadFile()
        }
    }

    private fun uploadFile() {

        var durationText = ""

        if (audioUri != null) {

            Toast.makeText(
                activity, "Uploading please wait...", Toast.LENGTH_SHORT
            ).show()

            audioUri?.let {
                storageReference = musicStorageRef.child(
                    System.currentTimeMillis().toString().plus(".").plus(getFileExtension(it))
                )
            }

            val durationMillis = findSongDuration(audioUri)

            if (durationMillis == 0) {
                durationText = "NA"
            }

            durationText = getDurationFromMilli(durationMillis)
            Log.i("Tnaaaa", "durationMillis = $durationMillis")


            imageUri?.apply {
                FirebaseStorage.getInstance().getReference("image").putFile(this).addOnSuccessListener {
                    FirebaseStorage.getInstance().getReference("image").downloadUrl.addOnSuccessListener { cover->
                        val coverUri = cover.toString()

                        Log.i("Tina", "this=$this")
                        Log.i("Tina", "coverUri=$coverUri")

                        audioUri?.apply {
                            FirebaseStorage.getInstance().getReference("sone").putFile(this).addOnSuccessListener {
                                FirebaseStorage.getInstance().getReference("sone").downloadUrl.addOnSuccessListener { music ->
                                    val songUri = music.toString()

                                    Log.d("Tina", "this=$this")
                                    Log.d("Tina", "songUri=$songUri")

                                    val songsReference =
                                        FirebaseFirestore.getInstance().collection("songs")
                                    val song = Songs(
                                        songTitle = binding.musicTitleEdit.text.toString(),
                                        songDuration = durationText,
                                        songLink = music.toString(),
                                        songId = "123",
                                        cover = coverUri
                                    )

                                    songsReference.document().set(song)

                                    Toast.makeText(activity, "Upload Succeeded", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            }

                        }

                    }
                }
            }


        } else {
            Toast.makeText(activity, "No file selected to upload", Toast.LENGTH_SHORT).show()
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

    private fun getFileExtension(audioUri: Uri): String? {

        val contentResolver = context?.contentResolver
        val mine = MimeTypeMap.getSingleton()

        return mine.getExtensionFromMimeType(contentResolver?.getType(audioUri))

    }


}