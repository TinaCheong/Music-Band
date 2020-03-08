package com.tina.musicband.main


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentMainBinding
import com.tina.musicband.ext.getVmFactory

enum class POST_TYPES(val value: String) {
    MUSIC("music"),
    EVENT("event")
}

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private var isOpen = false
    lateinit var binding: FragmentMainBinding
    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.prepareSnapshotListener()

        // Implement the FAB Animation
        binding.mainFab.setOnClickListener {
            fabAnimation()
        }

        // Navigate to the Add Music Fragment
        binding.musicFab.setOnClickListener {
            findNavController().navigate(R.id.action_global_addMusicFragment)
        }

        binding.eventFab.setOnClickListener {
            findNavController().navigate(R.id.action_global_addEventFragment)
        }

        binding.recyclerViewMainPage.adapter = MainAdapter(viewModel)


        viewModel.commented.observe(this, Observer {
            it?.let {
                binding.mainFab.visibility = View.GONE
                viewModel.doneCommented()
            }
        })

        viewModel.setFab.observe(this, Observer {
            it?.let {
                binding.mainFab.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

    // FAB Animation Settings

    private fun fabAnimation() {

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
