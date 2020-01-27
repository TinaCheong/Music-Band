package com.tina.musicband.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    var isOpen = false

    lateinit var binding: FragmentMainBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        // FAB Animation Settings
        binding.mainFab.setOnClickListener {
            fabAnimation()
        }

        return binding.root
    }

    // FAB Animation Settings

    fun fabAnimation() {

        val fabOpen = AnimationUtils.loadAnimation(activity, R.anim.fab_open)
        val fabClose = AnimationUtils.loadAnimation(activity, R.anim.fab_close)
        val fabRotateForward = AnimationUtils.loadAnimation(activity, R.anim.fab_rotate_forward)
        val fabRotateBackward = AnimationUtils.loadAnimation(activity, R.anim.fab_rotate_backward)

        if (isOpen) {

            binding.mainFab.startAnimation(fabRotateForward)
            binding.musicFab.startAnimation(fabClose)
            binding.eventFab.startAnimation(fabClose)
            binding.musicFab.setClickable(false)
            binding.eventFab.setClickable(false)

            isOpen = false

        } else {

            binding.mainFab.startAnimation(fabRotateBackward)
            binding.musicFab.startAnimation(fabOpen)
            binding.eventFab.startAnimation(fabOpen)
            binding.musicFab.setClickable(true)
            binding.eventFab.setClickable(true)

            isOpen = true
        }

    }


}
