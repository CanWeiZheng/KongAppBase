package com.kongappbase

import android.graphics.Color
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.kongappbase.databinding.ActivityDialogBinding
import com.kongappbase.dialog.CommonDialog
import kong.project.base.util.KLog

/**
 * @author: Kong
 * @date: 2021/2/26
 */
class DialogActivity : FragmentActivity() {
    lateinit var binding: ActivityDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dialog)
        binding.dialogCommonBtn.setOnClickListener { showDialog() }
//        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
//            ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                initData()
//                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
//            }
//        })
        binding.root.postDelayed({ initData() }, 280)
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        KLog.log("onEnterAnimationComplete---${System.currentTimeMillis()}")
    }


    override fun onResume() {
        super.onResume()
        KLog.log(System.currentTimeMillis().toString())
    }

    fun initData() {
        for (i in 0..1000) {
            val textView = TextView(this)
            textView.text = "text view $i"
            binding.containerLayout.addView(textView)
        }

        KLog.log("initData---${System.currentTimeMillis()}")
    }


    var dialog: CommonDialog? = null
    private fun showDialog() {
        if (dialog == null) {
            dialog = CommonDialog()
        }
        val bundle = Bundle()
        bundle.putString("hint", "请输入");
        dialog!!.arguments = bundle
        dialog!!.show(supportFragmentManager, "1212")

    }
}