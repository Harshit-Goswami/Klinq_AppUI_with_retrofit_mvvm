package com.example.klinq.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.klinq.ProductDetailActivity
import com.example.klinq.R
import com.example.klinq.databinding.CircularImageItemBinding
import com.example.klinq.model.Attribute
import com.example.klinq.utils.Common

class AttributeAdapter(private val attributes: ArrayList<Attribute>, val context: Context) :
    RecyclerView.Adapter<AttributeAdapter.ViewHolder>() {


    class ViewHolder(private val binding: CircularImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            Common.loadImage(binding.attributeImageView,imgUrl)
        }

        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CircularImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bind(attributes[position].swatch_url)
        }
        holder.root.setOnClickListener {
            viewPagerSetup(position)
            dotsSetUp(position)
            ProductDetailActivity.binding.selectedColor.text = attributes[position].value
        }
    }

    override fun getItemCount(): Int {
        return attributes.size
    }

    private fun dotsSetUp(position: Int) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 0, 8, 0)
        }
        val dotsImage = Array(attributes[position].images.size) { ImageView(context) }
        ProductDetailActivity.binding.llSliderDot.removeAllViews()
        if (attributes[position].images.isNotEmpty()) {
            dotsImage.forEach {
                it.setImageResource(R.drawable.non_active_dot)
                ProductDetailActivity.binding.llSliderDot.addView(it, params)
            }
            //default first dot
            dotsImage[0].setImageResource(R.drawable.active_dot)

            val pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    dotsImage.mapIndexed { index, imageView ->
                        if (position == index) {
                            imageView.setImageResource(R.drawable.active_dot)
                        } else {
                            imageView.setImageResource(R.drawable.non_active_dot)
                        }
                    }
                    super.onPageSelected(position)
                }
            }
            ProductDetailActivity.binding.viewPager2.registerOnPageChangeCallback(
                pageChangeListener
            )
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun viewPagerSetup(position: Int){
        ProductDetailActivity.binding.viewPager2.adapter =
            ViewPagerAdapter(attributes[position].images as ArrayList<String>)
        ProductDetailActivity.binding.viewPager2.adapter!!.notifyDataSetChanged()
    }


}
