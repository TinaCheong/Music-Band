package com.tina.musicband.others


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
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

        binding.viewModel = viewModel

        binding.recyclerViewEventOthers.adapter = MainAdapter(viewModel)

        viewModel.retrievePostByUserID(userID)

        viewModel.readUserDataResult(userID)


        return binding.root
    }


}
