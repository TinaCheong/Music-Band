package com.tina.musicband.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tina.musicband.data.Matching
import com.tina.musicband.databinding.ItemUserMatchBinding

class QuickMatchAdapter : ListAdapter<Matching, QuickMatchAdapter.EventViewHolder>(DiffCallback) {


    class EventViewHolder(private var binding: ItemUserMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(matching: Matching) {
            binding.executePendingBindings()
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


    companion object DiffCallback : DiffUtil.ItemCallback<Matching>() {
        override fun areItemsTheSame(oldItem: Matching, newItem: Matching): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Matching, newItem: Matching): Boolean {
            return oldItem.userId == newItem.userId
        }
    }



}