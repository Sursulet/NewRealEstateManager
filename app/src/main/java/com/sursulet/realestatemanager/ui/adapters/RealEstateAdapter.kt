package com.sursulet.realestatemanager.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sursulet.realestatemanager.databinding.RealEstateItemBinding
import javax.inject.Inject

class RealEstateAdapter @Inject constructor(
    private val glide: RequestManager
) : ListAdapter<RealEstateUiModel, RealEstateAdapter.ViewHolder>(DiffCallback()) {

    private var onItemClickListener: ((Long) -> Unit)? = null

    fun setOnItemClickListener(listener: (Long) -> Unit) {
        onItemClickListener = listener
    }

    class DiffCallback : DiffUtil.ItemCallback<RealEstateUiModel>() {
        override fun areItemsTheSame(
            oldItem: RealEstateUiModel,
            newItem: RealEstateUiModel
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: RealEstateUiModel,
            newItem: RealEstateUiModel
        ): Boolean =
            oldItem == newItem
    }

    class ViewHolder(
        private val binding: RealEstateItemBinding,
        private val glide: RequestManager,
        private val listener: ((Long) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun bind(item: RealEstateUiModel) {
            binding.apply {
                root.setBackgroundColor(item.backgroundColorRes)
                realEstateItemPrice.setTextColor(item.textColor)

                glide.load(item.image).centerCrop().into(realEstateItemImage)
                realEstateItemType.text = item.type
                realEstateItemCity.text = item.city
                realEstateItemPrice.text = item.price

                root.setOnClickListener {
                    listener?.let { click -> click(item.id) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RealEstateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, glide, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}