package com.example.klinq.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klinq.repository.ProductDetailRepo
import com.example.klinq.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val productDetailRepo: ProductDetailRepo) :
    ViewModel() {

    private val productStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    val _productStateFlow: StateFlow<ApiState> = productStateFlow

    fun getProduct() = viewModelScope.launch {
        productStateFlow.value = ApiState.Loading
        productDetailRepo.getProduct()
            .catch { e ->
                productStateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                productStateFlow.value = ApiState.Success(data)
            }
    }
}
