package utils

import java.io.BufferedReader
import java.io.InputStreamReader

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
        output += line + "\n"
    }
    return output
}

/*
* 执行adb命令
* */
fun execute(cmd:String): String {
    val process = cmd.execute()
    process.waitFor()
//    println(process.text())
    return process.text()
}


/**
 * @Description： 判断是否连接设备
 * @Param： []
 * @return： boolean
 */
fun isLinkDevices(): Boolean {
    var str: String = execute("adb devices")
    str = str.replace("List of devices attached".toRegex(), "").replace("\n".toRegex(), "")
    if (str.isBlank()) {
        println("已连接设备，设备名为：$str")
        return true
    }
    println("未连接设备")
    return false
}

