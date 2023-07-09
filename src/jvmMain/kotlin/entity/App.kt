package entity

import androidx.compose.runtime.mutableStateListOf

data class App(
    val packageName: String?,
    val path: String?,
    val versionCode:String?,
    val minSdk: String?,
    val targetSdk: String?,
    val versionName: String?
)

