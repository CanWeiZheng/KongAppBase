package com.kongappbase

import com.google.gson.annotations.SerializedName

/**
 * @author: Kong
 * @date: 2020/9/14
 */
data class HttpResponseInfo<out T>(
    var recordcount: Int = 0,
    var queryStatus: Int = 0,
    val resultMsg: String = "",
    @SerializedName("error_code", alternate = ["errno"])
    val code: Int,
    @SerializedName("error_message", alternate = ["errmsg"])
    var message: String,
    @SerializedName(value = "data", alternate = ["list", "info", "rule", "cars", "requestPayment"])
    val data: T?

) {
    fun isSuccess(): Boolean {
        return code == 0
    }
}