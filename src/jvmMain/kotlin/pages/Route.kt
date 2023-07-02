package pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import config.*
import entity.Page

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Route() {
    val pages = listOf(
        Page(0, "快捷功能", Icons.Default.Build, Color.Black) { QuickSetting() },
        Page(1, "文件管理", Icons.Default.Menu, Color.Black) { FileManage() },
        Page(2, "设置", Icons.Default.Settings, Color.Black) { Settings() },
        Page(3, "生成命令字", Icons.Default.Settings, Color.Black) { CommandGeneral() }
    )
    var curPage by remember {
        mutableStateOf(3)
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxHeight().width(route_left_width).background(route_left_background).padding(
                start = route_left_padding_left,
                end = route_left_padding_right,
                top = route_left_padding_top,
                bottom = route_left_padding_bottom
            )
        ) {
            pages.forEach {
                Spacer(modifier = Modifier.height(route_left_item_spacer))
                ListItem(
                    text = { Text(it.name, color = if (curPage == it.index) route_left_item_clicked_color else route_left_item_color)},
                    icon = { Icon(it.icon, it.name, tint = it.color) },
                    modifier = Modifier.clip(RoundedCornerShape(route_left_item_rounded)).height(route_left_item_height).clickable {
                        curPage = it.index
                    }.background(if (curPage == it.index) route_left_item_background else route_left_background)
                )
            }
        }

        Column(modifier = Modifier.fillMaxHeight().weight(1f).background(route_right_background)) {
            pages[curPage].comp()
        }
    }
}