package kong.project.base.util

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Kong on 2019/3/31.
 */
object KLog {
    private const val TAG = "KongAppBase"
    private var isShow = false
    fun showLog(show: Boolean) {
        isShow = show
    }

    fun log(msg: String) {
        if (!isShow) {
            return
        }
        val stackTrace =
            Thread.currentThread().stackTrace
        val index = 4
        val className = stackTrace[index].fileName
        var methodName = stackTrace[index].methodName
        methodName =
            className + "---" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1)
//        Log.d(TAG, methodName)
        if (msg.length > 3000) {
            val chunkCount = msg.length / 3000 // integer division
            for (i in 0..chunkCount) {
                val max = 3000 * (i + 1)
                if (max >= msg.length) {
                    Log.d(
                        TAG,
                        "chunk " + i + " of " + chunkCount + ":::" + msg.substring(3000 * i)
                    )
                } else {
                    Log.d(
                        TAG,
                        "chunk " + i + " of " + chunkCount + ":::" + msg.substring(3000 * i, max)
                    )
                }
            }
        } else {
            Log.d(TAG, msg)
        }
    }

    private val LINE_SEPARATOR = System.getProperty("line.separator")
    private fun printLine(tag: String?, isTop: Boolean) {
        if (isTop) {
            Log.d(
                tag,
                "╔═══════════════════════════════════════════════════════════════════════════════════════"
            )
        } else {
            Log.d(
                tag,
                "╚═══════════════════════════════════════════════════════════════════════════════════════"
            )
        }
    }

    fun printJson(tag: String?, msg: String, headString: String) {
        var message: String
        message = try {
            when {
                msg.startsWith("{") -> {
                    val jsonObject = JSONObject(msg)
                    jsonObject.toString(4) //最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
                }
                msg.startsWith("[") -> {
                    val jsonArray = JSONArray(msg)
                    jsonArray.toString(4)
                }
                else -> {
                    msg
                }
            }
        } catch (e: JSONException) {
            msg
        }
        printLine(tag, true)
        message = headString + LINE_SEPARATOR + message
        val lines =
            message.split(LINE_SEPARATOR.toRegex()).toTypedArray()
        for (line in lines) {
            Log.d(tag, "║ $line")
        }
        printLine(tag, false)
    }
}