package com.company.currencyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.company.currencyapp.R
import com.company.currencyapp.databinding.CurrencyItemBinding
import com.company.currencyapp.domain.model.ValuteUI

class DiffUtilTaskCallback(
    private var oldList: List<ValuteUI>,
    private var newList: List<ValuteUI>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}


class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    var currencyList: List<ValuteUI> = emptyList()
        set(newValue) {
            val diffCallback = DiffUtilTaskCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    inner class CurrencyViewHolder(val binding: CurrencyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = currencyList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            CurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currentItem = currencyList[position]
        val context = holder.itemView.context
        holder.binding.apply {
            currencyName.text = context.getString(
                R.string.currency_name, currentItem.charCode, currentItem.name
            )
            currencyPrice.text = context.getString(R.string.price, currentItem.value)
        }
    }
}