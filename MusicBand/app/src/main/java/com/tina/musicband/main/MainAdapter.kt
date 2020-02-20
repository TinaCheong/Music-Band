package com.tina.musicband.main

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.os.UserManager
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
import com.tina.musicband.MainActivity
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.R
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.ItemEventsMainBinding
import com.tina.musicband.databinding.ItemMusicMainBinding
import com.tina.musicband.ext.toDisplayFormat
import java.io.IOException
import java.lang.Exception

private val ITEM_VIEW_TYPE_MUSIC = 0
private val ITEM_VIEW_TYPE_EVENT = 1


class MainAdapter(val mainViewModel: MainViewModel) :
    ListAdapter<PostSealedItem, RecyclerView.ViewHolder>(DiffCallback) {

    class EventViewHolder(private var binding: ItemEventsMainBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(posts: Posts) {

            binding.posts = posts
            binding.usernameText.setText(com.tina.musicband.login.UserManager.userName)
            binding.eventTitle.setText(posts.title)
            binding.createdTimeText.setText(posts.createdTime.toDisplayFormat())
            binding.eventDescription.setText(posts.description)
            binding.eventDate.setText(posts.date.toString())
            binding.eventLike.setText(posts.like.toString())

            Glide
                .with(MusicBandApplication.instance.applicationContext)
                .load(posts.image)
                .centerCrop()
                .into(binding.mainImage)

//            val adapter = CommentAdapter()
//            binding.recyclerViewMainEventComment. adapter = adapter
//            adapter.submitList()

        }


    }

    class MusicViewHolder(private var binding: ItemMusicMainBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        private val mediaPlayer = MediaPlayer()
        lateinit var handler: Handler

        fun bind(posts: Posts) {

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

            binding.musicEndTime.setText(posts.song.songDuration)

            binding.usernameText.setText(com.tina.musicband.login.UserManager.userName)
            binding.musicTitle.setText(posts.title)
            binding.musicComposer.setText(posts.composer)
            binding.createdTimeText.setText(posts.createdTime.toDisplayFormat())
            binding.musicDescription.setText(posts.description)
            binding.musicLike.setText(posts.like.toString())

            Glide
                .with(MusicBandApplication.instance.applicationContext)
                .load(posts.song.cover)
                .transform(CenterCrop(), RoundedCorners(12))
                .placeholder(R.drawable.ic_cover)
                .error(R.drawable.ic_cover)
                .into(binding.musicCover)

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

            setMediaPlayer(posts.song)

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


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_EVENT -> {
                EventViewHolder(
                    ItemEventsMainBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), parent.context
                )
            }
            ITEM_VIEW_TYPE_MUSIC -> {
                MusicViewHolder(
                    ItemMusicMainBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), parent.context
                )
            }
            else -> throw ClassCastException("Unknown viewType ${viewType}")   //給一個else
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EventViewHolder -> {
                holder.bind((getItem(position) as PostSealedItem.EventItem).posts)
            }
            is MusicViewHolder -> {
                holder.bind((getItem(position) as PostSealedItem.MusicItem).posts)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PostSealedItem.EventItem -> ITEM_VIEW_TYPE_EVENT
            is PostSealedItem.MusicItem -> ITEM_VIEW_TYPE_MUSIC
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<PostSealedItem>() {
        override fun areItemsTheSame(oldItem: PostSealedItem, newItem: PostSealedItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PostSealedItem, newItem: PostSealedItem): Boolean {
            return oldItem.id == newItem.id
        }
    }


}

sealed class PostSealedItem {
    data class EventItem(val posts: Posts) : PostSealedItem() {
        override val id: String
            get() = "EVENT"
    }

    data class MusicItem(val posts: Posts) : PostSealedItem() {
        override val id: String
            get() = "MUSIC"
    }

    abstract val id: String
}