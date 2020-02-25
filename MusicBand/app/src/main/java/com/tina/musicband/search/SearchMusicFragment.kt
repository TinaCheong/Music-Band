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
//    lateinit var handler: Handler
    private  val songsList = mutableListOf<Songs>()
    private var status = LoadApiStatus.LOADING


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_music, container, false
        )

        binding.lifecycleOwner = this

//        val mediaPlayer = MediaPlayer.create(activity, R.raw.all_i_ask_of_you)
//
//        mediaPlayer.seekTo(0)
//
//        binding.musicPlayButton.setOnClickListener {
//            mediaPlayer.start()
//        }
//
//        binding.musicPauseButton.setOnClickListener {
//            mediaPlayer.pause()
//        }
//
//        val totalTime = mediaPlayer.duration
//
//        binding.musicSeekBar.setMax(totalTime)
//
//        binding.musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                if(fromUser) {
//                    mediaPlayer.seekTo(progress)
////                    binding.musicSeekBar.setProgress(progress)
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                if (mediaPlayer != null) {
//                    mediaPlayer.pause()
//                }
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                if (mediaPlayer != null) {
//                    mediaPlayer.start()
//                }
//            }
//        })
//
//        class MusicRunnable: Runnable {
//            override fun run() {
//                while(mediaPlayer != null){
//                    try {
//                        val message = Message()
//                        message.what = mediaPlayer.getCurrentPosition()
//                        handler.sendMessage(message)
//                        Thread.sleep(1000)
//                    }catch (e : Exception){
//
//                    }
//                }
//            }
//        }
//
//        handler = @SuppressLint("HandlerLeak")
//        object : Handler(){
//            override fun handleMessage(msg: Message) {
//                val currentPosition = msg.what
//                binding.musicSeekBar.setProgress(currentPosition)
//
//                val startTime = createTIme(currentPosition)
//                binding.musicStartTime.setText(startTime)
//
//                val endTime = createTIme(totalTime - currentPosition)
//                binding.musicEndTime.setText(endTime)
//
//            }
//        }
//
//        val runnable = Thread(MusicRunnable())
//        runnable.start()

        val adapter = SearchMusicAdapter(viewModel)
        binding.recyclerViewSearchMusicPage.adapter = adapter

        binding.searchPageProgressBar.visibility = View.VISIBLE

        FirebaseFirestore.getInstance().collection("songs")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){

                    status == LoadApiStatus.DONE
                    binding.searchPageProgressBar.visibility = View.GONE

                    for (document in it.result!!){

                        val songs = document.toObject(Songs::class.java)
                        songsList.add(songs)

                    }
                    showHint()
                    adapter.submitList(songsList)
                }
            }

        viewModel.selectedSong.observe(this, Observer {
            it?.let {
            findNavController().navigate(NavigationDirections.actionGlobalProfileOthersFragment(it))
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
        for(song in songsList){
            if(song.songTitle.toLowerCase().contains(keyword.toString())){

                resultList.add(song)

            }

            val adapter = SearchMusicAdapter(viewModel)
            binding.recyclerViewSearchMusicPage.adapter = adapter
            adapter.submitList(resultList)

        }

    }

    private fun showHint(){
        if(status == LoadApiStatus.DONE && songsList.size == 0){

            binding.noMusicImage.visibility = View.VISIBLE
            binding.questionMarkImage.visibility = View.VISIBLE
            binding.noMusicText.visibility = View.VISIBLE

        }else{

            binding.noMusicImage.visibility = View.GONE
            binding.questionMarkImage.visibility = View.GONE
            binding.noMusicText.visibility = View.GONE


        }
    }




}
