package com.example.satellitex.satellitelist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.config.getColorInt
import com.example.config.setVisibility
import com.example.satellitex.R
import android.widget.Filter
import com.example.satellitex.databinding.RowSatelliteBinding
import com.example.satellitex.room.Satellite
import java.util.*

class SatelliteAdapter(private val onItemClickListener: (Satellite) -> Unit) :
    RecyclerView.Adapter<SatelliteAdapter.SatelliteViewHolder>(), Filterable {

    private val allSatelliteList = mutableListOf<Satellite>()

    private val differCallback = object : DiffUtil.ItemCallback<Satellite>() {
        override fun areItemsTheSame(oldItem: Satellite, newItem: Satellite) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Satellite, newItem: Satellite) =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Satellite>) {
        differ.submitList(list)
        allSatelliteList.clear()
        allSatelliteList.addAll(list)
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
            allSatelliteList.size - 1 == position,
            onItemClickListener
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val charString = charSequence.toString()
            val filteredList = mutableListOf<Satellite>()
            if (charString.isEmpty()) {
                filteredList.addAll(allSatelliteList)
            } else {
                filteredList.addAll(allSatelliteList
                    .filter { it.name.lowercase(Locale.getDefault()).contains(charString) })
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
            differ.submitList(filterResults?.values as List<Satellite>)
            notifyItemRangeChanged(0, differ.currentList.size)
        }
    }

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