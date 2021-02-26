package kong.project.base.util

import android.content.Context
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat

/**
 *
 * @author: Kong
 * @date: 2021/2/22
 */
object UtilFormat {
    private val decimalFormat = DecimalFormat()
    private val dateFormat = SimpleDateFormat()

    /**
     * dp 转 px
     */
    fun dp2Px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5).toInt()
    }

    /**
     * px 转 dp
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5).toInt()
    }

    /**
     * px转sp
     */
    fun px2sp(context: Context, spValue: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (spValue / scale + 0.5).toInt()
    }

    /**
     * sp转px
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (spValue * scale + 0.5).toInt()
    }

    /**
     * 数值格式化
     */
    fun numFormat(value: Any, int: Int = 0, decimal: Int = 0): String {
        if (value is Int || value is Long || value is Float || value is Double) {
            if (int < 0 || decimal < 0)
                throw Exception("数值格式的保留位数不能小于0")
            if (int == 0 && decimal == 0)
                return ""
            //舍弃保留后的数据，不进行四舍五入
            decimalFormat.roundingMode = RoundingMode.DOWN
            var intStr = ""
            var decimalStr = ""
            for (i in 0 until int) {
                intStr += "0"
            }
            for (i in 0 until decimal) {
                decimalStr += "0"
            }
            if (decimalStr.isNotEmpty())
                decimalStr = ".$decimalStr"
            KLog.log("format ==${intStr + decimalStr}")
            decimalFormat.applyPattern(intStr + decimalStr)
            return decimalFormat.format(value)
        } else {
            throw Exception("value不能为非数值类型")
        }
    }

    /**
     * 时间戳转时间格式
     */
    fun timeFormat(timestamp: Long, pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
        System.currentTimeMillis()
        dateFormat.applyLocalizedPattern(pattern)
        return dateFormat.format(timestamp)

    }
}