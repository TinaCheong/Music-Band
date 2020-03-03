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
import com.tina.musicband.network.LoadApiStatus
import com.tina.musicband.search.SearchMusicViewModel
import java.util.*

class AddEventFragment : Fragment() {

    val viewModel by viewModels<AddEventViewModel> { getVmFactory() }

    lateinit var binding: LayoutAddEventMainBinding
    private var imageUri: Uri? = null

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


        viewModel.uploadStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {

                Toast.makeText(activity, "Upload Succeeded", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_global_mainFragment)
                viewModel.finishUpload()

            }
        })

        setUpDatePicker()

        return binding.root
    }

    private fun openImageFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 102)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 102 && resultCode == Activity.RESULT_OK && data?.getData() != null) {

            data.getData().let {
                imageUri = it
                viewModel.setImagePath(it)
            }
            Glide.with(this).load(imageUri).centerCrop().into(binding.imageBorder)
            binding.imageUploadHint.visibility = View.GONE
            binding.addImageIcon.visibility = View.INVISIBLE
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


}

