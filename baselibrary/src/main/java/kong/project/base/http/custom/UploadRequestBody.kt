package kong.project.base.http.custom

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.*
import java.io.File

/**
 * @author: Kong
 * @date: 2020/10/12
 */
class UploadRequestBody(file: File) :
    RequestBody() {
    private var requestBody: RequestBody? = null
    var bytesWritten = 0L
    var totalSize = 1L
    var mUploadOnSubscribe: UploadOnSubscribe? = null

    init {
        totalSize = file.length()
        requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    fun setUploadOnSubscribe(uploadOnSubscribe: UploadOnSubscribe) {
        this.mUploadOnSubscribe = uploadOnSubscribe
    }


    override fun contentType(): MediaType? {
        return requestBody?.contentType()
    }

    override fun writeTo(sink: BufferedSink) {
        val countingSink = CountingSink(sink)
        val bufferedSink = countingSink.buffer()
        requestBody!!.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            bytesWritten += byteCount
            mUploadOnSubscribe?.onRead((100 * bytesWritten / totalSize).toInt())
        }
    }
}
