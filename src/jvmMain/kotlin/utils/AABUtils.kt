package utils

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
            excAAB2Apks(aabPath, aabToolsCfgBean)
        } catch (e: Exception) {
            flushRes(e.toString())
            ""
        }
    }

    /**
     * 需要apks的路径
     *
     */
    suspend fun Install2Phont(apksPath: String): Boolean {
        return excInstallApks2Phone(apksPath)
    }

    private suspend fun excInstallApks2Phone(apksPath: String): Boolean = coroutineScope {
        val sb = StringBuilder()
        sb.append("java -jar ").append(bundletool.value)
        sb.append(" install-apks --apks=").append(apksPath)
        return@coroutineScope try {
            BashUtil.execCommand(sb.toString())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    private suspend fun excAAB2Apks(aabPath: String, aabToolsCfgBean: AABToolsCfgBean): String = coroutineScope {
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