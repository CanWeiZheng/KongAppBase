package com.kongappbase

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.view.ViewTreeObserver
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kongappbase.databinding.ActivityMainBinding
import com.kongappbase.http.NetworkHelper
import com.kongappbase.model.CityListInfo
import com.kongappbase.model.HttpResponseInfo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kong.project.base.http.custom.BaseDownloadSubscriber
import kong.project.base.http.custom.BaseSubscriber
import kong.project.base.http.custom.BaseUploadSubscriber
import kong.project.base.util.KLog
import java.io.File
import java.security.Permission
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var mScale = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        KLog.showLog(BuildConfig.DEBUG)
        binding.unitBtn.setOnClickListener {
            startActivity(Intent(this, UnitActivity::class.java))
        }
        binding.dialogBtn.setOnClickListener {
            KLog.log(System.currentTimeMillis().toString())
            startActivity(Intent(this, DialogActivity::class.java))
        }


        binding.requestBtn.setOnClickListener {
            NetworkHelper.getService().getCityList().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : BaseSubscriber<HttpResponseInfo<List<CityListInfo>>>() {
                })
        }
        binding.downloadBtn.setOnClickListener {
//            NetworkHelper.download("https://update.8684.cn/update1/_b_15.3.16_1543.apk",this.getExternalFilesDir(null)!!.absolutePath,object :BaseDownloadSubscriber<Any>(){
            NetworkHelper.download(
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.wang1314.net%2Fuploadfile%2F2014%2F2014-04-09%2F1397006863027u_47_uw_1680_wh_1050_hl_1188072_l.jpg&refer=http%3A%2F%2Fimg.wang1314.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1616664752&t=cef65346e10a70a3914c97fe9bf8762c",
                this.getExternalFilesDir(null)!!.absolutePath,
                object : BaseDownloadSubscriber<Any>() {
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

        binding.progressTv.text = "VersionCode----${BuildConfig.VERSION_CODE}"

        binding.animatorBtn.setOnClickListener {
            pointerAnim()
//            val picker = DatePickerDialog(this,0, null,2021,2,22)
//            picker.show()
            Flowable.intervalRange(6, 5, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    KLog.log("it---$it")
                    binding.progressBar.progress = it.toInt() * 10
                    progressAnim((it.toInt()) * 10)
                }
        }
        val HTML_TEXT =
            "<p><font size=\"3\" color=\"red\">设置了字号和颜色</font></p>" +
                    "<b><font size=\"5\" color=\"blue\">设置字体加粗 蓝色 5号</font></font></b></br>" +
                    "<h1><a href=\"http://magiclen.org/\">这个是H1标签</a></h1></br>" +
                    "<p>这里显示图片：</p><img src=\"https://img0.pconline.com.cn/pconline/1808/06/11566885_13b_thumb.jpg\""
        binding.linkTv.text = Html.fromHtml(HTML_TEXT)
        binding.linkTv.movementMethod = LinkMovementMethod.getInstance()
        val str = binding.linkTv.text
        if (str is Spannable) {
            val end = str.length
            val sp = binding.linkTv.text as Spannable
            val spans = sp.getSpans(0, end, URLSpan::class.java)
            val style = SpannableStringBuilder(str)
            style.clearSpans()
            for (url in spans) {
                style.setSpan(object : ClickableSpan() {
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = ContextCompat.getColor(this@MainActivity, R.color.purple_500)
                        ds.isUnderlineText = false
//                        super.updateDrawState(ds)
                    }

                    override fun onClick(p0: View) {
                        KLog.log("onClick------")
                    }
                }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            binding.linkTv.highlightColor = Color.TRANSPARENT
            binding.linkTv.text = style
        }
        binding.root.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                resetPointer()
                binding.pointerIv.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
        binding.locationBtn.setOnClickListener {
            testLocation()
        }


    }

    fun testLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            KLog.log("没有权限")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 111)
        } else {
            KLog.log("有权限")

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        KLog.log("permissions==" + permissions.toString())
        permissions.forEach {
            KLog.log("it----$it")
            if (it==Manifest.permission.ACCESS_FINE_LOCATION){
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    KLog.log("不再询问")
                }
            }
        }
        KLog.log("grantResults==" + grantResults.toString())
        grantResults.forEach {
            KLog.log("grantResult======$it")
        }
    }


    /**
     * html  带有<font>标签的文本
     */
    private fun getClickableHtml(html: String?): CharSequence {
        val spannedHtml: Spanned = Html.fromHtml(html)
        val clickableHtmlBuilder = SpannableStringBuilder(spannedHtml)
        val spans = clickableHtmlBuilder.getSpans(0, spannedHtml.length, URLSpan::class.java)
        for (value in spans) {
            KLog.log("value----${value.url}")
            setLinkClickable(clickableHtmlBuilder, value);
        }

        return clickableHtmlBuilder
    }


    /**
     * 捕获<a>标签点击事件
     */
    private fun setLinkClickable(clickableHtmlBuilder: SpannableStringBuilder, urlSpan: URLSpan?) {
        val start = clickableHtmlBuilder.getSpanStart(urlSpan)
        val end = clickableHtmlBuilder.getSpanEnd(urlSpan)
        val flags = clickableHtmlBuilder.getSpanEnd(urlSpan)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val url = urlSpan?.url
                KLog.log("url----$url")
            }

            override fun updateDrawState(ds: TextPaint) {
                //设置颜色
                ds.color = Color.parseColor("#000000")
                //设置是否要下划线
                ds.isUnderlineText = false
            }

        }
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags)
    }


    private fun progressAnim(value: Int) {
        val animator = ValueAnimator.ofInt(0, value)
        animator.duration = 1000
        animator.addUpdateListener {
            binding.progressBar.secondaryProgress = it.animatedValue as Int
        }
        animator.start()
    }

    private fun resetPointer() {
//        val animator = ObjectAnimator.ofFloat(binding.pointerIv, "rotation", -75f)
        binding.pointerIv.pivotX = (binding.pointerIv.width / 2).toFloat()
        binding.pointerIv.pivotY = binding.pointerIv.height.toFloat() / 165 * 130
        binding.pointerIv.rotation = -125f
//        binding.pointerIv.pivotY = binding.pointerIv.height.toFloat()
//        animator.duration = 100
//        animator.start()
    }

    private fun pointerAnim() {
        val animator = ObjectAnimator.ofFloat(
            binding.pointerIv,
            "rotation",
            binding.pointerIv.rotation,
            120f,
            -75f,
            0f
        )
        binding.pointerIv.pivotX = (binding.pointerIv.width / 2).toFloat()
        binding.pointerIv.pivotY = binding.pointerIv.height.toFloat() / 165 * 130
//        binding.pointerIv.pivotY = binding.pointerIv.height.toFloat()
        animator.duration = 3000
        animator.startDelay = 200
        animator.start()

    }

    fun upload(path: String) {
        val paramsMap = mutableMapOf<String, Any>()
        paramsMap["msid"] = "28d74b8e01baa57eed62318d8ac6a7b6_78610"

        NetworkHelper.upload(
            "https://api.8684.cn/wz/api.php?do=picture_add",
            "picture",
            File(path),
            paramsMap,
            object : BaseUploadSubscriber<Any>() {
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