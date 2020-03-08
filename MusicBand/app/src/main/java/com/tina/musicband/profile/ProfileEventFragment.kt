package com.tina.musicband.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tina.musicband.R
import com.tina.musicband.data.Posts
import com.tina.musicband.databinding.FragmentProfileEventBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.UserManager
import com.tina.musicband.main.MainAdapter
import com.tina.musicband.main.MainViewModel
import com.tina.musicband.main.POST_TYPES
import com.tina.musicband.main.PostSealedItem

/**
 * A simple [Fragment] subclass.
 */
class ProfileEventFragment : Fragment() {

    lateinit var binding: FragmentProfileEventBinding

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_event, container, false
        )

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.recyclerViewEventProfile.adapter = MainAdapter(viewModel)

        viewModel.retrievePostByUserID(UserManager.userToken.toString())

        viewModel.readUserDataResult(UserManager.userToken.toString())

        return binding.root
    }

}
