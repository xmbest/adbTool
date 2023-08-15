package status

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import utils.BashUtils

val appIsMinimized = mutableStateOf(false)
val currentDevice = mutableStateOf("")
val devicesList = mutableStateListOf<String>()
//自动刷新时间
var checkDevicesTime = 5L
//是否自动刷新
val autoSync = mutableStateOf(true)
//软件开启默认页
var index = 0
val desktop: String = BashUtils.dir
var pathSave = desktop