package com.tina.musicband.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.databinding.FragmentMainBinding
import java.sql.Timestamp

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private var isOpen = false

    lateinit var binding: FragmentMainBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        binding.lifecycleOwner = this

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

        //Change the visibility of toolbar and bottom navigation
        (activity as MainActivity).binding.toolbar.visibility = View.VISIBLE
        (activity as MainActivity).binding.bottomNavigation.visibility = View.VISIBLE
        (activity as MainActivity).binding.toolbarLogoSearch.visibility = View.GONE
        (activity as MainActivity).binding.toolbarLogo.visibility = View.VISIBLE
        (activity as MainActivity).binding.toolbarLogoProfile.visibility = View.GONE

        val mainAdapter = MainAdapter()

        binding.recyclerViewMainPage.adapter = mainAdapter

        val posts = listOf(Posts("",
            "",
            "",
            "",
            12,
            "","",
            "",
            "",
            "",
            "",
            20, Comments("")
        ), Posts("",
            "",
            "",
            "",
            24,
            "","",
            "",
            "",
            "",
            "",
            30, Comments("")))


        mainAdapter.submitList(posts)


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
