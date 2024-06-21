package com.example.freshscanapp.data.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("getallarticles")
    suspend fun getArticles(): Response<ArticleResponse>

    @GET("getarticle/{title}")
    suspend fun getDetailArticle(@Path("title") title: String): Response<DetailArticleResponse>

    @GET("getdetail/carrot")
    suspend fun getCarrotDetail(): Response<VeggiesResponse>

    @GET("getdetail/potato")
    suspend fun getPotatoDetail(): Response<VeggiesResponse>

    @GET("getdetail/tomato")
    suspend fun getTomatoDetail(): Response<VeggiesResponse>

    @GET("getdetail/pepper")
    suspend fun getPepperDetail(): Response<VeggiesResponse>

    @GET("getdetail/cucumber")
    suspend fun getCucumberDetail(): Response<VeggiesResponse>

    @GET("getdetail/okra")
    suspend fun getOkraDetail(): Response<VeggiesResponse>

    @GET("getdetail/cabbage")
    suspend fun getCabbageDetail(): Response<VeggiesResponse>

    @Multipart
    @POST("scan")
    suspend fun scanImage(
        @Part file: MultipartBody.Part
    ): ScanResponse
}