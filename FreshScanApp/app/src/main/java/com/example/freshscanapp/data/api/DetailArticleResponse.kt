package com.example.freshscanapp.data.api

import com.google.gson.annotations.SerializedName

data class DetailArticleResponse(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("source")
	val source: String,

	@field:SerializedName("title")
	val title: String
)
