package me.alberto.agrofarm.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import me.alberto.agrofarm.R
import me.alberto.agrofarm.database.Farmer
import me.alberto.agrofarm.screens.farmer_list.FarmerListAdapter

@BindingAdapter("app:farmerList")
fun setFarmerListRecyclerView(recyclerView: RecyclerView, list: List<Farmer>?) {
    val adapter = recyclerView.adapter as FarmerListAdapter
    adapter.submitList(list)
}

@BindingAdapter("app:setImage")
fun setFarmerImage(imageView: CircleImageView, url: String?) {
    Glide.with(imageView.context)
        .load(url)
        .placeholder(R.drawable.ic_profile)
        .error(R.drawable.ic_profile)
        .into(imageView)
}

@BindingAdapter("app:recentData")
fun setRecentlyAddedRecyclerView(recyclerView: RecyclerView, list: List<Farmer>?) {
    list?.let {
        val shortList = when {
            list.size <= 3 -> list
            else -> list.subList(list.size - 3, list.size).reversed()
        }

        val adapter = recyclerView.adapter as FarmerListAdapter
        adapter.submitList(shortList)
    }
}