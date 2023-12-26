package com.atg.gallerytask.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atg.gallerytask.data.model.Photo
import com.atg.gallerytask.databinding.ListSearchListBinding
import com.bumptech.glide.Glide

class SearchAdapter : ListAdapter<Photo, SearchAdapter.SearchViewHolder>(searchComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ListSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentPhoto = getItem(position)

        holder.binding.tvPhotoTitle.text = currentPhoto.title

        Glide.with(holder.binding.root.context)
            .load(currentPhoto.photoUrl)
            .into(holder.binding.imgPhoto)
    }

    inner class SearchViewHolder(var binding: ListSearchListBinding) : RecyclerView.ViewHolder(binding.root)
}

private val searchComparator = object : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }
}
