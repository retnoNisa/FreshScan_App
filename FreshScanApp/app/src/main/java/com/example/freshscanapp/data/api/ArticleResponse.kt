package com.example.freshscanapp.data.api

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("articles")
	val articles: List<ArticlesItem>
)

data class ArticlesItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("source")
	val source: String,

	@field:SerializedName("title")
	val title: String
)
