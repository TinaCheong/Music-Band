package com.tina.musicband.main

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.util.Log
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
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.R
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.data.*
import com.tina.musicband.databinding.ItemEventsMainBinding
import com.tina.musicband.databinding.ItemMusicMainBinding
import com.tina.musicband.ext.toDisplayFormat
import com.tina.musicband.login.UserManager
import com.tina.musicband.util.Logger
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.math.roundToInt

private val ITEM_VIEW_TYPE_MUSIC = 0
private val ITEM_VIEW_TYPE_EVENT = 1


class MainAdapter(private val mainViewModel: MainViewModel) :
    ListAdapter<PostSealedItem, RecyclerView.ViewHolder>(DiffCallback) {

    class EventViewHolder(private var binding: ItemEventsMainBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        val adapter = CommentAdapter()

        fun bind(posts: Posts, mainViewModel: MainViewModel) {

            Logger.i("POST_USER_NAME:${posts.userName}")

            binding.posts = posts
            binding.mainViewModel = mainViewModel

            binding.usernameText.setText(posts.userName)
            binding.eventTitle.setText(posts.title)
            binding.createdTimeText.setText(posts.createdTime.toDisplayFormat())
            binding.eventDescription.setText(posts.description)
            binding.eventDate.setText(posts.date.toString())


            Glide
                .with(MusicBandApplication.instance.applicationContext)
                .load(posts.image)
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(binding.mainImage)

            getEventComment(posts)
            checkEventLike(posts)

            binding.comment.setOnClickListener {
                binding.commentBlock.visibility = View.VISIBLE
                mainViewModel.leaveComment()
                mainViewModel.hideFab()

            }

            binding.sendButton.setOnClickListener {
                postEventComment(posts)
                binding.commentBlock.visibility = View.GONE
                binding.commentField.text = null
                mainViewModel.showFab()
            }

            binding.eventLikeIcon.setOnClickListener {
                FirebaseFirestore.getInstance().collection("posts").document(posts.postId)
                    .collection("like").document(posts.userId!!)
                    .set(mapOf("userId" to UserManager.userToken.toString()))
                    .addOnSuccessListener {

                        binding.eventLikeIcon.visibility = View.INVISIBLE
                        binding.eventLikeClickedIcon.visibility = View.VISIBLE
                    }
            }

            binding.eventLikeClickedIcon.setOnClickListener {
                FirebaseFirestore.getInstance().collection("posts").document(posts.postId)
                    .collection("like").document(UserManager.userToken.toString()).delete()
                    .addOnSuccessListener {

                        binding.eventLikeIcon.visibility = View.VISIBLE
                        binding.eventLikeClickedIcon.visibility = View.INVISIBLE

                    }
            }

            FirebaseFirestore.getInstance().collection("posts").document(posts.postId)
                .collection("like")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    querySnapshot?.size()?.let { it1 ->
                        binding.eventLike.setText(it1.toString())
                    }
                }

        }


        private fun postEventComment(post: Posts) {

            val posts = FirebaseFirestore.getInstance().collection("posts")
            val commentReference = posts.document(post.postId).collection("comments").document()
            val comment = Comments(
                username = com.tina.musicband.login.UserManager.userName.toString(),
                comment = binding.commentField.text.toString(),
                time = Calendar.getInstance().timeInMillis
            )

            commentReference.set(comment).addOnCompleteListener {

                if (it.isSuccessful) {

                    Log.i(
                        "Tinaaa",
                        "comment username = ${com.tina.musicband.login.UserManager.userName.toString()}"
                    )

                }
            }
        }

        private fun getEventComment(post: Posts) {
            FirebaseFirestore.getInstance()
                .collection("posts")
                .document(post.postId)
                .collection("comments")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, exception ->
                    exception?.let {
                        Log.w(TAG, "Listen failed.", it)
                    }
                    val list = mutableListOf<Comments>()
                    for (document in snapshot!!) {
                        val commentList = document.toObject(Comments::class.java)
                        list.add(commentList)
                    }
                    binding.recyclerViewMainEventComment.adapter = adapter
                    adapter.submitList(list)
                }

        }

        private fun checkEventLike(post: Posts) {
            FirebaseFirestore.getInstance().collection("posts")
                .document(post.postId)
                .collection("like")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        val like = it.result!!.toObjects(Like::class.java)

                        for (document in like) {

                            if (document.userId == com.tina.musicband.login.UserManager.userToken.toString()) {

                                binding.eventLikeClickedIcon.visibility = View.VISIBLE
                                break
                            }

                        }


                    }
                }
        }


    }

    class MusicViewHolder(private var binding: ItemMusicMainBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        private var mediaPlayer = MediaPlayer()
        lateinit var handler: Handler
        val adapter = CommentAdapter()
        var loadSong = ""

        fun bind(post: Posts, mainViewModel: MainViewModel) {

            Logger.i("POST_USER_NAME:${post.userName}")

            binding.post = post
            binding.mainViewModel = mainViewModel

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

            binding.musicEndTime.setText(post.song.songDuration)

            binding.usernameText.setText(post.userName)
            binding.musicTitle.setText(post.title)
            binding.musicComposer.setText(post.composer)
            binding.createdTimeText.setText(post.createdTime.toDisplayFormat())
            binding.musicDescription.setText(post.description)

            Glide
                .with(MusicBandApplication.instance.applicationContext)
                .load(post.song.cover)
                .transform(CenterCrop(), RoundedCorners(12))
                .placeholder(R.drawable.ic_cover)
                .error(R.drawable.ic_cover)
                .into(binding.musicCover)

            binding.musicPlayButton.setOnClickListener {

                if (post.song.songId == loadSong) {

                    mediaPlayer.start()

                } else {
                    loadSong = post.song.songId

                    prepareMediaPlayer(post.song)
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
                    val percent =
                        (1 / (mediaPlayer.duration / 100.0) * currentPosition).roundToInt()
                    binding.musicSeekBar.progress = percent
                    val startTime = createTIme(currentPosition)

                    if (startTime.contains("-")) {
                        binding.musicStartTime.setText("00:00")
                    } else {
                        binding.musicStartTime.setText(startTime)

                    }


                }
            }

            getMusicComment(post)
            checkMusicLike(post)

            binding.comment.setOnClickListener {
                binding.commentBlock.visibility = View.VISIBLE
                mainViewModel.leaveComment()
                mainViewModel.hideFab()

            }

            binding.sendButton.setOnClickListener {
                postMusicComment(post)
                binding.commentBlock.visibility = View.GONE
                binding.commentField.text = null
                mainViewModel.showFab()
            }

            binding.musicLikeIcon.setOnClickListener {
                FirebaseFirestore.getInstance().collection("posts").document(post.postId)
                    .collection("like").document(post.userId!!)
                    .set(mapOf("userId" to UserManager.userToken.toString()))
                    .addOnSuccessListener {

                        binding.musicLikeIcon.visibility = View.INVISIBLE
                        binding.musicLikeClickedIcon.visibility = View.VISIBLE

                    }
            }


            binding.musicLikeClickedIcon.setOnClickListener {
                FirebaseFirestore.getInstance().collection("posts").document(post.postId)
                    .collection("like").document(UserManager.userToken.toString()).delete()
                    .addOnSuccessListener {

                        binding.musicLikeIcon.visibility = View.VISIBLE
                        binding.musicLikeClickedIcon.visibility = View.INVISIBLE

                    }
            }

            FirebaseFirestore.getInstance().collection("posts").document(post.postId)
                .collection("like")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    querySnapshot?.size()
                        ?.let { it1 -> binding.musicLike.setText(it1.toString()) }
                }


            binding.executePendingBindings()
        }


        private fun createTIme(time: Int): String {
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
//                    mediaPlayer.setOnPreparedListener {
//                        it.start()
//                    }
                    mediaPlayer.prepareAsync()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }


        private fun postMusicComment(post: Posts) {

            val posts = FirebaseFirestore.getInstance().collection("posts")
            val commentReference = posts.document(post.postId).collection("comments").document()
            val comment = Comments(
                username = com.tina.musicband.login.UserManager.userName.toString(),
                comment = binding.commentField.text.toString(),
                time = Calendar.getInstance().timeInMillis
            )
            commentReference.set(comment)

        }

        private fun getMusicComment(post: Posts) {
            FirebaseFirestore.getInstance()
                .collection("posts")
                .document(post.postId)
                .collection("comments")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, exception ->
                    exception?.let {
                        Log.w(TAG, "Listen failed.", it)
                    }
                    val list = mutableListOf<Comments>()
                    for (document in snapshot!!) {
                        val commentList = document.toObject(Comments::class.java)
                        list.add(commentList)
                    }
                    binding.recyclerViewMainMusicComment.adapter = adapter
                    adapter.submitList(list)
                }

        }

        private fun checkMusicLike(post: Posts) {
            FirebaseFirestore.getInstance().collection("posts")
                .document(post.postId)
                .collection("like")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        val like = it.result!!.toObjects(Like::class.java)

                        for (document in like) {

                            if (document.userId == com.tina.musicband.login.UserManager.userToken.toString()) {

                                binding.musicLikeClickedIcon.visibility = View.VISIBLE
                                break
                            }

                        }


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
                holder.bind((getItem(position) as PostSealedItem.EventItem).posts, mainViewModel)
            }
            is MusicViewHolder -> {
                holder.bind((getItem(position) as PostSealedItem.MusicItem).posts, mainViewModel)
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
            return if (oldItem is PostSealedItem.EventItem && newItem is PostSealedItem.EventItem) {
                oldItem.posts.postId == newItem.posts.postId
            } else if (oldItem is PostSealedItem.MusicItem && newItem is PostSealedItem.MusicItem) {
                oldItem.posts.postId == newItem.posts.postId
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItem: PostSealedItem, newItem: PostSealedItem): Boolean {
            return if (oldItem is PostSealedItem.EventItem && newItem is PostSealedItem.EventItem) {
                oldItem.posts.postId == newItem.posts.postId
            } else if (oldItem is PostSealedItem.MusicItem && newItem is PostSealedItem.MusicItem) {
                oldItem.posts.postId == newItem.posts.postId
            } else {
                false
            }
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