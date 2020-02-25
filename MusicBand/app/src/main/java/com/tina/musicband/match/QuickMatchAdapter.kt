package com.tina.musicband.match

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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.R
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.avatar.getDrawable
import com.tina.musicband.data.Matching
import com.tina.musicband.data.Songs
import com.tina.musicband.data.User
import com.tina.musicband.databinding.ItemUserMatchBinding
import com.tina.musicband.login.UserManager
import java.io.IOException
import java.lang.Exception

class QuickMatchAdapter : ListAdapter<User, QuickMatchAdapter.EventViewHolder>(DiffCallback) {


    class EventViewHolder(private var binding: ItemUserMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val mediaPlayer = MediaPlayer()
        lateinit var handler: Handler

        fun bind(user: User) {

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

            binding.username.text = user.username
            binding.userAvatar.setImageDrawable(

                user.avatar.getAvatarDrawable()

                )


            FirebaseFirestore.getInstance().collection("songs")
                .whereEqualTo("userId", user.userId)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    if (querySnapshot != null) {
                        var songList = querySnapshot.toObjects(Songs::class.java)

                        for (song in songList) {

                            binding.musicTitleText.setText(song.songTitle)
                            binding.musicEndTime.setText(song.songDuration)
                            Glide
                                .with(MusicBandApplication.instance.applicationContext)
                                .load(song.cover)
                                .transform(CenterCrop(), RoundedCorners(12))
                                .placeholder(R.drawable.ic_cover)
                                .error(R.drawable.ic_cover)
                                .into(binding.musicCover)

                            setMediaPlayer(song)
                        }


                    }

                }



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

            binding.followButton.setOnClickListener {
                FirebaseFirestore.getInstance().collection("users").document(UserManager.userToken.toString())
                    .collection("following").document(user.userId)
                    .set(mapOf("userId" to user.userId))
                    .addOnSuccessListener {

                        FirebaseFirestore.getInstance().collection("users").document(user.userId)
                            .collection("follower").document(UserManager.userToken.toString())
                            .set(mapOf("userId" to UserManager.userToken.toString()))

                        binding.followButton.visibility = View.INVISIBLE
                        binding.followClickedButton.visibility = View.VISIBLE
                    }


                binding.followClickedButton.setOnClickListener {
                    FirebaseFirestore.getInstance().collection("posts").document(UserManager.userToken.toString())
                        .collection("following").document(user.userId).delete()
                        .addOnSuccessListener {

                            FirebaseFirestore.getInstance().collection("users").document(user.userId)
                                .collection("follower").document(UserManager.userToken.toString())
                                .delete()

                            binding.followButton.visibility = View.VISIBLE
                            binding.followClickedButton.visibility = View.INVISIBLE

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
            }
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

        private var mySong = Songs()

            private fun setMediaPlayer(songs: Songs) {

                if (songs.songId == mySong.songId) return

                mySong = songs

                mediaPlayer.stop()

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


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            return EventViewHolder(
                ItemUserMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }


        override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
            holder.bind(getItem(position))
        }


        companion object DiffCallback : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.userId == newItem.userId
            }
        }


    }
