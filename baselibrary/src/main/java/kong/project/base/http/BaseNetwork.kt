package kong.project.base.http

import kong.project.base.BuildConfig
import kong.project.base.http.custom.CustomGsonConverterFactory
import kong.project.base.util.KLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * @author: Kong
 * @date: 2020/9/14
 */
open class BaseNetwork {
    val retrofit = getRetrofit()

    companion object {
        private fun getOkHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    KLog.log(message)
                }
            }).also {
                if (BuildConfig.DEBUG) {
                    it.level = HttpLoggingInterceptor.Level.BODY
                } else {
                    it.level = HttpLoggingInterceptor.Level.NONE
                }
            }
            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()

        }

        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl("https:127.0.0.1")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .build()
        }
    }

    fun download() {

    }


}