package com.tina.musicband.search

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.google.firebase.storage.FirebaseStorage
import com.tina.musicband.*
import com.tina.musicband.data.Songs
import com.tina.musicband.data.User
import com.tina.musicband.databinding.ItemMusicPlayerSearchBinding
import com.tina.musicband.others.ProfileOthersFragment
import kotlinx.android.synthetic.main.item_music_player_search.view.*
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import java.io.IOException
import java.lang.Exception

class SearchMusicAdapter(val searchMusicViewModel: SearchMusicViewModel) : ListAdapter<Songs, SearchMusicAdapter.SongsViewHolder>(DiffCallback) {


    class SongsViewHolder(private var binding: ItemMusicPlayerSearchBinding, private val searchMusicViewModel: SearchMusicViewModel) :
        RecyclerView.ViewHolder(binding.root) {

        private val mediaPlayer = MediaPlayer()
        lateinit var handler: Handler

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
                .centerCrop()
                .placeholder(R.drawable.ic_album_cover)
                .error(R.drawable.ic_album_cover)
                .into(binding.musicCover)

            setMediaPlayer(songs)

            binding.musicPlayButton.setOnClickListener {

                mediaPlayer.start()
                val runnable = Thread(MusicRunnable())
                runnable.start()
                binding.musicPlayButton.visibility = View.INVISIBLE
                binding.musicPauseButton.visibility = View.VISIBLE
            }

            binding.musicPauseButton.setOnClickListener {
                mediaPlayer.pause()
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
                    val percent = (currentPosition.toFloat() / 100000 * 100).toInt()
                    binding.musicSeekBar.progress = percent
                    val startTime = createTIme(currentPosition)

                    if (startTime.contains("-")) {
                        binding.musicStartTime.setText("00:00")
                    } else {
                        binding.musicStartTime.setText(startTime)

                    }


                }
            }

            binding.executePendingBindings()

        }


        private fun createTIme(time: Int): String {
            var timeLevel: String
            val min = time / 1000 / 60
            val sec = time / 1000 % 60
            timeLevel = String.format("%02d:%02d", min, sec)

//        if(sec<10){
//            timeLevel += "0"
//            timeLevel += sec
//        }

            return timeLevel

        }

        private fun setMediaPlayer(songs: Songs) {

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            fetchAudioUrlFromFirebase(songs)

        }

        private fun fetchAudioUrlFromFirebase(songs: Songs) {
            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.getReferenceFromUrl(songs.songLink)
            storageReference.downloadUrl.addOnSuccessListener {
                try {
                    mediaPlayer.setDataSource(songs.songLink)
//                    mediaPlayer.setOnPreparedListener {
//                        it.start()
//                    }
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
