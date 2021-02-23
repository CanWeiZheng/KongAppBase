package kong.project.base.http.custom

import android.content.Context
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import kong.project.base.util.UtilComm
import okhttp3.ResponseBody
import org.reactivestreams.Publisher
import retrofit2.Response

/**
 * @author: Kong
 * @date: 2020/10/12
 */
class DownloadTransformer(private val context: Context, private val url: String) :
    FlowableTransformer<Response<ResponseBody>, Any> {
    override fun apply(upstream: Flowable<Response<ResponseBody>>): Publisher<Any> {
        val fileName = UtilComm.md5(url)
        val filePath = context.getExternalFilesDir(null)!!.absolutePath
        return upstream.flatMap {
            if (it.body() == null || it.body()?.source() == null) {
                Flowable.empty<Any>()
            }
            it.body()?.let { responseBody ->
                Flowable.create(
                    DownLoadOnSubscribe(responseBody, filePath, fileName),
                    BackpressureStrategy.BUFFER
                )
            }
        }
    }
}