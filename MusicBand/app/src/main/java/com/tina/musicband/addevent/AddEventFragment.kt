package com.tina.musicband.addevent

import android.app.Activity
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.common.io.Files.getFileExtension
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.LayoutAddEventMainBinding

class AddEventFragment : Fragment() {

    lateinit var binding: LayoutAddEventMainBinding
    private var imageUri: Uri? = null
    var mStorageRef: StorageReference = FirebaseStorage.getInstance().getReference("posts")
    lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.layout_add_event_main, container, false)

        (activity as MainActivity).binding.toolbar.visibility = View.GONE
        (activity as MainActivity).binding.bottomNavigation.visibility = View.GONE

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_mainFragment)
        }

        binding.imageBorder.setOnClickListener {
            openImageFile()
        }

        binding.submitButton.setOnClickListener {
            uploadImageToFirebase()
        }



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

            imageUri?.let {
                storageReference = mStorageRef.child(
                    System.currentTimeMillis().toString().plus(".").plus(getFileExtension(it))
                )
            }

            imageUri?.let {
                mStorageRef.putFile(it).addOnSuccessListener {
                    val uri = it.metadata?.reference?.downloadUrl
                    uri?.addOnSuccessListener {
                        val imageReference =
                            FirebaseFirestore.getInstance().collection("posts")
                        val post = Posts(
                            userId = "",
                            userName = "Anna Tin",
                            comments = Comments(""),
                            type = "music",
                            postId = "",
                            composer = "",
                            title = binding.eventTitleText.text.toString(),
                            description = binding.eventDescriptionText.text.toString(),
                            musicLink = "",
                            createdTime = 20200226,
                            date = "20200308",
                            image = it.toString(),
                            like = 26)
                       imageReference.document().set(post)

                        Toast.makeText(activity, "Upload Succeeded", Toast.LENGTH_SHORT)
                            .show()


                    }

                }


            }

        } else {
            Toast.makeText(activity, "No file selected to upload", Toast.LENGTH_SHORT).show()
        }


    }



    private fun getFileExtension(audioUri: Uri): String? {

        val contentResolver = context?.contentResolver
        val mine = MimeTypeMap.getSingleton()
        return mine.getExtensionFromMimeType(contentResolver?.getType(audioUri))

    }


}