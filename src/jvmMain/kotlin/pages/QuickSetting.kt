package pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import components.*
import config.route_left_item_color
import entity.DeviceInfo
import entity.KeyMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import status.pathSave
import theme.GOOGLE_BLUE
import theme.GOOGLE_GREEN
import theme.GOOGLE_RED
import utils.*

val packageName = mutableStateOf("")
val quickSettingKeyword = mutableStateOf("")
val deviceInfo = mutableStateOf(DeviceInfo())


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuickSetting() {
    var expanded by remember {
        mutableStateOf(false)
    }
    val appList = mutableStateListOf<String>()
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

    val keyMapperList4 = listOf(
        KeyMapper(getRealLocation("eye"), 1, "查看当前Activity"),
        KeyMapper(getRealLocation("delete"), 2, "清理logcat缓存"),
        KeyMapper(getRealLocation("android"), 0, "挂载设备"),
        KeyMapper(getRealLocation("sync"), 0, "重启设备")
    )
    val scroll = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().fillMaxHeight().verticalScroll(scroll)) {

        General(title = "系统信息", height = 2, color = GOOGLE_GREEN) {
            Row(modifier = Modifier.fillMaxSize().padding(start = 20.dp, top = 20.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    SelectionContainer {
                        Text(
                            "${deviceInfo.value.brand} ${deviceInfo.value.device} \n" +
                                    "安卓版本: ${deviceInfo.value.androidVersion} \n" +
                                    "系统版本: ${deviceInfo.value.sdkVersion} \n" +
                                    "代号: ${deviceInfo.value.model} \n" +
                                    "处理器: ${deviceInfo.value.cpu} \n" +
                                    "序列号: ${deviceInfo.value.serialNo} \n" +
                                    "分辨率: ${deviceInfo.value.density} \n" +
                                    "运行内存: ${deviceInfo.value.memory} \n"
                        )
                    }

                }
            }

        }

        General(title = "按键模拟", height = 4, content = {
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
                        saveScreen("/sdcard", pathSave.value)
                    }
                    Item(keyMapperList3[3].icon, keyMapperList3[3].name) {
                        shell("am start  -n com.android.settings/com.android.settings.Settings")
                    }
                }
                ContentNRow {
                    Item(keyMapperList4[0].icon, keyMapperList4[0].name, false) {
                        CoroutineScope(Dispatchers.Default).launch {
                            val cmd =
                                if (BashUtil.split == "\\") "\"dumpsys window | grep mCurrentFocus\"" else "dumpsys window | grep mCurrentFocus"
                            val res = shell(cmd)
                            title.value = "current activity: "
                            titleColor.value = GOOGLE_GREEN
                            dialogText.value = res
                            run.value = {}
                            needRun.value = false
                            showingDialog.value = true
                        }
                        ""
                    }
                    Item(keyMapperList4[1].icon, keyMapperList4[1].name, false) {
                        title.value = "警告"
                        titleColor.value = GOOGLE_RED
                        dialogText.value = "是否清理logcat缓存"
                        needRun.value = true
                        run.value = {
                            run.value = {}
                            CoroutineScope(Dispatchers.Default).launch {
                                logcatClear()
                                if (showToast.value) {
                                    delay(1000)
                                }
                                currentToastTask.value = "QuickSettingReboot"
                                toastText.value = "logcat缓存清理中..."
                                showToast.value = true
                                needRun.value = false
                            }
                        }
                        showingDialog.value = true
                        ""
                    }
                    Item(keyMapperList4[2].icon, keyMapperList4[2].name, false) {
                        title.value = "警告"
                        titleColor.value = GOOGLE_RED
                        dialogText.value = "是否挂载设备remount"
                        needRun.value = true
                        run.value = {
                            run.value = {}
                            CoroutineScope(Dispatchers.Default).launch {
                                remount()
                                if (showToast.value) {
                                    delay(1000)
                                }
                                currentToastTask.value = "QuickSettingRemount"
                                toastText.value = "remount..."
                                showToast.value = true
                                needRun.value = false
                            }
                        }
                        showingDialog.value = true
                        ""
                    }
                    Item(keyMapperList4[3].icon, keyMapperList4[3].name, false) {
                        title.value = "警告"
                        titleColor.value = GOOGLE_RED
                        dialogText.value = "是否重启设备"
                        needRun.value = true
                        run.value = {
                            run.value = {}
                            CoroutineScope(Dispatchers.Default).launch {
                                reboot()
                                if (showToast.value) {
                                    delay(1000)
                                }
                                currentToastTask.value = "QuickSettingReboot"
                                toastText.value = "设备重启中..."
                                showToast.value = true
                            }
                        }
                        showingDialog.value = true
                        ""
                    }
                }
            }
        })
        General(title = "应用相关", color = GOOGLE_RED, height = 2, content = {
            ContentMoreRowColumn {
                ContentNRow {
                    Item(getRealLocation("start"), "启动应用") {
                        applicationManager {
                            start(packageName.value)
                        }
                    }
                    Item(getRealLocation("power"), "停止运行", false) {
                        applicationManager {
                            title.value = "警告"
                            titleColor.value = GOOGLE_RED
                            dialogText.value = "是否停止运行\"${packageName.value}\""
                            needRun.value = true
                            run.value = {
                                run.value = {}
                                needRun.value = false
                                CoroutineScope(Dispatchers.Default).launch {
                                    killall(packageName.value)
                                    toastText.value = "${packageName.value}已停止"
                                    showToast.value = true
                                    currentToastTask.value = "ApplicationManageStop"
                                }
                            }
                            showingDialog.value = true
                            ""
                        }
                    }
                    Item(getRealLocation("clear"), "清除数据", false) {
                        applicationManager {
                            title.value = "警告"
                            titleColor.value = GOOGLE_RED
                            dialogText.value = "是否清除\"${packageName.value}\"数据"
                            needRun.value = true
                            run.value = {
                                run.value = {}
                                needRun.value = false
                                CoroutineScope(Dispatchers.Default).launch {
                                    clear(packageName.value)
                                    toastText.value = "${packageName.value}数据已清除"
                                    showToast.value = true
                                    currentToastTask.value = "ApplicationManageClear"
                                }
                            }
                            showingDialog.value = true
                            ""
                        }
                    }
                    Item(getRealLocation("delete"), "卸载应用", false) {
                        applicationManager {
                            title.value = "警告"
                            titleColor.value = GOOGLE_RED
                            dialogText.value = "是否卸载\"${packageName.value}\"程序"
                            needRun.value = true
                            run.value = {
                                run.value = {}
                                needRun.value = false
                                CoroutineScope(Dispatchers.Default).launch {
                                    uninstall(packageName.value)
                                    toastText.value = "${packageName.value}程序已卸载"
                                    showToast.value = true
                                    packageName.value = ""
                                    currentToastTask.value = "ApplicationManageUnInstall"
                                }
                            }
                            showingDialog.value = true
                            ""
                        }
                    }
                }
                ContentNRow {
                    Item(getRealLocation("go"), "授予所有权限", false) {
                        applicationManager {
                            title.value = "警告"
                            titleColor.value = GOOGLE_RED
                            dialogText.value = "是否授予\"${packageName.value}\"所有权限"
                            needRun.value = true
                            run.value = {
                                run.value = {}
                                needRun.value = false
                                CoroutineScope(Dispatchers.Default).launch {
                                    getAppPermissionList(packageName.value).forEach {
                                        grant(packageName.value, it)
                                    }
                                    toastText.value = "${packageName.value}已授予所有权限"
                                    showToast.value = true
                                    currentToastTask.value = "ApplicationManageGrant"
                                }
                            }
                            showingDialog.value = true
                            ""
                        }
                    }

                    Item(getRealLocation("back"), "撤销所有权限", false) {
                        applicationManager {
                            title.value = "警告"
                            titleColor.value = GOOGLE_RED
                            dialogText.value = "是否撤销\"${packageName.value}\"所有权限"
                            needRun.value = true
                            run.value = {
                                run.value = {}
                                needRun.value = false
                                CoroutineScope(Dispatchers.Default).launch {
                                    getAppPermissionList(packageName.value).forEach {
                                        revoke(packageName.value, it)
                                    }
                                    toastText.value = "${packageName.value}已撤销所有权限"
                                    showToast.value = true
                                    currentToastTask.value = "ApplicationManageRevoke"
                                }
                            }
                            showingDialog.value = true
                            ""
                        }
                    }

                    Item(getRealLocation("eye"), "查看应用信息", false) {
                        applicationManager {
                            title.value = "应用信息"
                            titleColor.value = GOOGLE_GREEN
                            needRun.value = false
                            run.value = {}
                            CoroutineScope(Dispatchers.Default).launch {
                                dialogText.value = dump(packageName.value, "version")
                                showingDialog.value = true
                            }
                            ""
                        }
                    }
                    Item(getRealLocation("save"), "保存程序到电脑", false) {
                        applicationManager {
                            val path = shell("pm path ${packageName.value}").trim().substring(8)
                            val path1 = PathSelector.selectDir("保存到")
                            if (path1.isNotBlank()) {
                                CoroutineScope(Dispatchers.Default).launch {
                                    pull(path, path1)
                                    toastText.value = "文件已保存至$path1"
                                    showToast.value = true
                                    currentToastTask.value = "ApplicationManageSave"
                                }
                            }
                            ""
                        }

                    }
                }
            }
        }, topRight = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                ListItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                if (packageName.value.isBlank()) "请选择应用" else packageName.value,
                                color = GOOGLE_BLUE,
                                maxLines = 2,
                                textAlign = TextAlign.End,
                                modifier = Modifier.clickable {
                                    val newList = syncAppList(quickSettingKeyword.value)
                                    appList.clear()
                                    appList.addAll(newList)
                                    expanded = true
                                }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {
                                    expanded = false
                                },
                                offset = DpOffset(x = 260.dp, y = 2.dp)
                            ) {
                                if (appList.size == 0) {
                                    DropdownMenuItem(onClick = {

                                    }) {
                                        Text(text = "请选择应用")
                                    }
                                } else {
                                    Row {
                                        TextField(
                                            quickSettingKeyword.value,
                                            trailingIcon = {
                                                if (quickSettingKeyword.value.isNotBlank()) Icon(
                                                    Icons.Default.Close,
                                                    null,
                                                    modifier = Modifier.width(20.dp).height(20.dp).clickable {
                                                        quickSettingKeyword.value = ""
                                                        val newList = syncAppList(quickSettingKeyword.value)
                                                        appList.clear()
                                                        appList.addAll(newList)
                                                    },
                                                    tint = route_left_item_color
                                                )
                                            },
                                            placeholder = { Text("keyword") },
                                            onValueChange = {
                                                quickSettingKeyword.value = it
                                                val newList = syncAppList(quickSettingKeyword.value)
                                                appList.clear()
                                                appList.addAll(newList)
                                            },
                                            modifier = Modifier.weight(1f).height(48.dp)
                                                .padding(end = 10.dp, start = 10.dp)
                                        )
                                    }
                                    appList.forEach {
                                        DropdownMenuItem(onClick = {
                                            expanded = false
                                            packageName.value = it
                                        }) {
                                            Text(text = it)
                                        }
                                    }
                                }
                            }
                        }

                    },
                    modifier = Modifier.width(480.dp)
                )
            }
        })
    }
}

fun applicationManager(runnable: () -> String): String {
    return if (packageName.value.isBlank()) {
        CoroutineScope(Dispatchers.Default).launch {
            if (showToast.value) {
                delay(1000)
            }
            currentToastTask.value = "ApplicationManager"
            toastText.value = "请选择应用后重试"
            showToast.value = true
        }
        ""
    } else {
        runnable.invoke()
    }
}

fun syncAppList(keyWord: String = ""): List<String> {
    val appList = ArrayList<String>()
    var cmd = "pm list packages -f"
    if (keyWord.isNotBlank()) {
        cmd += " | grep $keyWord"
    }
    val packages = shell(cmd)
    val split = packages.trim().split("\n").filter { it.isNotBlank() }.map { it.substring(8) }
    split.forEach {
        val index = it.lastIndexOf("=")
        val packageName = it.substring(index + 1)
        appList.add(packageName)
    }
    return appList
}
