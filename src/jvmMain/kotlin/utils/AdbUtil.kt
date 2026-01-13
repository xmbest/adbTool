package utils

import entity.BroadParam
import entity.DeviceInfo
import kotlinx.coroutines.*
import pages.deviceInfo
import status.adb
import status.currentDevice
import status.devicesList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.DecimalFormat
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
        return BashUtil.execCommand(cmd)
    } else if (currentDevice.value.isEmpty()) {
        return ""
    }
    return BashUtil.execCommand("${adb.value} -s ${currentDevice.value} " + cmd)
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

fun uninstall(packageName: String): String {
    return shell("pm uninstall $packageName")
}

fun install(path: String): String {
    return execute("install $path")
}

fun mkdir(path: String): String {
    return shell("mkdir -p $path")
}

fun touch(path: String): String {
    return shell("touch $path")
}

fun start(packageName: String): String {
    val launchActivity = getLaunchActivity(packageName)
    //未成功获取尝试monkey启动应用
    if (launchActivity.isBlank())
        return shell("monkey -p $packageName -v 1")
    return shell("am start -n $launchActivity")
}

fun getLaunchActivity(packageName: String): String {
    val launchActivity = dumpsys(packageName, "-A 1 MAIN")
    if (launchActivity.isBlank()) return ""
    val outLines = launchActivity.lines()
    if (outLines.isEmpty()) {
        return ""
    } else {
        for (value in outLines) {
            if (value.contains("$packageName/")) {
                return value.substring(
                    value.indexOf("$packageName/"), value.indexOf(" filter")
                )
            }
        }
        return ""
    }
}

fun clear(packageName: String): String {
    return shell("pm clear $packageName")
}

fun dump(packageName: String, filter: String): String {
    if (isWindows)
        return shell("\"pm dump $packageName | grep -E '$filter'\"")
    return shell("pm dump $packageName | grep -E '$filter'")
}

fun dumpsys(packageName: String, filter: String = ""): String {
    if (isWindows)
        return shell("\"dumpsys package $packageName${if (filter.isNotBlank()) " | grep -E '$filter'\"" else "\""}")
    return shell("dumpsys package $packageName${if (filter.isNotBlank()) " | grep -E '$filter'" else ""}")
}

fun ps(keyWord: String, isA: Boolean): String {
    if (isWindows)
        return shell("\"ps ${if (isA) "-A" else ""} ${if (keyWord.isNotBlank()) " | grep -E '$keyWord'\"" else "\""}")
    return shell("ps ${if (isA) "-A" else ""} ${if (keyWord.isNotBlank()) " | grep -E '$keyWord'" else ""}")
}


fun kill(pids: String) {
    shell("kill $pids")
}

fun killall(packageName: String): String {
    return shell("killall $packageName")
}

fun reboot() {
    execute("reboot")
}

fun serialno(): String {
    return execute("get-serialno")
}

fun logcatClear() {
    execute("logcat -c")
}

// 获取应用权限列表
fun getAppPermissionList(packageName: String): List<String> {
    val permission = shell("dumpsys package $packageName")
    val permissionList: ArrayList<String> = ArrayList()
    if (permission.isBlank()) return permissionList
    val outLines = permission.lines()
    for (value in outLines) {
        if (value.contains("permission.")) {
            val permissionLine = value.replace(" ", "").split(":")
            if (permissionLine.isEmpty()) {
                continue;
            }
            permissionList.add(permissionLine[0]);
        }
    }
    return permissionList;
}

//授予应用权限
fun grant(packageName: String, permission: String): String {
    return shell("pm grant $packageName $permission")
}

fun revoke(packageName: String, permission: String): String {
    return shell("pm revoke $packageName $permission")
}

fun board(action: String, params: String) {
    val str = "am broadcast -a $action $params"
    shell(str)
}


fun board(action: String, key: Int, list: List<BroadParam>?) {
    var str = "am broadcast -a com.txznet.adapter.recv --ei key_type $key --es action $action"
    if (list != null) {
        if (list.isNotEmpty()) {
            for (broadParam in list) {
                str += "${broadParam.paramType} ${broadParam.param} ${broadParam.value}"
            }
        }
    }
    shell(str)
}

fun board(action: String, key: Int, broadParam: BroadParam) {
    val str =
        "am broadcast -a com.txznet.adapter.recv --ei key_type $key --es action $action ${broadParam.paramType} ${broadParam.param} ${broadParam.value}"
    shell(str)
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
        if (!devicesList.contains(currentDevice.value)) {
            currentDevice.value = devicesList[0]
            CoroutineScope(Dispatchers.Default).launch {
                setDeviceInfo()
                root()
            }
        }
    }

}

fun getProp(key: String) :String{
    return shell("getprop $key").trim()
}

fun setDeviceInfo() {
    val deviceInfo1 = DeviceInfo()
    deviceInfo1.brand = getProp("ro.product.brand")
    deviceInfo1.device = getProp("ro.product.device")
    deviceInfo1.model = getProp("ro.product.model")
    deviceInfo1.serialNo = getProp("ro.serialno")
    deviceInfo1.cpu = getProp("ro.soc.model")
    val hard = shell("cat /proc/cpuinfo | grep Hardware").trim()
    if (hard.isNotBlank()){
        deviceInfo1.cpu += hard.trim().split(":")[1]
    }
    deviceInfo1.cpu += "(" + getProp("ro.product.cpu.abi") + "架构 "
    deviceInfo1.core = shell("cat /proc/cpuinfo | grep processor | wc -l").trim()
    deviceInfo1.cpu += deviceInfo1.core + "核心)"
    deviceInfo1.androidVersion = getProp("ro.vendor.build.version.release")
    if(deviceInfo1.androidVersion.isBlank()){
        deviceInfo1.androidVersion = getProp("ro.build.version.release")
    }
    deviceInfo1.density = shell("wm size").trim().split(":")[1].trim()
    deviceInfo1.density += "(dpi = " + shell("wm density").trim().split(":")[1].trim() + ")"
    var memoryCmd = "cat /proc/meminfo | grep MemTotal"
    if (isWindows)
        memoryCmd = "\"" + memoryCmd + "\""
    deviceInfo1.memory = shell(memoryCmd).trim().split(":")[1].replace("kB","").trim()
    deviceInfo1.memory = "${convertKBToGB(deviceInfo1.memory.toLong())}GB"
    deviceInfo1.storage = ""
    deviceInfo1.systemVersion = getProp("ro.bootimage.build.fingerprint")
    deviceInfo.value = deviceInfo1
}

fun convertKBToGB(size: Long): String {
    val df = DecimalFormat("0.00") //格式化小数
    return df.format(size.toDouble() / 1024 / 1024)
}

