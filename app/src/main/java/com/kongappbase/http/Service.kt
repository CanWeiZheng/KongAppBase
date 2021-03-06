package com.kongappbase.http

import com.kongappbase.constant.ConstantUrl
import com.kongappbase.model.CityListInfo
import com.kongappbase.model.HttpResponseInfo
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author: Kong
 * @date: 2021/2/23
 */
interface Service {
    @GET(ConstantUrl.CITY_LIST)
    fun getCityList(): Flowable<HttpResponseInfo<List<CityListInfo>>>

    @Streaming
    @GET()
    fun download(@Url url: String): Flowable<Response<ResponseBody>>
}