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
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentSearchMusicBinding
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class SearchMusicFragment : Fragment() {

    lateinit var binding : FragmentSearchMusicBinding
    lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_music, container, false
        )

        val mediaPlayer = MediaPlayer.create(activity, R.raw.all_i_ask_of_you)

        mediaPlayer.seekTo(0)

        binding.musicPlayButton.setOnClickListener {
            mediaPlayer.start()
        }

        binding.musicPauseButton.setOnClickListener {
            mediaPlayer.pause()
        }

        val totalTime = mediaPlayer.duration

        binding.musicSeekBar.setMax(totalTime)

        binding.musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    mediaPlayer.seekTo(progress)
//                    binding.musicSeekBar.setProgress(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause()
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (mediaPlayer != null) {
                    mediaPlayer.start()
                }
            }
        })

        class MusicRunnable: Runnable {
            override fun run() {
                while(mediaPlayer != null){
                    try {
                        val message = Message()
                        message.what = mediaPlayer.getCurrentPosition()
                        handler.sendMessage(message)
                        Thread.sleep(1000)
                    }catch (e : Exception){

                    }
                }
            }
        }

        handler = @SuppressLint("HandlerLeak")
        object : Handler(){
            override fun handleMessage(msg: Message) {
                val currentPosition = msg.what
                binding.musicSeekBar.setProgress(currentPosition)

                val startTime = createTIme(currentPosition)
                binding.musicStartTime.setText(startTime)

                val endTime = createTIme(totalTime - currentPosition)
                binding.musicEndTime.setText(endTime)

            }
        }

        val runnable = Thread(MusicRunnable())
        runnable.start()

        return binding.root

    }

    private fun createTIme(time: Int) : String{
        var timeLevel : String
        val min = time/1000/60
        val sec = time/1000%60
        timeLevel = String.format("%02d:%02d", min, sec)

//        if(sec<10){
//            timeLevel += "0"
//            timeLevel += sec
//        }

        return timeLevel

    }


}
