package com.tina.musicband.search

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.storage.FirebaseStorage
import com.tina.musicband.*
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.ItemMusicPlayerSearchBinding
import com.tina.musicband.ext.createTime
import java.io.IOException
import java.lang.Exception
import kotlin.math.roundToInt

class SearchMusicAdapter(val searchMusicViewModel: SearchMusicViewModel) : ListAdapter<Songs, SearchMusicAdapter.SongsViewHolder>(DiffCallback) {



    class SongsViewHolder(private var binding: ItemMusicPlayerSearchBinding, private val searchMusicViewModel: SearchMusicViewModel) :
        RecyclerView.ViewHolder(binding.root) {

        private val mediaPlayer = MediaPlayer()
        lateinit var handler: Handler
        var loadedSong = ""

        fun bind(songs: Songs) {

            class MusicRunnable : Runnable {
                override fun run() {
                    while (mediaPlayer != null) {
                        try {
                            val message = Message()
                            message.what = mediaPlayer.getCurrentPosition()
                            handler.sendMessage(message)
                            Thread.sleep(1000)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            binding.musicTitleText.setText(songs.songTitle)

            binding.musicEndTime.setText(songs.songDuration)

            Glide.with(MusicBandApplication.instance.applicationContext)
                .load(songs.cover)
                .transform(CenterCrop(),RoundedCorners(12))
                .placeholder(R.drawable.ic_cover)
                .error(R.drawable.ic_cover)
                .into(binding.musicCover)



            binding.musicPlayButton.setOnClickListener {
//                mediaPlayer.start()
                if (songs.songId == loadedSong) {

                    mediaPlayer.start()

                } else {

                    loadedSong = songs.songId
                    prepareMediaPlayer(songs)
                    mediaPlayer.setOnPreparedListener {
                        it.start()
                    }

                    val runnable = Thread(MusicRunnable())
                    runnable.start()

                }


                binding.musicPlayButton.visibility = View.INVISIBLE
                binding.musicPauseButton.visibility = View.VISIBLE
            }


            binding.musicPauseButton.setOnClickListener {
                mediaPlayer.pause()
//                mediaPlayer.stop()
//                mediaPlayer.reset()
//                mediaPlayer.release()
                binding.musicPlayButton.visibility = View.VISIBLE
                binding.musicPauseButton.visibility = View.INVISIBLE
                }



            mediaPlayer.seekTo(0)


            binding.musicSeekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress)
//                        binding.musicSeekBar.setProgress(progress)
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




            handler = @SuppressLint("HandlerLeak")
            object : Handler() {
                override fun handleMessage(msg: Message) {
                    val currentPosition = msg.what
                    val percent = (1 / ( mediaPlayer.duration / 100.0) * currentPosition).roundToInt()
                    binding.musicSeekBar.progress = percent
                    val startTime = currentPosition.createTime()

                    if (startTime.contains("-")) {
                        binding.musicStartTime.setText("00:00")
                    } else {
                        binding.musicStartTime.setText(startTime)

                    }


                }
            }

            binding.executePendingBindings()

        }


        fun createTime(time: Int): String {
            var timeLevel: String
            val min = time / 1000 / 60
            val sec = time / 1000 % 60
            timeLevel = String.format("%02d:%02d", min, sec)

            return timeLevel

        }


        private fun prepareMediaPlayer(songs: Songs) {

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            fetchAudioUrlFromFirebase(songs)

        }

        private fun fetchAudioUrlFromFirebase(songs: Songs) {
            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.getReferenceFromUrl(songs.songLink)
            storageReference.downloadUrl.addOnSuccessListener {
                try {
                    mediaPlayer.setDataSource(songs.songLink)
                    mediaPlayer.prepareAsync()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Songs>() {
        override fun areItemsTheSame(oldItem: Songs, newItem: Songs): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Songs, newItem: Songs): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        return SongsViewHolder(
            ItemMusicPlayerSearchBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), searchMusicViewModel
        )
    }


    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            searchMusicViewModel.selectSong(getItem(position))
        }
    }

    
}
