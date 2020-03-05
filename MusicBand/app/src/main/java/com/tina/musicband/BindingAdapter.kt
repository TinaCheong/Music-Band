package com.tina.musicband

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.data.Follower
import com.tina.musicband.data.Following
import com.tina.musicband.data.Songs
import com.tina.musicband.data.User
import com.tina.musicband.dialog.BACKGROUND
import com.tina.musicband.dialog.getBackgroundDrawable
import com.tina.musicband.follower.FollowerAdapter
import com.tina.musicband.following.FollowingAdapter
import com.tina.musicband.search.SearchMusicAdapter

@BindingAdapter("followers")
fun bindRecyclerViewWithFollowers(recyclerView: RecyclerView, followers: List<Follower>?){

    followers?.let {
        recyclerView.adapter.apply {
            when(this){
                is FollowerAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("followings")
fun bindRecyclerViewWithFollowings(recyclerView: RecyclerView, followings: List<Following>?) {

    followings?.let {
        recyclerView.adapter.apply {
            when (this) {
                is FollowingAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("songs")
fun bindRecyclerViewWithSongs(recyclerView: RecyclerView, songs: List<Songs>?){

    songs?.let {
        recyclerView.adapter.apply {
            when(this){
                is SearchMusicAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("setBackground")
fun bindBackgroundSetting(imageView: ImageView, user: User?) {

    imageView.setImageDrawable(user?.background?.getBackgroundDrawable())
}

@BindingAdapter("setAvatar")
fun bindAvatarSetting(imageView: ImageView, user: User?) {

    imageView.setImageDrawable(user?.avatar?.getAvatarDrawable())
}