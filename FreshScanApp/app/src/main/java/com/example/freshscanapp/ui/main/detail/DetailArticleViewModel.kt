package com.example.freshscanapp.ui.main.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshscanapp.data.api.ApiConfig
import com.example.freshscanapp.data.api.DetailArticleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailArticleViewModel : ViewModel() {

    private val _article = MutableLiveData<DetailArticleResponse?>()
    val article: LiveData<DetailArticleResponse?> = _article

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadArticleDetail(title: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService1().getDetailArticle(title)
                }
                if (response.isSuccessful) {
                    val detailArticleResponse = response.body()
                    _article.postValue(detailArticleResponse)
                    Log.d("DetailArticleViewModel", "Detail article loaded: $detailArticleResponse")
                } else {
                    _error.value = "Failed to fetch article details: ${response.message()}"
                    Log.e("DetailArticleViewModel", "Error fetching article details: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("DetailArticleViewModel", "Network error or exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
