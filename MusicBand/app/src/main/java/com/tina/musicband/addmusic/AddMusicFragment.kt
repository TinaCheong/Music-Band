package com.tina.musicband.addmusic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

        return binding.root
    }

}