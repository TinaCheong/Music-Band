package com.tina.musicband.addevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.databinding.LayoutAddEventMainBinding

class AddEventFragment : Fragment() {

    lateinit var binding: LayoutAddEventMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.layout_add_event_main, container, false)

        (activity as MainActivity).binding.toolbar.visibility = View.GONE
        (activity as MainActivity).binding.bottomNavigation.visibility = View.GONE

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_mainFragment)
        }


        return binding.root
    }
}