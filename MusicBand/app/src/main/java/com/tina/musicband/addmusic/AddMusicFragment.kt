package com.tina.musicband.addmusic

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.opengl.Visibility
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.tina.musicband.MainActivity
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.R
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.LayoutAddMusicMainBinding
import java.lang.Exception
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class AddMusicFragment : Fragment() {

    lateinit var binding: LayoutAddMusicMainBinding
    private var audioUri: Uri? = null
    var mStorageRef: StorageReference = FirebaseStorage.getInstance().getReference("songs")
    lateinit var storageReference: StorageReference

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

        binding.addMusicIcon.setOnClickListener {
            openAudioFile()
        }

        binding.submitButton.setOnClickListener {
            uploadAudioToFirebase()
        }


        return binding.root
    }

    fun openAudioFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("audio/*")
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && data?.getData() != null) {

            data.getData().let {
                audioUri = it
            }
            val fileName = getFileName(audioUri)
            binding.musicUploadHint.setText(fileName)
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

    fun uploadAudioToFirebase() {

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
                storageReference = mStorageRef.child(
                    System.currentTimeMillis().toString().plus(".").plus(getFileExtension(it))
                )
            }

            val durationMillis = findSongDuration(audioUri)

            if (durationMillis == 0) {
                durationText = "NA"
            }

            durationText = getDurationFromMilli(durationMillis)
            Log.i("Tnaaaa", "durationMillis = $durationMillis")

            audioUri?.let {
                val uploadTask = mStorageRef.putFile(it).addOnSuccessListener {
                    val songUri = it.metadata?.reference?.downloadUrl
                    songUri?.addOnSuccessListener {
                        val songsReference =
                            FirebaseFirestore.getInstance().collection("songs")
                        val song = Songs(
                            songTitle = binding.musicTitleEdit.text.toString(),
                            songDuration = durationText,
                            songLink = it.toString(),
                            songId = songsReference.document().id
                        )
//                        val uploadId = songsReference.id
//                        val uploadInfo = hashMapOf("uploadId" to uploadId)
                        songsReference.document().set(song)

                        Toast.makeText(activity, "Upload Succeeded", Toast.LENGTH_SHORT)
                            .show()


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