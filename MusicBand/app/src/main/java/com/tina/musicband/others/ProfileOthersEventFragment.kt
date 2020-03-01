package com.tina.musicband.others


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentProfileOthersEventBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.main.MainAdapter
import com.tina.musicband.main.MainViewModel

/**
 * A simple [Fragment] subclass.
 */
class ProfileOthersEventFragment(private val userID : String): Fragment() {

    lateinit var binding : FragmentProfileOthersEventBinding
    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_others_event, container, false
        )

        binding.lifecycleOwner = this

        binding.recyclerViewEventOthers.adapter = MainAdapter(viewModel)

        viewModel.fetchPostByUserID(userID)

        viewModel.getProfileAvatar(userID)

        viewModel.userPosts.observe(viewLifecycleOwner, Observer {

            (binding.recyclerViewEventOthers.adapter as? MainAdapter)?.submitList(it)
            showHint()

        })


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showHint(){

        if (viewModel.userPosts.value?.size == 0) {

            binding.noPostImage.visibility = View.VISIBLE
            binding.noPostText.visibility = View.VISIBLE

        } else {

            binding.noPostImage.visibility = View.GONE
            binding.noPostText.visibility = View.GONE

        }


    }

}
