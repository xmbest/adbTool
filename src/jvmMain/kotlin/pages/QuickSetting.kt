package pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import components.*
import entity.KeyMapper
import status.pathSave
import theme.GOOGLE_RED
import theme.GOOGLE_YELLOW
import utils.execute
import utils.getRealLocation
import utils.saveScreen
import utils.shell


@Composable
fun QuickSetting() {
    val keyPre = "input keyevent "
    val keyMapperList1 = listOf(
        KeyMapper(getRealLocation("task"), 187, "任务列表"),
        KeyMapper(getRealLocation("home"), 3, "回到桌面"),
        KeyMapper(getRealLocation("back"), 4, "返回上级"),
        KeyMapper(getRealLocation("power"), 26, "锁定屏幕")
    )
    val keyMapperList2 = listOf(
        KeyMapper(getRealLocation("plus"), 24, "增加音量"),
        KeyMapper(getRealLocation("minus"), 25, "减少音量"),
        KeyMapper(getRealLocation("up"), 221, "增加亮度"),
        KeyMapper(getRealLocation("down"), 220, "减少亮度"),

        )

    val keyMapperList3 = listOf(
        KeyMapper(getRealLocation("down"), 1, "显示状态栏"),
        KeyMapper(getRealLocation("up"), 2, "隐藏状态栏"),
        KeyMapper(getRealLocation("image"), 0, "截图到桌面"),
        KeyMapper(getRealLocation("settings"), 0, "进入设置")
    )
    val scroll = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column (modifier = Modifier.fillMaxSize().fillMaxHeight().verticalScroll(scroll)) {
            General(title = "按键模拟", height = 3, content = {
                ContentMoreRowColumn {
                    ContentNRow {
                        keyMapperList1.forEach {
                            Item(it.icon, it.name) {
                                shell(keyPre + it.key)
                            }
                        }
                    }
                    ContentNRow {
                        keyMapperList2.forEach {
                            Item(it.icon, it.name) {
                                shell(keyPre + it.key)
                            }
                        }
                    }
                    ContentNRow {
                        Item(keyMapperList3[0].icon, keyMapperList3[0].name) {
                            shell("service call statusbar 1")
                        }
                        Item(keyMapperList3[1].icon, keyMapperList3[1].name) {
                            shell("service call statusbar 2")
                        }
                        Item(keyMapperList3[2].icon, keyMapperList3[2].name) {
                            saveScreen("/sdcard", pathSave)
                        }
                        Item(keyMapperList3[3].icon, keyMapperList3[3].name) {
                            shell("am start  -n com.android.settings/com.android.settings.Settings")
                        }
                    }
                }
            })
            General(title = "应用相关", color = GOOGLE_RED, height = 2, content = {
                ContentMoreRowColumn {
                    ContentNRow {
                        Item(getRealLocation("file"), "测试1"){
                            execute("aapt.exe dump badging xxx.apk")
                        }
                        Item(getRealLocation("file"), "测试1")
                        Item(getRealLocation("file"), "测试1")
                        Item(getRealLocation("file"), "查看当前Activity")
                    }
                    ContentNRow {
                        Item(getRealLocation("file"), "测试1")
                        Item(getRealLocation("file"), "测试1")
                        Item(getRealLocation("file"), "测试1")
                        Item(getRealLocation("file"), "查看当前Activity")
                    }
                }
            })
            General(title = "测试功能", color = GOOGLE_YELLOW, content = {
                ContentRow {
                    Item(getRealLocation("file"), "测试1")
                    Item(getRealLocation("file"), "测试1")
                    Item(getRealLocation("file"), "测试1")
                    Item(getRealLocation("file"), "查看当前Activity")
                }
            })
        }
        Toast(showToast, toastText)
    }
}
