package pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import components.*

@Composable
fun QuickSetting() {
    General(title = "常用功能", content = {
        ContentRow{
            Item("quick.png","测试1")
            Item("quick.png","测试1")
            Item("quick.png","测试1")
            Item("quick.png","查看当前Activity")
        }
    })
    General(title = "应用相关", color = Color.Green, height = 2, content = {
        ContentMoreRowColumn {
            ContentNRow{
                Item("quick.png","测试1")
                Item("quick.png","测试1")
                Item("quick.png","测试1")
                Item("quick.png","查看当前Activity")
            }
            ContentNRow{
                Item("quick.png","测试1")
                Item("quick.png","测试1")
                Item("quick.png","测试1")
                Item("quick.png","查看当前Activity")
            }
        }

    })
}