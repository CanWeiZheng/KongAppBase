package kong.project.base.http

import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * @author: Kong
 * @date: 2021/2/23
 */
interface BaseService {

    @Streaming
    @GET()
    fun download(@Url url: String): Flowable<Response<ResponseBody>>

    @Multipart
    @POST
    fun upload(@Url url: String, @Part file: MultipartBody.Part, @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>): Flowable<Any>

}