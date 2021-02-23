package kong.project.base.http.custom

import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import okhttp3.ResponseBody
import okio.*
import java.io.File
import java.io.FileOutputStream

/**
 * @author: Kong
 * @date: 2020/10/12
 */
class DownLoadOnSubscribe(
    private val responseBody: ResponseBody,
    filePath: String,
    fileName: String
) :
    FlowableOnSubscribe<Any> {
    private var flowableEmitter: FlowableEmitter<Any>? = null
    private var filePath: String? = null
    private var fileName: String? = null

    //已下载
    private var downloadSize = 0L

    //资源总大小
    private var totalSize = 0L

    //完成比
    private var mPercent = 0
    private var source: Source? = null
    private var progressSource: Source? = null
    private var sink: BufferedSink? = null
    private var file:File?=null

    init {
        this.fileName = fileName
        this.filePath = filePath
        this.source = responseBody.source()
        this.source?.let {
            this.progressSource = getProgressSource(it)
        }
        file = File(filePath, fileName)
        try {
            if (!file!!.exists()) {
                file!!.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            flowableEmitter?.onComplete()

        }
        this.totalSize = responseBody.contentLength()
        sink = FileOutputStream(file, false).sink().buffer()
    }

    override fun subscribe(emitter: FlowableEmitter<Any>) {
        flowableEmitter = emitter
        flowableEmitter?.let {
            if (downloadSize >= totalSize) {
                onProgress(100)
                it.onComplete()
            }
            try {
                it.onNext(totalSize)
                progressSource?.let { it1 -> sink?.writeAll(it1.buffer()) }
                sink?.close()
                it.onComplete()
            } catch (e: Exception) {
                if (it.isCancelled) {
                    it.onError(e)
                }
            }
        }
    }

    fun onRead(read: Long) {
        downloadSize += if (read == -1L) 0 else read
        if (totalSize <= 0) {
            onProgress(-1)
        } else {
            onProgress((100 * downloadSize / totalSize).toInt())

        }
    }

    private fun onProgress(percent: Int) {
        if (flowableEmitter == null) return
        if (percent == mPercent) return
        mPercent = percent
        if (percent >= 100) {
            mPercent = 100
            flowableEmitter!!.onNext(mPercent)
            file?.let {
                flowableEmitter!!.onNext(it.absolutePath)
            }
            return
        } else {
            flowableEmitter!!.onNext(percent)
        }


    }


    private fun getProgressSource(source: Source): ForwardingSource {
        return object : ForwardingSource(source) {
            override fun read(sink: Buffer, byteCount: Long): Long {
                val read = super.read(sink, byteCount)
                onRead(read)
                return read
            }
        }

    }
}