package com.kongappbase

import kong.project.base.http.BaseNetwork


/**
 * @author: Kong
 * @date: 2020/9/10
 */
object NetworkHelper : BaseNetwork() {

    fun getService(): Service {
        return retrofit.create(Service::class.java)
    }
}