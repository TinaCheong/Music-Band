package com.tina.musicband.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tina.musicband.data.Posts
import com.tina.musicband.databinding.ItemEventsMainBinding
import com.tina.musicband.databinding.ItemMusicMainBinding

private val ITEM_VIEW_TYPE_RECEIVE = 0
private val ITEM_VIEW_TYPE_SEND = 1

class MainAdapter : ListAdapter<Posts, RecyclerView.ViewHolder>(DiffCallback) {


    class EventViewHolder(private var binding: ItemEventsMainBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(posts: Posts) {
            binding.executePendingBindings()
        }
    }

    class MusicViewHolder(private var binding: ItemMusicMainBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(posts: Posts) {
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_RECEIVE -> {
                EventViewHolder(
                    ItemEventsMainBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), parent.context
                )
            }
            ITEM_VIEW_TYPE_SEND -> {
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
                val item = getItem(position)
                holder.bind(item)
            }
            is MusicViewHolder -> {
                val item = getItem(position)
                holder.bind(item)
            }
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Posts>() {
        override fun areItemsTheSame(oldItem: Posts, newItem: Posts): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Posts, newItem: Posts): Boolean {
            return oldItem.type == newItem.type
        }
    }



}