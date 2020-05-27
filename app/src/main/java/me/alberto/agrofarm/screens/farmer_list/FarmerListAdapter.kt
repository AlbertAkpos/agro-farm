package me.alberto.agrofarm.screens.farmer_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.alberto.agrofarm.database.Farmer
import me.alberto.agrofarm.databinding.FarmerItemBinding

class FarmerListAdapter(private val clickListener: FarmerClickListener? = null) :
    ListAdapter<Farmer, RecyclerView.ViewHolder>(DiffCallback()) {
    class DiffCallback : DiffUtil.ItemCallback<Farmer>() {
        override fun areItemsTheSame(oldItem: Farmer, newItem: Farmer): Boolean {
            return oldItem.farmerId == newItem.farmerId
        }

        override fun areContentsTheSame(oldItem: Farmer, newItem: Farmer): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FarmerItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val farmer = getItem(position)
        when (holder) {
            is FarmerItemHolder -> holder.bind(farmer, clickListener)
        }
    }


    class FarmerItemHolder(private val binding: FarmerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            farmer: Farmer,
            clickListener: FarmerClickListener?
        ) {
            binding.farmer = farmer
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val binding =
                    FarmerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                return FarmerItemHolder(binding)
            }
        }
    }


    class FarmerClickListener(val clickListener: (farmer: Farmer) -> Unit) {
        fun onClick(farmer: Farmer) = clickListener(farmer)
    }
}