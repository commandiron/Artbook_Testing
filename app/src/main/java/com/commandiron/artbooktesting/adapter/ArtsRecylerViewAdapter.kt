package com.commandiron.artbooktesting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.commandiron.artbooktesting.R
import com.commandiron.artbooktesting.model.Art
import com.commandiron.artbooktesting.view.ArtsFragmentDirections
import javax.inject.Inject

class ArtsRecylerViewAdapter @Inject constructor(
    val glide: RequestManager): RecyclerView.Adapter<ArtsRecylerViewAdapter.ArtsViewHolder>() {

    class ArtsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    private val diffUtil = object: DiffUtil.ItemCallback<Art>() {

        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var arts: List<Art>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtsViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.art_row, parent, false)
        return ArtsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtsViewHolder, position: Int) {
        val list = recyclerListDiffer.currentList

        val imageView = holder.itemView.findViewById<ImageView>(R.id.artRowImageView)
        val nameTextView = holder.itemView.findViewById<TextView>(R.id.artRowNameTextView)
        val artistNameTextView = holder.itemView.findViewById<TextView>(R.id.artRowArtistNameTextView)
        val yearTextView = holder.itemView.findViewById<TextView>(R.id.artRowYearTextView)

        holder.itemView.apply {
            glide.load(list[position].artImageUrl).into(imageView)
            nameTextView.text = list[position].artName
            artistNameTextView.text = list[position].artArtistName
            yearTextView.text = list[position].artYear.toString()
        }

        holder.itemView.setOnClickListener {
            val action = ArtsFragmentDirections
                .actionArtsFragmentToArtDetailsFragment(
                    list[position].artImageUrl,
                    list[position].artName,
                    list[position].artArtistName,
                    list[position].artYear.toString())
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int = arts.size
}