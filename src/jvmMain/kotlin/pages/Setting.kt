package pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import components.Toast
import components.currentToastTask
import components.showToast
import components.toastText
import config.route_left_background
import config.route_left_item_clicked_color
import config.route_left_item_color
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import status.*
import status.autoSync
import theme.GOOGLE_BLUE
import utils.*
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Settings() {
    val pattern = remember { Regex("^\\d+\$") }
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxSize().padding(bottom = 20.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("设置|关于", color = route_left_item_color, modifier = Modifier.clickable {

            })
        }
        Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    autoSync.value,
                    onCheckedChange = {
                        autoSync.value = it
                        if (autoSync.value)
                            ListenDeviceUtil.listenDevices()
                        PropertiesUtil.setValue("autoSync", if (autoSync.value) "1" else "0", "")
                        Log.d("autoSync value change ==> " + autoSync.value)
                    },
                    colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                )
                TooltipArea(tooltip = {
                    Text("关闭后手动点击手机图标刷新,建议开启,最小化时取消刷新")
                }) {
                    Text(text = "自动刷新",
                        color = route_left_item_color,
                        modifier = Modifier.align(Alignment.CenterVertically).clickable {
                            autoSync.value = !autoSync.value
                            if (autoSync.value)
                                ListenDeviceUtil.listenDevices()
                            PropertiesUtil.setValue("autoSync", if (autoSync.value) "1" else "0", "")
                            Log.d("autoSync value change ==> " + autoSync.value)
                        })
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    saveLog.value,
                    onCheckedChange = {
                        saveLog.value = it
                        PropertiesUtil.setValue("saveLog", if (saveLog.value) "1" else "0", "")
                        Log.d("saveLog value change ==> " + saveLog.value)
                    },
                    colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                )

                Text(text = "保存日志",
                    color = route_left_item_color,
                    modifier = Modifier.align(Alignment.CenterVertically).clickable {
                        saveLog.value = !saveLog.value
                        PropertiesUtil.setValue("saveLog", if (saveLog.value) "1" else "0", "")
                        Log.d("saveLog value change ==> " + saveLog.value)
                    })
            }
            Row(
                modifier = Modifier.padding(start = 14.dp, top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingLable("自动时长(s)")
                for (i in 5..60 step 5) {
                    SelectButton(
                        "$i",
                        if (i != checkDevicesTime.value) route_left_background else GOOGLE_BLUE,
                        if (i != checkDevicesTime.value) route_left_item_color else route_left_item_clicked_color
                    ) {
                        checkDevicesTime.value = i
                        PropertiesUtil.setValue("checkDevicesTime", "${checkDevicesTime.value}", "")
                        Log.d("checkDevicesTime value change ==> " + checkDevicesTime.value)
                    }
                }
            }
            Row(
                modifier = Modifier.padding(start = 14.dp, top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingLable("程序起始页")
                for (page in pages) {
                    SelectButton(
                        page.name,
                        if (page.index != index.value) route_left_background else GOOGLE_BLUE,
                        if (page.index != index.value) route_left_item_color else route_left_item_clicked_color
                    ) {
                        index.value = page.index
                        PropertiesUtil.setValue("index", "${index.value}", "")
                        Log.d("index value change ==> ${index.value}")
                    }
                }
            }
            if (BashUtil.split == "\\"){
                Row(
                    modifier = Modifier.padding(start = 14.dp, top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SettingLable("当前adb")
                    TextField(
                        adb.value,
                        onValueChange = { },
                        modifier = Modifier.weight(1f).height(48.dp).padding(end = 10.dp),
                        enabled = false,
                        trailingIcon = {
                            TooltipArea(tooltip = {
                                Text("切换")
                            }) {
                                Icon(
                                    painterResource(getRealLocation("folder")),
                                    null,
                                    modifier = Modifier.size(30.dp).clickable {
                                        JFileChooser().apply {
                                            dialogTitle = "切换为"
                                            fileFilter = FileNameExtensionFilter(
                                                "adb(*.exe)", "exe"
                                            )
                                            fileSelectionMode = JFileChooser.FILES_ONLY
                                            val state: Int = showOpenDialog(ComposeWindow())
                                            if (state == JFileChooser.CANCEL_OPTION) {
                                                //取消更换
                                                GlobalScope.launch {
                                                    if (showToast.value) {
                                                        delay(1000)
                                                    }
                                                    currentToastTask.value = "SettingAdbPathChangeCancel"
                                                    toastText.value = "已取消"
                                                    showToast.value = true
                                                }
                                                return@clickable
                                            }
                                            val path = selectedFile?.absolutePath ?: ""
                                            if (path.isNotBlank()) {
                                                adb.value = path
                                                PropertiesUtil.setValue("adb", adb.value, "")
                                                GlobalScope.launch {
                                                    if (showToast.value) {
                                                        delay(1000)
                                                    }
                                                    currentToastTask.value = "SettingAdbPathChangeSuccess"
                                                    toastText.value = "已更新"
                                                    showToast.value = true
                                                }
                                            } else {
                                                GlobalScope.launch {
                                                    if (showToast.value) {
                                                        delay(1000)
                                                    }
                                                    currentToastTask.value = "SettingAdbPathChangeThrow"
                                                    toastText.value = "异常"
                                                    showToast.value = true
                                                }
                                            }
                                        }
                                    },
                                    tint = route_left_item_color
                                )
                            }

                        }
                    )
                }
            }

            Row(
                modifier = Modifier.padding(start = 14.dp, top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingLable("桌面目录")
                val file = File(desktop.value)
                if (!file.exists()) {
                    desktop.value = BashUtil.workDir
                }
                TextField(
                    desktop.value,
                    onValueChange = { },
                    modifier = Modifier.weight(1f).height(48.dp).padding(end = 10.dp),
                    enabled = false,
                    trailingIcon = {
                        TooltipArea(tooltip = {
                            Text("切换目录")
                        }) {
                            Icon(
                                painterResource(getRealLocation("folder")),
                                null,
                                modifier = Modifier.size(30.dp).clickable {
                                    JFileChooser().apply {
                                        dialogTitle = "切换到"
                                        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                                        val state: Int = showOpenDialog(ComposeWindow())
                                        if (state == JFileChooser.CANCEL_OPTION) {
                                            //取消更换
                                            GlobalScope.launch {
                                                if (showToast.value) {
                                                    delay(1000)
                                                }
                                                currentToastTask.value = "SettingFilePathChangeCancel"
                                                toastText.value = "已取消"
                                                showToast.value = true
                                            }
                                            return@clickable
                                        }
                                        val path = selectedFile?.absolutePath ?: ""
                                        if (path.isNotBlank()) {
                                            desktop.value = path
                                            PropertiesUtil.setValue("desktop", desktop.value, "")
                                            GlobalScope.launch {
                                                if (showToast.value) {
                                                    delay(1000)
                                                }
                                                currentToastTask.value = "SettingFilePathChangeSuccess"
                                                toastText.value = "已更新"
                                                showToast.value = true
                                            }
                                        } else {
                                            GlobalScope.launch {
                                                if (showToast.value) {
                                                    delay(1000)
                                                }
                                                currentToastTask.value = "SettingFilePathChangeThrow"
                                                toastText.value = "异常"
                                                showToast.value = true
                                            }
                                        }
                                    }
                                },
                                tint = route_left_item_color
                            )
                        }

                    }
                )
            }
            Toast(showToast, toastText)
        }
    }
}

@Composable
fun SelectButton(str: String, backgroundColor: Color, textColor: Color, click: () -> Unit) {
    Button(
        onClick = click,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        modifier = Modifier.padding(end = 6.dp)
    ) {
        Text(str, color = textColor)
    }
}

@Composable
fun SettingLable(str: String, width: Int = 100) {
    Text(str, color = route_left_item_color, modifier = Modifier.width(width.dp))
}

