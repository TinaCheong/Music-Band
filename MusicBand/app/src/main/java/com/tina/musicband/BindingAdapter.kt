package com.tina.musicband

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.data.*
import com.tina.musicband.dialog.BACKGROUND
import com.tina.musicband.dialog.getBackgroundDrawable
import com.tina.musicband.follower.FollowerAdapter
import com.tina.musicband.following.FollowingAdapter
import com.tina.musicband.main.MainAdapter
import com.tina.musicband.main.PostSealedItem
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

@BindingAdapter("posts")
fun bindRecyclerViewWithPosts(recyclerView: RecyclerView, posts: List<PostSealedItem>?){

    posts?.let {
        recyclerView.adapter.apply {
            when(this){
                is MainAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("setBackground")
fun bindBackgroundSetting(imageView: ImageView, user: User?) {

    imageView.setImageDrawable(user?.background?.getBackgroundDrawable())
}

@BindingAdapter("setAvatar")
fun bindAvatarSetting(imageView: ImageView, avatar: Int) {

    imageView.setImageDrawable(avatar.getAvatarDrawable())
}

@BindingAdapter("setAvatarByUser")
fun bindAvatarSettingByUser(imageView: ImageView, user: User?) {

    imageView.setImageDrawable(user?.avatar?.getAvatarDrawable())
}
