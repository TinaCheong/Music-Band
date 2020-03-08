package com.tina.musicband.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.FragmentProfileMusicBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.UserManager
import com.tina.musicband.search.SearchMusicAdapter
import com.tina.musicband.search.SearchMusicViewModel

/**
 * A simple [Fragment] subclass.
 */
class ProfileMusicFragment : Fragment() {

    val viewModel by viewModels<SearchMusicViewModel> { getVmFactory() }
    lateinit var binding: FragmentProfileMusicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_music, container, false
        )

        binding.recyclerViewMusicProfile.adapter = SearchMusicAdapter(viewModel)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        viewModel.retrieveSongsByUserID(UserManager.userToken.toString())

        return binding.root
    }

}
