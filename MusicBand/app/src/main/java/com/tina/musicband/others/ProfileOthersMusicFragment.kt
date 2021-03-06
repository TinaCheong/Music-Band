package com.tina.musicband.others


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentProfileOthersMusicBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.search.SearchMusicAdapter
import com.tina.musicband.search.SearchMusicViewModel

/**
 * A simple [Fragment] subclass.
 */
class ProfileOthersMusicFragment(private val userID : String) : Fragment() {

    lateinit var binding : FragmentProfileOthersMusicBinding
    val viewModel by viewModels<SearchMusicViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_others_music, container, false
        )

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.recyclerViewMusicOthers.adapter = SearchMusicAdapter(viewModel)

        viewModel.retrieveSongsByUserID(userID)

        return binding.root
    }


}
