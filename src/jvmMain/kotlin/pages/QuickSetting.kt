package pages

import androidx.compose.runtime.Composable
import components.*
import config.GOOGLE_GREEN
import config.GOOGLE_RED
import config.GOOGLE_YELLOW

@Composable
fun QuickSetting() {
    General(title = "常用功能", content = {
        ContentRow{
            Item("file.png","测试1")
            Item("file.png","测试1")
            Item("file.png","测试1")
            Item("file.png","查看当前Activity")
        }
    })
    General(title = "应用相关", color = GOOGLE_RED, height = 2, content = {
        ContentMoreRowColumn {
            ContentNRow{
                Item("file.png","测试1")
                Item("file.png","测试1")
                Item("file.png","测试1")
                Item("file.png","查看当前Activity")
            }
            ContentNRow{
                Item("file.png","测试1")
                Item("file.png","测试1")
                Item("file.png","测试1")
                Item("file.png","查看当前Activity")
            }
        }
    })
    General(title = "测试功能", color = GOOGLE_YELLOW, content = {
        ContentRow{
            Item("file.png","测试1")
            Item("file.png","测试1")
            Item("file.png","测试1")
            Item("file.png","查看当前Activity")
        }
    })
}