package com.example.freshscanapp.data.api

import com.google.gson.annotations.SerializedName

data class VeggiesResponse(

	@field:SerializedName("vegetable")
	val vegetable: Vegetable
)

data class Vegetable(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("Step 4")
	val step4: String,

	@field:SerializedName("Step 5")
	val step5: String,

	@field:SerializedName("Step 6")
	val step6: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("Step 1")
	val step1: String,

	@field:SerializedName("Step 2")
	val step2: String,

	@field:SerializedName("Step 3")
	val step3: String,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("id")
	val id: Int
)
