package com.tina.musicband

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tina.musicband.data.Follower
import com.tina.musicband.data.Following
import com.tina.musicband.data.Songs
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
fun bindRecyclerViewWithFollowerings(recyclerView: RecyclerView, followings: List<Following>?) {

    followings?.let {
        recyclerView.adapter.apply {
            when (this) {
                is FollowingAdapter -> submitList(it)
            }
        }


    }
}