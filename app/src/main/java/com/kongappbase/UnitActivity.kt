package com.kongappbase

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.kongappbase.databinding.ActivityUnitBinding
import kong.project.base.util.UtilFormat

/**
 * @author: Kong
 * @date: 2021/2/26
 */
class UnitActivity : FragmentActivity() {
    lateinit var binding: ActivityUnitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_unit)
        test()
    }

    private fun test() {
        val sb = StringBuffer()
        sb.append("1  format ${UtilFormat.numFormat(1, 3, 3)}\n")
        sb.append("1  format ${UtilFormat.numFormat(1, 1, 0)}\n")
        sb.append("1  format ${UtilFormat.numFormat(1, 0, 1)}\n")
        sb.append("1  format ${UtilFormat.numFormat(0.9, 0, 0)}\n")
        sb.append("1  format ${UtilFormat.numFormat(.9, 1, 0)}\n")
        sb.append("${System.currentTimeMillis()}  format ${UtilFormat.timeFormat(System.currentTimeMillis())}\n")
        sb.append("${System.currentTimeMillis()}  format ${UtilFormat.timeFormat(System.currentTimeMillis(),"yyyy-MM-dd hh:mm")}\n")
        sb.append("${System.currentTimeMillis()}  format ${UtilFormat.timeFormat(System.currentTimeMillis(),"HH:mm:ss")}\n")
        binding.unitTv.text = sb.toString()
    }
}