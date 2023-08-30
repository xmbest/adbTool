package utils

import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import entity.AABToolsCfgBean
import kotlinx.coroutines.*
import status.bundletool
import utils.LogUtil.Companion.flushRes
import java.io.File
import java.lang.StringBuilder

object AABUtils {
    /**
     * aab转换为apks
     */
    suspend fun AAB2Apks(aabPath: String, aabToolsCfgBean: AABToolsCfgBean): String {
        return try {
            excAAB(aabPath, aabToolsCfgBean)
        } catch (e: Exception) {
            flushRes(e.toString())
            ""
        }
    }

    suspend fun Install2Phont(apksPath: String): Boolean {
        return true
    }


    private suspend fun excAAB(aabPath: String, aabToolsCfgBean: AABToolsCfgBean): String = coroutineScope {
        val file = File(aabPath)
        val apksOutput: String = (file.getParent() + "\\" + removeExtension(file.getName())) + ".apks"
        File(apksOutput).let {
            if (it.exists()) it.delete()
        }
        val sb = StringBuilder()
        sb.append("java -jar ").append(bundletool.value)
        sb.append(" build-apks --bundle=").append(aabPath)
        sb.append(" --output=").append(apksOutput)
        sb.append(" --ks=").append(aabToolsCfgBean.keyStoryPath)
        sb.append(" --ks-pass=pass:").append(aabToolsCfgBean.keyStoryPwd)
        sb.append(" --ks-key-alias=").append(aabToolsCfgBean.keyAlias)
        sb.append(" --key-pass=pass:").append(aabToolsCfgBean.keyPwd)
        return@coroutineScope try {
            BashUtil.execCommand(sb.toString())
            apksOutput
        } catch (e: Exception) {
            ""
        }
    }

    fun removeExtension(fileName: String): String {
        val lastIndexOfDot = fileName.lastIndexOf('.')
        return if (lastIndexOfDot > 0) {
            fileName.substring(0, lastIndexOfDot)
        } else {
            fileName
        }
    }
}