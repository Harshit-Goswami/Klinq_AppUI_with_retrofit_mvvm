package com.example.klinq

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.klinq.adapters.AttributeAdapter
import com.example.klinq.adapters.ViewPagerAdapter
import com.example.klinq.databinding.ActivityProductDetailBinding
import com.example.klinq.model.Attribute
import com.example.klinq.utils.ApiState
import com.example.klinq.utils.Common
import com.example.klinq.utils.Connnectivity
import com.example.klinq.viewModel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {
    private var attributeList = ArrayList<Attribute>()

    companion object {
        var imgList = ArrayList<String>()
        lateinit var binding: ActivityProductDetailBinding
    }

    private val productDetailViewModel: ProductDetailViewModel by viewModels()
    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback
    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8, 0, 8, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Connnectivity.isNetworkAvailable(this)) {
            getProductFromApi()
        } else {
            Common.showFixSnackbar(binding.root, this, "No Internet Connectivity")
            binding.llBody.visibility = View.GONE
        }
        binding.btnAddQuantity.setOnClickListener {
            setQuantity(true)

        }
        binding.btnMinusQuantity.setOnClickListener {
            setQuantity(false)
        }
        binding.btnProductInfo.setOnClickListener {
            if (binding.productDetailText.isVisible) {
                binding.btnProductInfo.setImageResource(R.drawable.down_arrow)
                binding.productDetailText.visibility = View.GONE
            } else {
                binding.btnProductInfo.setImageResource(R.drawable.up_arrow)
                binding.productDetailText.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setQuantity(increment: Boolean) {
        val i: String = binding.btnQuantity.text.toString()
        if (increment) {
            binding.btnQuantity.text = ( i.toInt() + 1).toString()
        } else {
            if (i.toInt() > 1) {
                binding.btnQuantity.text = (i.toInt() - 1).toString()
            }
        }
    }

    private fun setUpViewPagerDots(images: List<String>) {
        val dotsImage = Array(images.size) { ImageView(this) }
        if (images.isNotEmpty()) {
            dotsImage.forEach {
                it.setImageResource(R.drawable.non_active_dot)
                binding.llSliderDot.addView(it, params)
            }
            //default first dot
            dotsImage[0].setImageResource(R.drawable.active_dot)

            pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
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

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewPagerWithDots() {
        binding.viewPager2.adapter = ViewPagerAdapter(imgList)
        setUpViewPagerDots(imgList)
        binding.viewPager2.registerOnPageChangeCallback(pageChangeListener)
        binding.viewPager2.adapter!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        binding.rsvAttribute.apply {
            layoutManager =
                LinearLayoutManager(
                    this@ProductDetailActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = AttributeAdapter(attributeList, this@ProductDetailActivity)
            setHasFixedSize(true)
            adapter!!.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getProductFromApi() {
        productDetailViewModel.getProduct()
        lifecycleScope.launch {
            productDetailViewModel._productStateFlow.collect {
                when (it) {
                    ApiState.Empty -> {
                        Common.showSnackbar(binding.root, this@ProductDetailActivity, "Empty Data")
                    }

                    is ApiState.Failure -> {
                        Common.showSnackbar(
                            binding.root,
                            this@ProductDetailActivity,
                            "Err- ${it.msg}"
                        )
                    }

                    ApiState.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                        binding.llBody.visibility = View.GONE
                    }

                    is ApiState.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        binding.llBody.visibility = View.VISIBLE
                        binding.product = it.data.data
//                        ViewPagerAdapter().setImageList(it.data.data.images)
                        imgList.addAll(it.data.data.images)

                        it.data.data.configurable_option.forEach { attr ->
                            attributeList.addAll(attr.attributes)
                        }
                        initRecyclerView()
                        initViewPagerWithDots()
                        binding.productDetailText.text =
                            Html.fromHtml(it.data.data.description, Html.FROM_HTML_MODE_LEGACY)
                        val price = it.data.data.price.toDouble()
                        val formattedPrice = "%.2f".format(price)
                        binding.price.text = "${formattedPrice} KWD"
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager2.unregisterOnPageChangeCallback(pageChangeListener)
    }
}