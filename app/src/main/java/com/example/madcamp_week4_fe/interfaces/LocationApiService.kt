package com.example.madcamp_week4_fe.interfaces

import com.example.madcamp_week4_fe.models.GalleryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URLDecoder

interface LocationApiService {
    @GET("galleryList1")
    fun getGalleryList(
        @Query("serviceKey") serviceKey: String = URLDecoder.decode("6pV0A29OuVqdGtWqISMIRMBb6pYP1tNsoRedxnuuQK5zyhlzSGmtjOHfDfx979w56FJVwhfWxiRgsZRh61Dbfw%3D%3D", "UTF-8"),
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "AppTest",
        @Query("arrange") arrange: String = "A",
        @Query("_type") type: String = "json"
    ): Call<GalleryResponse>


    @GET("galleryList1")
    fun getKeywordGalleryList(
        @Query("serviceKey") serviceKey: String = URLDecoder.decode("6pV0A29OuVqdGtWqISMIRMBb6pYP1tNsoRedxnuuQK5zyhlzSGmtjOHfDfx979w56FJVwhfWxiRgsZRh61Dbfw%3D%3D", "UTF-8"),
        @Query("numOfRows") numOfRows: Int = 3,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "AppTest",
        @Query("arrange") arrange: String = "A",
        @Query("keyword") keyword: String,
        @Query("_type") type: String = "json"
    ): Call<GalleryResponse>

}