package com.tina.musicband.search


import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tina.musicband.MainActivity
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.NavigationDirections
import com.tina.musicband.R
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.FragmentSearchMusicBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.network.LoadApiStatus
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class SearchMusicFragment : Fragment() {

    val viewModel by viewModels<SearchMusicViewModel> { getVmFactory() }
    lateinit var binding : FragmentSearchMusicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_music, container, false
        )

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.recyclerViewSearchMusicPage.adapter = SearchMusicAdapter(viewModel)


        viewModel.selectedSong.observe(this, Observer {
            it?.userId?.let {userID ->
            findNavController().navigate(NavigationDirections.actionGlobalProfileOthersFragment(userID))
            viewModel.doneNavigate()
            }
        })

        binding.searchViewBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true
            }
        })



        return binding.root

    }

    private fun search(keyword: String?){

        val resultList = mutableListOf<Songs>()
        for(song in viewModel.songs.value!!){
            if(song.songTitle.toLowerCase().contains(keyword.toString()) ||
                song.songTitle.toUpperCase().contains(keyword.toString())){

                resultList.add(song)
            }

            binding.recyclerViewSearchMusicPage.adapter = SearchMusicAdapter(viewModel)
            (binding.recyclerViewSearchMusicPage.adapter as SearchMusicAdapter).submitList(resultList)

        }

    }

}

