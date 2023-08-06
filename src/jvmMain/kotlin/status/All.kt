package status

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import utils.BashUtils

val appIsMinimized = mutableStateOf(false)
val currentDevice = mutableStateOf("")
val devicesList = mutableStateListOf<String>()
val checkDevicesTime = mutableStateOf(5L)
val autoSync = mutableStateOf(true)
val desktop: String = BashUtils.dir
var pathSave = desktop