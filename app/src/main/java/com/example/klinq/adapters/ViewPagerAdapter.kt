package com.example.klinq.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.klinq.databinding.ImageItemViewPagerBinding
import com.example.klinq.utils.Common

class ViewPagerAdapter(private val imageUrls : ArrayList<String>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
//    var imageUrls = emptyList<String>()

//    @SuppressLint("NotifyDataSetChanged")
//    fun setImageList(imageList: List<String>) {
//        this.imageUrls = imageList
//        notifyDataSetChanged()
//    }


    class ViewHolder(private val binding: ImageItemViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            Common.loadImage(binding.viewPager2ImageView,imgUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewHolder {
        return ViewHolder(
            ImageItemViewPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }


    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewHolder, position: Int) {
        holder.apply {
            bind(imageUrls[position])
        }
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }
}