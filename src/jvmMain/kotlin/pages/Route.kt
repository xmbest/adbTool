package pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import components.*
import config.*
import entity.Page
import status.*
import utils.getDevices
import utils.getRealLocation

val pages = listOf(
    Page("快捷功能", getRealLocation("pushpin")) { QuickSetting() },
    Page("应用管理", getRealLocation("android")) { AppManage() },
    Page("AAB工具", getRealLocation("aabtools")) { AABToolsManager() },
    Page("文件管理", getRealLocation("folder")) { FileManage() },
    Page("命令泛化", getRealLocation("code")) { CommandGeneral() },
    Page("广播模拟", getRealLocation("board")) { BoardManage() },
    Page("程序设置", getRealLocation("settings")) { Settings() }
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Route() {
    var expanded by remember {
        mutableStateOf(false)
    }

    var curPage by remember {
        mutableStateOf(index.value)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxHeight().width(route_left_width).background(route_left_background).padding(
                    start = route_left_padding_left,
                    end = route_left_padding_right,
                    top = route_left_padding_top,
                    bottom = route_left_padding_bottom
                )
            ) {
                pages.forEachIndexed { index, page ->
                    Spacer(modifier = Modifier.height(route_left_item_spacer))
                    ListItem(
                        text = {
                            Text(
                                page.name,
                                color = if (curPage == index) route_left_item_clicked_color else route_left_item_color
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(page.icon),
                                page.name,
                                tint = if (curPage != index) page.color else Color.White
                            )
                        },
                        modifier = Modifier.clip(RoundedCornerShape(route_left_item_rounded))
                            .height(route_left_item_height)
                            .clickable {
                                curPage = index
                            }.background(if (curPage == index) route_left_item_background else route_left_background)
                    )
                }
                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    ListItem(
                        text = {
                            Text(
                                if (currentDevice.value.isBlank()) "请选择设备" else currentDevice.value,
                                color = route_left_item_color,
                                maxLines = 2
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(getRealLocation("mobile")),
                                null,
                                tint = route_left_item_color,
                                modifier = Modifier.clickable {
                                    if (!autoSync.value) {
                                        getDevices()
                                    }
                                }
                            )
                        },
                        modifier = Modifier.clip(RoundedCornerShape(route_left_item_rounded))
                            .height(route_left_item_height)
                            .clickable {
                                expanded = true
                            }
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    offset = DpOffset(x = 2.dp, y = 2.dp),
                    modifier = Modifier.width(route_left_width - route_left_padding_left - route_left_padding_right)
                ) {
                    if (devicesList.size == 0) {
                        DropdownMenuItem(onClick = {

                        }) {
                            Text(text = "当前设备列表为空")
                        }
                    } else {
                        devicesList.forEach {
                            DropdownMenuItem(onClick = {
                                expanded = false
                                currentDevice.value = it
                            }) {
                                Text(text = it)
                            }
                        }
                    }
                }
            }
            Column(modifier = Modifier.fillMaxHeight().weight(1f).background(route_right_background)) {
                pages[curPage].comp()
            }
        }
        Toast(showToast, toastText, background = toastBgColor.value, textColor = toastTextColor.value)
        if (showingDialog.value) {
            SimpleDialog(
                showingDialog,
                title = title.value,
                titleColor = titleColor.value,
                text = dialogText.value,
                needRun = needRun.value,
                runnable = run.value
            )
        }
        if (showingConfirmDialog.value) {
            ConfirmDialog(
                showingConfirmDialog,
                title = title.value,
                titleColor = titleColor.value,
                text = dialogText,
                hint = hint.value,
                runnable = runBoolean.value,
                checkBox = checkBox,
                showCheckBox = showCheckBox,
                checkBoxText = checkBoxText
            )
        }
    }
}