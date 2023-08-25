package status

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import utils.BashUtil

val appIsMinimized = mutableStateOf(false)
val currentDevice = mutableStateOf("")
val devicesList = mutableStateListOf<String>()
//自动刷新时间
var checkDevicesTime = mutableStateOf(5)
//是否自动刷新
val autoSync = mutableStateOf(true)
//软件开启默认页
val index = mutableStateOf(0)
//开启日志保存
var saveLog = mutableStateOf(false)
var desktop = mutableStateOf(BashUtil.desktop_dir)
var pathSave = desktop
val adb = mutableStateOf("adb")