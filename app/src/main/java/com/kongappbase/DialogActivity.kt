package com.kongappbase

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.kongappbase.databinding.ActivityDialogBinding
import com.kongappbase.dialog.CommonDialog

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
    }

    var dialog: CommonDialog? = null
    private fun showDialog() {
        if (dialog == null) {
            dialog = CommonDialog()
        }
        val bundle = Bundle()
        bundle.putString("hint","请输入");
        dialog!!.arguments = bundle
        dialog!!.show(supportFragmentManager, "1212")

    }
}