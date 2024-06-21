package com.example.freshscanapp.ui.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshscanapp.data.api.ApiConfig
import com.example.freshscanapp.data.api.ArticlesItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleViewModel : ViewModel() {

    private val apiService = ApiConfig.getApiService1()

    private val _articles = MutableLiveData<List<ArticlesItem>>()
    val articles: LiveData<List<ArticlesItem>> get() = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = withContext(Dispatchers.IO) { apiService.getArticles() }
                if (response.isSuccessful) {
                    val articlesResponse = response.body()
                    Log.d("ArticleViewModel", "Response successful: ${articlesResponse?.articles}")
                    _articles.value = articlesResponse?.articles ?: emptyList()
                } else {
                    Log.e("ArticleViewModel", "Response unsuccessful: ${response.errorBody()}")
                    _error.value = "Response unsuccessful: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                Log.e("ArticleViewModel", "Network error or exception: ${e.message}")
                _error.value = "Network error or exception: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
