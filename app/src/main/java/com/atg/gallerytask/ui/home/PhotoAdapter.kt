package com.atg.gallerytask.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.atg.gallerytask.data.model.Photo
import com.atg.gallerytask.databinding.ItemPhotoListBinding

class PhotoAdapter : PagingDataAdapter<Photo, PhotoAdapter.PhotoViewHolder>(photoComparator){

    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(photoTitle: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentPhoto = getItem(position)
        holder.bind(currentPhoto)

        if(::onItemClickListener.isInitialized){
            holder.itemView.setOnClickListener {
                if (currentPhoto != null) {
                    onItemClickListener.onItemClick(currentPhoto.title)
                }
            }
        }
    }

    inner class PhotoViewHolder(var binding: ItemPhotoListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo?) {

            if (photo != null) {
                binding.tvPhotoTitle.text = photo.title

                Glide.with(binding.root.context)
                    .load(photo.photoUrl)
                    .into(binding.imgPhoto)
            }
        }
    }
}

private val photoComparator = object : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }
}
