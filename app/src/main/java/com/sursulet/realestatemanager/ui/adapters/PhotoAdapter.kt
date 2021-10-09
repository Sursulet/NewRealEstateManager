package com.sursulet.realestatemanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sursulet.realestatemanager.databinding.PhotoItemBinding
import javax.inject.Inject

class PhotoAdapter @Inject constructor(private val glide: RequestManager) :
    ListAdapter<PhotoUiModel, PhotoAdapter.ViewHolder>(DiffCallback()) {

    private var onItemClickListener: ((PhotoUiModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (PhotoUiModel) -> Unit) {
        onItemClickListener = listener
    }

    class DiffCallback : DiffUtil.ItemCallback<PhotoUiModel>() {
        override fun areItemsTheSame(
            oldItem: PhotoUiModel,
            newItem: PhotoUiModel
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: PhotoUiModel,
            newItem: PhotoUiModel
        ): Boolean =
            oldItem == newItem
    }

    class ViewHolder(
        private val binding: PhotoItemBinding,
        private val glide: RequestManager,
        private val listener: ((PhotoUiModel) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PhotoUiModel) {
            binding.apply {
                glide.load(item.image).into(photoItemImg)
                photoItemTitle.text = item.title

                root.setOnClickListener {
                    listener?.let { click -> click(item) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, glide, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}