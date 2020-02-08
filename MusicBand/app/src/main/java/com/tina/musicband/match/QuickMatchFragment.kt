package com.tina.musicband.match


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.data.Matching
import com.tina.musicband.databinding.FragmentQuickMatchBinding

/**
 * A simple [Fragment] subclass.
 */
class QuickMatchFragment : Fragment() {

    lateinit var binding: FragmentQuickMatchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quick_match, container, false
        )

        (activity as MainActivity).binding.toolbarLogoSearch.visibility = View.GONE
        (activity as MainActivity).binding.toolbarLogo.visibility = View.GONE
        (activity as MainActivity).binding.toolbarLogoProfile.visibility = View.GONE
        (activity as MainActivity).binding.toolbarLogoMatch.visibility = View.VISIBLE


        val adapter = QuickMatchAdapter()

        binding.recyclerViewQuickMatch.adapter = adapter

        val list = listOf(
            Matching("","",""),
            Matching("","","1"),
            Matching("","","2"),
            Matching("","","3"),
            Matching("","","4"))

        adapter.submitList(list)

        // Inflate the layout for this fragment
        return binding.root
    }


}
