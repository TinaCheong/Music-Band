package com.tina.musicband.addmusic

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.databinding.LayoutAddMusicMainBinding

class AddMusicFragment : Fragment(){

    lateinit var binding : LayoutAddMusicMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.layout_add_music_main, container, false)

        binding.lifecycleOwner = this

        //Navigate to the Main Page
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        //Change the visibility of toolbar and bottom navigation
        (activity as MainActivity).binding.toolbar.visibility = View.GONE
        (activity as MainActivity).binding.bottomNavigation.visibility = View.GONE

        return binding.root
    }

}