package kong.project.base.http

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kong.project.base.BuildConfig
import kong.project.base.http.custom.BaseDownloadSubscriber
import kong.project.base.http.custom.CustomGsonConverterFactory
import kong.project.base.http.custom.DownloadTransformer
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
    private val downloadService by lazy { Companion.getDownloadService(this) }

    companion object {
        private fun getOkHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    KLog.log(message)
                }
            }).also {
                if (BuildConfig.DEBUG) {
                    it.level = HttpLoggingInterceptor.Level.BASIC
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

        private fun getDownloadService(baseNetwork: BaseNetwork): DownloadService {
            return baseNetwork.retrofit.create(DownloadService::class.java)
        }
    }

    fun download(
        url: String,
        downloadDirectory: String,
        baseDownloadSubscriber: BaseDownloadSubscriber<Any>
    ) {
        downloadService.download(url).flatMap {
            Flowable.just(it)
                .compose(DownloadTransformer(it.raw().request.url.toString(), downloadDirectory))
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe(baseDownloadSubscriber)
    }


}