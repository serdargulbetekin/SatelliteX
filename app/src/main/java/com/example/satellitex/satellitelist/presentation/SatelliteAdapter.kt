package com.example.satellitex.satellitelist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.config.getColorInt
import com.example.config.setVisibility
import com.example.satellitex.R
import com.example.satellitex.databinding.RowSatelliteBinding
import com.example.satellitex.room.Satellite

class SatelliteAdapter(private val onItemClickListener: (Satellite) -> Unit) :
    RecyclerView.Adapter<SatelliteAdapter.SatelliteViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Satellite>() {
        override fun areItemsTheSame(oldItem: Satellite, newItem: Satellite) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Satellite, newItem: Satellite) =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Satellite>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SatelliteViewHolder {
        return SatelliteViewHolder(
            RowSatelliteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SatelliteViewHolder, position: Int) {
        holder.onBindItem(
            differ.currentList[position],
            differ.currentList.size - 1 == position,
            onItemClickListener
        )
    }

    override fun getItemCount() = differ.currentList.size

    inner class SatelliteViewHolder(private val binding: RowSatelliteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBindItem(
            satellite: Satellite,
            isLastItem: Boolean,
            onItemClickListener: (Satellite) -> Unit
        ) {
            with(binding) {
                textViewName.text = satellite.name
                if (satellite.active) {
                    with(textViewStatus) {
                        setTextColor(getColorInt(R.color.black))
                        text = itemView.context.getString(R.string.active)
                    }
                    with(textViewName) {
                        setTextColor(getColorInt(R.color.black))
                    }
                    imageViewStatus.setImageResource(R.drawable.ic_active)
                } else {
                    with(textViewStatus) {
                        setTextColor(getColorInt(R.color.grey))
                        textViewStatus.text = binding.root.context.getString(R.string.passive)
                    }
                    with(textViewName) {
                        setTextColor(getColorInt(R.color.grey))
                    }
                    imageViewStatus.setImageResource(R.drawable.ic_passive)
                }
                viewDivider.setVisibility(isLastItem.not())
                root.setOnClickListener { onItemClickListener.invoke(satellite) }
            }
        }
    }
}