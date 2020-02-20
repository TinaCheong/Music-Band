package com.tina.musicband.addevent

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.common.io.Files.getFileExtension
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tina.musicband.MainActivity
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.R
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.LayoutAddEventMainBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.UserManager
import com.tina.musicband.main.POST_TYPES
import com.tina.musicband.search.SearchMusicViewModel
import java.util.*

class AddEventFragment : Fragment() {

    val viewModel by viewModels<AddEventViewModel> { getVmFactory() }

    lateinit var binding: LayoutAddEventMainBinding
    private var imageUri: Uri? = null
    var mStorageRef: StorageReference = FirebaseStorage.getInstance().reference
    lateinit var storageReference: StorageReference
    private val postId = FirebaseFirestore.getInstance().collection("posts").document().id
    private val imageReference = FirebaseFirestore.getInstance().collection("posts").document(postId)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.layout_add_event_main, container, false)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_mainFragment)
        }

        binding.imageBorder.setOnClickListener {
            openImageFile()
        }

        binding.submitButton.setOnClickListener {
            uploadImageToFirebase()
        }


        setUpDatePicker()

        return binding.root
    }

    fun openImageFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, 102)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 102 && resultCode == Activity.RESULT_OK && data?.getData() != null) {

            data.getData().let {
                imageUri = it
            }
            Glide.with(this).load(imageUri).centerCrop().into(binding.imageBorder)
            binding.imageUploadHint.visibility = View.GONE
            binding.addImageIcon.visibility = View.INVISIBLE
        }
    }

    fun uploadImageToFirebase() {

        if (binding.imageUploadHint.toString().equals("No file selected")) {
            Toast.makeText(
                activity, "Please select an image", Toast.LENGTH_SHORT
            ).show()
        } else {
            uploadFile()
        }
    }

    private fun uploadFile(){

        if (imageUri != null) {

            Toast.makeText(
                activity, "Uploading please wait...", Toast.LENGTH_SHORT
            ).show()


            storageReference = mStorageRef.child("images/" + UUID.randomUUID().toString())


            imageUri?.let {
                storageReference.putFile(it).addOnSuccessListener {
                    val uri = it.metadata?.reference?.downloadUrl
                    uri?.addOnSuccessListener {
                        val postList = Posts(
                            userId = UserManager.userToken,
                            userName = UserManager.userName,
                            type = POST_TYPES.EVENT.value,
                            postId = postId,
                            composer = "",
                            title = binding.eventTitleText.text.toString(),
                            description = binding.eventDescriptionText.text.toString(),
                            avatar = "",
                            createdTime = Calendar.getInstance().timeInMillis,
                            date = viewModel.selectedDate.value,
                            image = it.toString(),
                            like = 0,
                            eventId = "",
                            song = Songs("","","","","")
                        )
                       imageReference.set(postList)

                        Toast.makeText(activity, "Upload Succeeded", Toast.LENGTH_SHORT)
                            .show()


                    }

                }


            }

        } else {
            Toast.makeText(activity, "No file selected to upload", Toast.LENGTH_SHORT).show()
        }


    }

    private fun setUpDatePicker(){
        val calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener{ _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            viewModel.setEventTime(calendar.time)
        }

        binding.eventTimeText.setOnClickListener {
            context?.let {
            DatePickerDialog(it, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }



//    private fun getFileExtension(audioUri: Uri): String? {
//
//        val contentResolver = context?.contentResolver
//        val mine = MimeTypeMap.getSingleton()
//        return mine.getExtensionFromMimeType(contentResolver?.getType(audioUri))
//
//    }


}