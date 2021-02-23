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
import kong.project.base.http.custom.BaseUploadSubscriber
import kong.project.base.http.custom.DownloadTransformer
import kong.project.base.util.KLog
import kong.project.base.util.UtilUnit
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        KLog.showLog(BuildConfig.DEBUG)
        binding.requestBtn.setOnClickListener {
            NetworkHelper.getService().getCityList().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : BaseSubscriber<HttpResponseInfo<List<CityListInfo>>>() {
                })
        }
        binding.downloadBtn.setOnClickListener {
//            NetworkHelper.download("https://update.8684.cn/update1/_b_15.3.16_1543.apk",this.getExternalFilesDir(null)!!.absolutePath,object :BaseDownloadSubscriber<Any>(){
            NetworkHelper.download("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.wang1314.net%2Fuploadfile%2F2014%2F2014-04-09%2F1397006863027u_47_uw_1680_wh_1050_hl_1188072_l.jpg&refer=http%3A%2F%2Fimg.wang1314.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1616664752&t=cef65346e10a70a3914c97fe9bf8762c",this.getExternalFilesDir(null)!!.absolutePath,object :BaseDownloadSubscriber<Any>(){
                override fun onStart(d: Disposable) {
                    super.onStart(d)
                    binding.progressTv.text = "下载准备中。。。"

                }
                override fun onProgress(progress: Int) {
                    super.onProgress(progress)
                    binding.progressTv.text = "进度$progress"
                }

                override fun onDownloadSuccess(path: String) {
                    super.onDownloadSuccess(path)
                    upload(path)
                }
            })
        }
    }

    fun upload(path:String){
        val paramsMap = mutableMapOf<String,Any>()
        paramsMap["msid"] = "28d74b8e01baa57eed62318d8ac6a7b6_78610"

        NetworkHelper.upload("https://api.8684.cn/wz/api.php?do=picture_add","picture",File(path),paramsMap,object:BaseUploadSubscriber<Any>(){
            override fun onProgress(progress: Int) {
                super.onProgress(progress)
                binding.progressTv.text = "上传进度$progress"
            }

            override fun onUploadSuccess(result: String) {
                super.onUploadSuccess(result)
                binding.progressTv.text = "上传成功$result"
            }

        })
    }
}