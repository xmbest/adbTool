package utils

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
fun remount(): String {
    return execute("remount")
}

fun saveScreen(srcPath: String, destPath: String): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val formattedDateTime = currentDateTime.format(formatter)
    shell("screencap -p $srcPath/screen_$formattedDateTime.png")
    pull("$srcPath/screen_$formattedDateTime.png", destPath)
    return shell("rm -rf $srcPath/screen_$formattedDateTime.png")
}

fun uninstall(packageName:String): String {
    return shell("pm uninstall $packageName")
}

fun install(path:String):String{
    return execute("install $path")
}

fun mkdir(path: String):String{
    return shell("mkdir -p $path")
}

fun start(packageName: String):String{
    val dumpsys = dumpsys(packageName)
    val regex = Regex(".* (.*)/(.*?) .*")
    val find = regex.find(dumpsys)
    if (find != null) {
        val mainActivity= find.groups[2]?.value?:""
        if (mainActivity.isNotBlank())
            return shell("am start ${packageName}/${packageName}$mainActivity")
    }
    //以上方式未成功尝试monkey启动应用
    return shell("monkey -p $packageName -v 1")
}

fun clear(packageName:String):String{
    return shell("pm clear $packageName")
}

fun dump(packageName: String, filter: String): String {
    if (getOsType() == "windows")
        return shell("\"pm dump $packageName | grep $filter\"")
    return shell("pm dump $packageName | grep $filter")
}

fun dumpsys(packageName: String, filter: String = ""): String {
    if (getOsType() == "windows")
        return shell("\"dumpsys package $packageName${if (filter.isNotBlank()) " | grep $filter\"" else "\""}")
    return shell("dumpsys package $packageName${if (filter.isNotBlank()) " | grep $filter" else ""}")
}

fun ps(keyWord:String,isA:Boolean):String{
    if (getOsType() == "windows")
        return shell("\"ps ${if(isA) "-A" else ""} ${if (keyWord.isNotBlank()) " | grep $keyWord\"" else "\""}")
    return shell("ps ${if(isA) "-A" else ""} ${if (keyWord.isNotBlank()) " | grep $keyWord" else ""}")
}


fun kill(pids:String){
    shell("kill $pids")
}

/**
 * @Description： 获取设备列表
 */
@OptIn(DelicateCoroutinesApi::class)
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
            GlobalScope.launch {
                root()
                remount()
            }
        }
    }

}

