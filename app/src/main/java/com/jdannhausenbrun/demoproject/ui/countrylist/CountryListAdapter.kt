package com.jdannhausenbrun.demoproject.ui.countrylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jdannhausenbrun.demoproject.R
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.databinding.ItemCountryBinding

class CountryListAdapter: PagingDataAdapter<Country, CountryListAdapter.CountryViewHolder>(DiffCallback()) {

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCountryBinding.bind(itemView)

        fun bind(country: Country?) {
            binding.countryName.text = country?.name ?: ""
            binding.root.setOnClickListener {
                it.findNavController().navigate(CountryListFragmentDirections.selectCountry(country?.code ?: ""))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CountryViewHolder(inflater.inflate(R.layout.item_country, parent, false))
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem.code == oldItem.code
        }

        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem.name == newItem.name
        }
    }
}