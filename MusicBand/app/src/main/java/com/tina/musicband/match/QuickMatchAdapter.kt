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
import android.widget.Toast
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
import com.tina.musicband.data.Following
import com.tina.musicband.data.Songs
import com.tina.musicband.data.User
import com.tina.musicband.databinding.ItemUserMatchBinding
import com.tina.musicband.login.UserManager
import java.io.IOException
import java.lang.Exception
import kotlin.math.roundToInt

class QuickMatchAdapter : ListAdapter<User, QuickMatchAdapter.EventViewHolder>(DiffCallback) {


    class EventViewHolder(private var binding: ItemUserMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val mediaPlayer = MediaPlayer()
        lateinit var handler: Handler
        var loadedSong: Boolean? = null
        var userUploadSong = Songs()

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

            checkFollowing(user)

            getUserSong(user)

            binding.musicPlayButton.setOnClickListener {
                if (loadedSong != null) {

                    mediaPlayer.start()

                } else {

                    loadedSong = true
                    prepareMediaPlayer(userUploadSong)
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
                binding.musicPlayButton.visibility = View.VISIBLE
                binding.musicPauseButton.visibility = View.INVISIBLE
            }

            mediaPlayer.seekTo(0)

            binding.followButton.setOnClickListener {
                followUser(user)
            }


            binding.followClickedButton.setOnClickListener {
                unfollowUser(user)
            }


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


        private fun prepareMediaPlayer(songs: Songs) {

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            fetchAudioUrlFromFirebase(songs)

        }

        private fun fetchAudioUrlFromFirebase(songs: Songs) {

            if (songs.songLink.isEmpty()) return
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

        private fun checkFollowing(user: User) {

            FirebaseFirestore.getInstance().collection("users")
                .document(UserManager.userToken.toString())
                .collection("following")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        val following = it.result!!.toObjects(Following::class.java)

                        for (document in following) {

                            if (document.userId == user.userId) {

                                binding.followClickedButton.visibility = View.VISIBLE
                                break

                            }


                        }

                    }
                }


        }

        private fun getUserSong(user: User) {

            FirebaseFirestore.getInstance().collection("songs")
                .whereEqualTo("userId", user.userId)
                .limit(1)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        val song = it.result!!.toObjects(Songs::class.java)

                        if(song.size != 0) {
                            binding.musicTitleText.setText(song[0].songTitle)
                            binding.musicEndTime.setText(song[0].songDuration)
                            Glide
                                .with(MusicBandApplication.instance.applicationContext)
                                .load(song[0].cover)
                                .transform(CenterCrop(), RoundedCorners(12))
                                .placeholder(R.drawable.ic_cover)
                                .error(R.drawable.ic_cover)
                                .into(binding.musicCover)

                            userUploadSong = song[0]
                        }

                    }
                }


        }

        private fun followUser(user: User) {

            FirebaseFirestore.getInstance().collection("users")
                .document(UserManager.userToken.toString())
                .collection("following").document(user.userId)
                .set(mapOf("userId" to user.userId))
                .addOnSuccessListener {

                    FirebaseFirestore.getInstance().collection("users").document(user.userId)
                        .collection("follower").document(UserManager.userToken.toString())
                        .set(mapOf("userId" to UserManager.userToken.toString()))
                        .addOnCompleteListener {

                            if (it.isSuccessful) {

                                Toast.makeText(
                                    MusicBandApplication.instance.applicationContext,
                                    "Follow Success",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }


                    binding.followButton.visibility = View.INVISIBLE
                    binding.followClickedButton.visibility = View.VISIBLE
                }


        }

        private fun unfollowUser(user: User) {

            FirebaseFirestore.getInstance().collection("posts")
                .document(UserManager.userToken.toString())
                .collection("following").document(user.userId).delete()
                .addOnSuccessListener {

                    FirebaseFirestore.getInstance().collection("users")
                        .document(user.userId)
                        .collection("follower").document(UserManager.userToken.toString())
                        .delete()
                        .addOnCompleteListener {

                            if (it.isSuccessful) {

                                Toast.makeText(
                                    MusicBandApplication.instance.applicationContext,
                                    "Unfollow user",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }


                    binding.followButton.visibility = View.VISIBLE
                    binding.followClickedButton.visibility = View.INVISIBLE

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
