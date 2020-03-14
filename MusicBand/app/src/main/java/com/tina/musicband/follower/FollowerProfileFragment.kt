package com.tina.musicband.follower


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentFollowerProfileBinding
import com.tina.musicband.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class FollowerProfileFragment : Fragment() {

    lateinit var binding: FragmentFollowerProfileBinding
    val viewModel by viewModels<FollowerProfileViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_follower_profile, container, false
        )

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.recyclerViewFollowerPage.adapter = FollowerAdapter(viewModel)

        return binding.root
    }

}
