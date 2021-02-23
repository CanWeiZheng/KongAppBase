package kong.project.base.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import kong.project.base.BuildConfig
import java.io.File
import java.security.MessageDigest
import java.util.*


/**
 * @author: Kong
 * @date: 2020/9/16
 */

object UtilComm {

    /**
     * @description md5加密
     * @param
     * @return
     * @time 2020/10/19
     */
    fun md5(string: String): String {
        try {
            val digest = MessageDigest.getInstance("MD5")
            val bytes = digest.digest(string.toByteArray())
            val sb = StringBuffer()
            bytes.forEach {
                val c = it.toInt() and 0xff
                val result = Integer.toHexString(c)
                if (result.length == 1)
                    sb.append(0.toString())
                sb.append(result)
            }
            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * @description 生成指定长度的随机数
     * @param
     * @return
     * @time 2020/11/23
     */
    fun createRandomStr(length: Int): String {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val sb = StringBuffer()
        for (i in 0 until length) {
            val number: Int = random.nextInt(62)
            sb.append(str[number])
        }
        return sb.toString()
    }
}