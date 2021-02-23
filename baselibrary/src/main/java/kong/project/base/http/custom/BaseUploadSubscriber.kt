package kong.project.base.http.custom

import io.reactivex.disposables.Disposable
import io.reactivex.subscribers.ResourceSubscriber
import kong.project.base.util.KLog
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * @author: Kong
 * @date: 2020/10/15
 */
abstract class BaseUploadSubscriber<T> : ResourceSubscriber<T>() {
    override fun onStart() {
        super.onStart()
        request(Long.MAX_VALUE)
        onStart(this)
    }

    override fun onError(e: Throwable) {
        if (e is ConnectException || e is TimeoutException || e is SocketTimeoutException || e is UnknownHostException) {
            // 网络连接错误
            onNormalError(e, "网络连接异常")
            return
        } else if (e is HttpException) {
            // 404等错误
            onNormalError(e, "服务器开小差")
            return
        } else if (e is IOException) {
            // Json数据解析错误
            onNormalError(e, "数据解析错误")
            return
        } else {
            onNormalError(e, "服务器异常")
            return
        }
    }

    override fun onComplete() {}
    open fun onNormalError(e: Throwable, uiMsg: String) {
        e.printStackTrace()
        KLog.log(e.message!!)
        onError(message = uiMsg)
    }

    open fun onStart(d: Disposable) {}
    override fun onNext(t: T) {
        when (t) {
            is Int -> {
                onProgress(t)
            }
            else -> {
                onUploadSuccess(t.toString())
            }
        }

    }

    open fun onProgress(progress: Int) {}
    open fun onUploadSuccess(result: String) {}

    open fun onError(message: String) {}
}