package kong.project.base.http.custom

import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe

/**
 * @author: Kong
 * @date: 2020/10/12
 */
class UploadOnSubscribe : FlowableOnSubscribe<Any> {
    private var flowableEmitter: FlowableEmitter<Any>? = null
    override fun subscribe(emitter: FlowableEmitter<Any>) {
        flowableEmitter = emitter
        flowableEmitter?.onNext(0)
    }

    fun onRead(progress: Int) {
//        KLog.log("progress==$progress")
        if (progress == 100) {
            flowableEmitter?.onNext(progress)
            flowableEmitter?.onComplete()
        }
        flowableEmitter?.onNext(progress)
    }
}