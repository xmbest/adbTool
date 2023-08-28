package utils

import status.bundletool
import utils.LogUtil.Companion.flushRes
import java.io.File

object AABUtils {
    /**
     * aab转换为apks
     */
    fun AAB2Apks() {
        try {
            excAAB()
        } catch (e: Exception) {
            flushRes(e.toString())
        }
    }

    private fun excAAB() {
//        val file = File(aabFilePath.getText())
//        val apksOutput: String = (file.getParent() + "\\" + removeExtension(file.getName())).toString() + ".apks"
//        //执行生成命令
//        val cmd =
//            (((((("java -jar " + bundletool.value) + " build-apks --bundle=" + aabFilePath.getText()) + " --output=" + apksOutput + " --ks=" + keyPath.getText()).toString() + " --ks-pass=pass:" + keyPwdInput.getText()).toString() + " --ks-key-alias=" + keyAliasInput.getText()).toString() + " --key-pass=pass:" + keyPwdInput.getText()) + " && pause"
//        BashUtil.execCommand("javac")
    }
}