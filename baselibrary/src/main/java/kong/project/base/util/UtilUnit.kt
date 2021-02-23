package kong.project.base.util

import android.content.Context

/**
 * @author: Kong
 * @date: 2021/2/22
 */
object UtilUnit {
    fun dp2Px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5).toInt()

    }
}