package com.example.freshscanapp.data.api

import com.google.gson.annotations.SerializedName

data class ScanResponse(

	@field:SerializedName("data")
	val data: Data
)

data class Data(

	@field:SerializedName("prediction")
	val prediction: String? = null,

	@field:SerializedName("freshness_info")
	val freshnessInfo: String? = null
)