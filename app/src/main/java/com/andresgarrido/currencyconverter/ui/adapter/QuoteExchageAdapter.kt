package com.andresgarrido.currencyconverter.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andresgarrido.currencyconverter.databinding.GridViewItemBinding
import com.andresgarrido.currencyconverter.model.QuoteItem

class QuoteExchageAdapter() :
        ListAdapter<QuoteItem,
                QuoteExchageAdapter.MarsPropertyViewHolder>(DiffCallback) {

    class MarsPropertyViewHolder(private var binding: GridViewItemBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(quoteItem: QuoteItem) {
            binding.property = quoteItem
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<QuoteItem>() {
        override fun areItemsTheSame(oldItem: QuoteItem, newItem: QuoteItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: QuoteItem, newItem: QuoteItem): Boolean {
            return oldItem.currency.contentEquals(newItem.currency)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MarsPropertyViewHolder {
        return MarsPropertyViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MarsPropertyViewHolder, position: Int) {
        val marsProperty = getItem(position)
        holder.bind(marsProperty)
    }
}

