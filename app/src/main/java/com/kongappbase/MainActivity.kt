package com.kongappbase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kong.project.base.http.custom.BaseDownloadSubscriber
import kong.project.base.http.custom.BaseSubscriber
import kong.project.base.http.custom.DownloadTransformer
import kong.project.base.util.KLog
import kong.project.base.util.UtilUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        KLog.showLog(BuildConfig.DEBUG)
        Log.d("kong", "dp2px----${UtilUnit.dp2Px(this, 20f)}")
        NetworkHelper.getService().getCityList().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : BaseSubscriber<HttpResponseInfo<List<CityListInfo>>>() {
            })

        NetworkHelper.getService().download("https://update.8684.cn/update1/_b_15.3.16_1543.apk").flatMap {
            Flowable.just(it).compose(DownloadTransformer(this,it.raw().request.url.toString()))
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe(object :BaseDownloadSubscriber<Any>(){
                override fun onProgress(progress: Int) {
                    super.onProgress(progress)
                    KLog.log("progress---${progress}")
                }

                override fun onDownloadSuccess(path: String) {
                    super.onDownloadSuccess(path)
                    KLog.log("path---${path}")
                }
            })
    }
}