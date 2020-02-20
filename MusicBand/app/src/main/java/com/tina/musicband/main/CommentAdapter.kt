package com.tina.musicband.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tina.musicband.data.Comments
import com.tina.musicband.databinding.ItemCommentMainBinding


class CommentAdapter() : ListAdapter<Comments, CommentAdapter.CommentViewHolder>(DiffCallback) {


    class CommentViewHolder(private var binding: ItemCommentMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comments) {
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ItemCommentMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Comments>() {
        override fun areItemsTheSame(oldItem: Comments, newItem: Comments): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Comments, newItem: Comments): Boolean {
            return oldItem.comment == newItem.comment
        }
    }



}