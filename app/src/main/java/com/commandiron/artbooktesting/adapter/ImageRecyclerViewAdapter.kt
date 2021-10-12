package com.commandiron.artbooktesting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.commandiron.artbooktesting.R
import com.commandiron.artbooktesting.view.ImageApiFragmentDirections
import javax.inject.Inject

class ImageRecyclerViewAdapter @Inject constructor(
    val glide: RequestManager): RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    private val diffUtil = object: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var images: List<String>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.search_art_row, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.searchArtRowImage)

        holder.itemView.apply {
            glide.load(images[position]).into(imageView)
        }

        holder.itemView.setOnClickListener {
            val action = ImageApiFragmentDirections.actionImageApiFragmentToArtDetailsFragment(artUrl = images[position],"","","")
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int = images.size
}