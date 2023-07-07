package status

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

val currentDevice = mutableStateOf("")
val devicesList = mutableStateListOf<String>()
val checkDevicesTime = mutableStateOf(2L)
val desktop = System.getProperty("user.home") + "\\Desktop"
var pathSave = desktop