package com.kongappbase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.kongappbase.databinding.ActivityMainBinding
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
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        KLog.showLog(BuildConfig.DEBUG)
        Log.d("kong", "dp2px----${UtilUnit.dp2Px(this, 20f)}")
        binding.requestBtn.setOnClickListener {
            NetworkHelper.getService().getCityList().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : BaseSubscriber<HttpResponseInfo<List<CityListInfo>>>() {
                })
        }
        binding.downloadBtn.setOnClickListener {
            NetworkHelper.download("https://update.8684.cn/update1/_b_15.3.16_1543.apk",this.getExternalFilesDir(null)!!.absolutePath,object :BaseDownloadSubscriber<Any>(){
                override fun onStart(d: Disposable) {
                    super.onStart(d)
                    binding.progressTv.text = "下载准备中。。。"

                }
                override fun onProgress(progress: Int) {
                    super.onProgress(progress)
                    KLog.log("progress---$progress")
                    binding.progressTv.text = "进度$progress"
                }

                override fun onDownloadSuccess(path: String) {
                    super.onDownloadSuccess(path)
                    KLog.log("onDownloadSuccess------$path")
                }
            })
        }
    }
}