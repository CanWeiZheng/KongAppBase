package kong.project.base.http

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
interface DownloadService {

    @Streaming
    @GET()
    fun download(@Url url: String): Flowable<Response<ResponseBody>>
}