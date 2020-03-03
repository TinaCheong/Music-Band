package com.tina.musicband.following


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.data.Following
import com.tina.musicband.databinding.FragmentFollowerProfileBinding
import com.tina.musicband.databinding.FragmentFollowingProfileBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.UserManager

/**
 * A simple [Fragment] subclass.
 */
class FollowingProfileFragment : Fragment() {

    lateinit var binding: FragmentFollowingProfileBinding
    val viewModel by viewModels<FollowingProfileViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_following_profile, container, false
        )

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.recyclerViewFollowingPage.adapter = FollowingAdapter()


        return binding.root
    }


}
