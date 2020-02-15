package com.tina.musicband.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.tina.musicband.R
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.FragmentProfileEventBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.main.MainAdapter
import com.tina.musicband.main.MainViewModel

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

//        val adapter = MainAdapter(viewModel)
//
//        binding.recyclerViewEventProfile.adapter = adapter
//
////        val posts = listOf(
//            Posts("",
//                "",
//                "",
//                "",
//                "",
//                "",
//                123,
//                "",
//                "",
//                "",
//                "",
//                "",
//                1,
//                song = Songs("","","","",""),
//                comments = Comments("")
//            ), Posts("",
//                "",
//                "",
//                "",
//                "",
//                "",
//                124,
//                "",
//                "",
//                "",
//                "",
//                "",
//                1,
//                song = Songs("","","","",""),
//                comments = Comments("")
//            )
//        )
//
//
//        adapter.submitList(posts)

        // Inflate the layout for this fragment
        return binding.root
    }


}
