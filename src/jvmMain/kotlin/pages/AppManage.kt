package pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import components.SimpleDialog
import components.Toast
import config.route_left_item_color
import config.route_right_background
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import status.currentDevice
import theme.GOOGLE_BLUE
import theme.GOOGLE_GREEN
import theme.GOOGLE_RED
import theme.GOOGLE_YELLOW
import utils.*
import java.util.Arrays

val appList = mutableStateListOf<String>()
val checkedList = mutableStateListOf<Boolean>()
val checkAll = mutableStateOf(false)
val keyword = mutableStateOf("")
val systemApp = mutableStateOf(false)
val showingDialog = mutableStateOf(false)
val first = mutableStateOf(true)

val needRun = mutableStateOf(false)
val title = mutableStateOf("警告")
val titleColor = mutableStateOf(Color.Blue)
val dialogText = mutableStateOf("测试")
val rm = mutableStateOf("")
val run = mutableStateOf({})

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppManage() {
    if (currentDevice.value.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("请先连接设备")
        }
    } else {
        if (appList.isEmpty() && first.value) {
            initAppList()
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().fillMaxHeight().background(route_right_background).padding(10.dp)
            ) {
                LazyColumn {
                    stickyHeader {
                        Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(start = 8.dp)) {
                            Checkbox(
                                checkAll.value,
                                onCheckedChange = {
                                    checkAll.value = it
                                    for (i in 0 until checkedList.size) {
                                        checkedList[i] = it
                                    }
                                },
                                colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                            )
                            TextField(
                                keyword.value,
                                trailingIcon = {
                                    if (keyword.value.isNotBlank()) Icon(
                                        Icons.Default.Close,
                                        null,
                                        modifier = Modifier.width(20.dp).height(20.dp).clickable {
                                            keyword.value = ""
                                        },
                                        tint = route_left_item_color
                                    )
                                },
                                placeholder = { Text("keyword") },
                                onValueChange = { keyword.value = it },
                                modifier = Modifier.weight(1f).height(48.dp).padding(end = 10.dp)
                            )
                            Text(text = "系统应用",
                                color = route_left_item_color,
                                modifier = Modifier.align(Alignment.CenterVertically).clickable {
                                    systemApp.value = !systemApp.value
                                })
                            Checkbox(
                                systemApp.value,
                                onCheckedChange = {
                                    systemApp.value = it
                                    initAppList()
                                },
                                colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                            )
                            Button(onClick = { initAppList() }, modifier = Modifier.fillMaxHeight()) {
                                Text(text = "刷新")
                            }
                            Button(
                                onClick = {
                                    val list = ArrayList<String>()
                                    checkedList.forEachIndexed { index, b ->
                                        if (b) {
                                            val index1 = appList[index].lastIndexOf("=")
                                            val packageName = appList[index].substring(index1 + 1)
                                            list.add(packageName)
                                        }
                                    }
                                    if (list.isEmpty() || list.size == 0) {
                                        if (!showToast.value) {
                                            toastText.value = "至少选中一个"
                                            showToast.value = true
                                            currentToastId.value = 6
                                        } else {
                                            if (currentToastId.value == 6)
                                                return@Button
                                            GlobalScope.launch {
                                                delay(1000)
                                                toastText.value = "至少选中一个"
                                                showToast.value = true
                                                currentToastId.value = 6
                                            }
                                        }
                                        return@Button
                                    }
                                    var str = ""
                                    list.forEach {
                                        str += "    \n$it"
                                    }
                                    title.value = "警告"
                                    titleColor.value = GOOGLE_RED
                                    dialogText.value = "是否卸载选中应用$str"
                                    needRun.value = true
                                    run.value = {
                                        GlobalScope.launch {
                                            list.forEach {
                                                uninstall(it)
                                            }
                                            toastText.value = "应用已卸载"
                                            showToast.value = true
                                            currentToastId.value = 6
                                            initAppList()
                                        }
                                        run.value = {}
                                        needRun.value = false
                                    }
                                    showingDialog.value = true
                                },
                                modifier = Modifier.fillMaxHeight().padding(start = 4.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_RED)
                            ) {
                                Text(text = "卸载", color = Color.White)
                            }
                        }
                    }
                    itemsIndexed(appList) { index, item ->
                        AppItem(item, index)
                    }
                }
                if (showingDialog.value)
                    SimpleDialog(showingDialog)
            }
            Toast(showToast, toastText)
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(str: String, i: Int) {
    Row(
        modifier = Modifier.height(80.dp).fillMaxWidth().padding(8.dp).background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val index = str.lastIndexOf("=")
        val packageName = str.substring(index + 1)
        val path = str.substring(0, index)
        Row(modifier = Modifier.fillMaxHeight().weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checkedList[i],
                onCheckedChange = { checkedList[i] = it },
                colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
            )
            Icon(painter = painterResource("android.png"), null, tint = GOOGLE_GREEN, modifier = Modifier.size(40.dp))
            Column(modifier = Modifier.fillMaxHeight().weight(1f), verticalArrangement = Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("包名:$packageName")
                    Spacer(modifier = Modifier.width(5.dp))
                    TooltipArea(tooltip = {
                        Text("复制")
                    }) {
                        Icon(painter = painterResource("copy.png"), null, modifier = Modifier.size(15.dp).clickable {
                            ClipboardUtil.setSysClipboardText(packageName)
                            if (!showToast.value) {
                                toastText.value = "包名已复制"
                                showToast.value = true
                                currentToastId.value = 1
                            } else {
                                if (currentToastId.value == 1)
                                    return@clickable
                                GlobalScope.launch {
                                    delay(1000)
                                    toastText.value = "包名已复制"
                                    showToast.value = true
                                    currentToastId.value = 1
                                }
                            }
                        }, tint = route_left_item_color)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("路径:$path", modifier = Modifier)
                    Spacer(modifier = Modifier.width(5.dp))
                    TooltipArea(tooltip = {
                        Text("复制")
                    }) {
                        Icon(painter = painterResource("copy.png"), null, modifier = Modifier.size(15.dp).clickable {
                            ClipboardUtil.setSysClipboardText(path)
                            if (!showToast.value) {
                                toastText.value = "路径已复制"
                                showToast.value = true
                                currentToastId.value = 2
                            } else {
                                if (currentToastId.value == 2)
                                    return@clickable
                                GlobalScope.launch {
                                    delay(1000)
                                    toastText.value = "路径已复制"
                                    showToast.value = true
                                    currentToastId.value = 2
                                }
                            }
                        }, tint = route_left_item_color)
                    }
                }
            }
        }
        TooltipArea(tooltip = {
            Text("详情")
        }) {
            Icon(
                painter = painterResource("eye.png"),
                "详情",
                tint = GOOGLE_BLUE,
                modifier = Modifier.size(50.dp).padding(10.dp).clickable {
                    GlobalScope.launch {
                        dialogText.value = getInfo(packageName)
                        showingDialog.value = true
                    }
                    title.value = "应用信息"
                    titleColor.value = GOOGLE_GREEN
                    needRun.value = false
                    run.value = {}
                })
        }
        Spacer(modifier = Modifier.width(8.dp))
        TooltipArea(tooltip = {
            Text("清除")
        }) {
            Icon(
                painter = painterResource("clear.png"),
                "清除数据",
                tint = GOOGLE_YELLOW,
                modifier = Modifier.size(50.dp).padding(10.dp).clickable {
                    title.value = "警告"
                    titleColor.value = GOOGLE_RED
                    dialogText.value = "是否清除${packageName}数据"
                    needRun.value = true
                    run.value = {
                        GlobalScope.launch {
                            clear(packageName)
                            toastText.value = "${packageName}数据已清理"
                            showToast.value = true
                            currentToastId.value = 4
                            initAppList()
                        }
                        run.value = {}
                        needRun.value = false
                    }
                    showingDialog.value = true
                }
            )
        }
        if (!systemApp.value) {
            Spacer(modifier = Modifier.width(8.dp))
            TooltipArea(tooltip = {
                Text("卸载")
            }) {
                Icon(
                    painter = painterResource("delete.png"),
                    "卸载",
                    tint = GOOGLE_RED,
                    modifier = Modifier.size(50.dp).padding(10.dp).clickable {
                        title.value = "警告"
                        titleColor.value = GOOGLE_RED
                        dialogText.value = "是否卸载${packageName}"
                        needRun.value = true
                        run.value = {
                            GlobalScope.launch {
                                uninstall(packageName)
                                toastText.value = "${packageName}已卸载"
                                showToast.value = true
                                currentToastId.value = 5
                                initAppList()
                            }
                            run.value = {}
                            needRun.value = false
                        }
                        showingDialog.value = true
                    })
            }
        }
    }
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
}

fun initAppList() {
    if (first.value) {
        first.value = false
    }
    appList.clear()
    checkedList.clear()
    var cmd = ""
    if (getOsType() == "windows")
        cmd += if (keyword.value.isEmpty()) "" else "\""
    cmd += "pm list packages -f"
    cmd += if (systemApp.value) "" else " -3"
    cmd += if (keyword.value.isEmpty()) "" else " | grep ${keyword.value}"
    if (getOsType() == "windows")
        cmd += if (keyword.value.isEmpty()) "" else "\""
//    val cmd = "\"pm dump * | grep -E 'Package |version|codePath'\""
    val packages = shell(cmd)
    val split = packages.trim().split("\n").filter { it.isNotBlank() }.map { it.substring(8) }
    split.forEach {
        appList.add(it)
        checkedList.add(false)
    }
}


fun getInfo(packageName: String): String {
    if (getOsType() == "windows")
        return shell("\"pm dump $packageName | grep version\"")
    return shell("pm dump $packageName | grep version")
}
