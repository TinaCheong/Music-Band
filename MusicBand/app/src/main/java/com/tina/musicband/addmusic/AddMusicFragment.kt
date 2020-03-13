package com.tina.musicband.addmusic

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tina.musicband.R
import com.tina.musicband.databinding.LayoutAddMusicMainBinding
import com.tina.musicband.ext.getVmFactory

class AddMusicFragment : Fragment() {

    lateinit var binding: LayoutAddMusicMainBinding
    private var audioUri: Uri? = null
    val viewModel by viewModels<AddMusicViewModel> { getVmFactory() }
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.layout_add_music_main, container, false)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addMusicBorder.setOnClickListener {
            openAudioFile()
        }

        binding.imageBorder.setOnClickListener {
            openImageFile()
        }

        viewModel.isMusicPostReadyToPost.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    viewModel.publishSong()
                    viewModel.publishMusicPost()
                    viewModel.donePostingMusic()
                }
            }
        })

        viewModel.isMusicPostingFinished.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    Toast.makeText(
                        activity, "Upload Success", Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_global_mainFragment)
                    viewModel.confirmMusicPostingFinished()
                }
            }
        })

        return binding.root
    }

    private fun openAudioFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("audio/*")
        startActivityForResult(intent, 101)
    }

    private fun openImageFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, 102)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && data?.getData() != null) {

            data.getData().let {
                audioUri = it
                viewModel.setSongPath(it)
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
                viewModel.setImagePath(it)
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

}