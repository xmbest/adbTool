package utils

import status.currentDevice
import status.devicesList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * 给String扩展 execute() 函数
 */
fun String.execute(): Process {
    val runtime = Runtime.getRuntime()
    return runtime.exec(this)
}

/**
 * 扩展Process扩展 text() 函数
 */
fun Process.text(): String {
    // 输出 Shell 执行结果
    val inputStream = this.inputStream
    val insReader = InputStreamReader(inputStream)
    val bufReader = BufferedReader(insReader)

    var output = ""
    var line: String? = ""
    while (null != line) {
        // 逐行读取shell输出，并保存到变量output
        line = bufReader.readLine()
        if (null != line)
            output += line + "\n"
    }
    return output
}

/*
* 执行adb命令
* */
//fun execute(cmd: String): String {
//    if (cmd == "adb devices") {
//        val process = cmd.execute()
////        process.waitFor()
//        return process.text()
//    } else if (currentDevice.value.isEmpty()) {
//        return "none"
//    }
//    println(cmd)
//    val process = ("adb -s ${currentDevice.value} " + cmd).execute()
////    process.waitFor()
//    return process.text()
//}


fun execute(cmd: String): String {
    if (cmd == "adb devices") {
        return BashUtils.execCommand(cmd)
    } else if (currentDevice.value.isEmpty()) {
        return ""
    }
    return BashUtils.execCommand("adb -s ${currentDevice.value} " + cmd)
}


fun shell(cmd: String): String {
    return execute("shell $cmd")
}

fun pull(srcPath: String, destPath: String): String {
    return execute("pull $srcPath $destPath")
}

fun push(srcPath: String, destPath: String): String {
    return execute("push $srcPath $destPath")
}

fun root(): String {
    return execute("root")
}

fun saveScreen(srcPath: String, destPath: String): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val formattedDateTime = currentDateTime.format(formatter)
    shell("screencap -p $srcPath/screen_$formattedDateTime.png")
    pull("$srcPath/screen_$formattedDateTime.png", destPath)
    return shell("rm -rf $srcPath/screen_$formattedDateTime.png")
}


/**
 * @Description： 获取设备列表
 */
fun getDevices() {
    val devices: String = execute("adb devices")
    devicesList.clear()
    val splitList = devices.trim().split("\n")
    if (splitList.size == 1) {
        currentDevice.value = ""
        return
    }
    //只列出活跃(device)的设备
    for (i in 1 until splitList.size) {
        val element = splitList[i]
        if (element.contains("device")) {
            val device = element.replace("device", "").trim()
            devicesList.add(device)
        }
    }
    if (devicesList.size > 0) {
        if (!devicesList.contains(currentDevice.value)){
            currentDevice.value = devicesList[0]
        }
    }

}

