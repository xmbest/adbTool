package pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import components.*
import config.route_left_item_color
import config.route_right_background
import config.window_width
import kotlinx.coroutines.*
import status.currentDevice
import theme.*
import utils.*
import javax.swing.JFileChooser

val appList = mutableStateListOf<String>()
val taskList = mutableStateListOf<List<String>>()
val checkedList = mutableStateListOf<Boolean>()
val checkedList1 = mutableStateListOf<Boolean>()
val checkAll = mutableStateOf(false)
val checkAll1 = mutableStateOf(false)
val checkA = mutableStateOf(true)
val appKeyword = mutableStateOf("")
val systemApp = mutableStateOf(false)
val first = mutableStateOf(true)

//进程管理/应用管理
val appManage = mutableStateOf(true)

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
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
        LaunchedEffect(""){
            if (appManage.value) {
                initAppList()
            }else{
                initTask()
            }
        }
//        if (appManage.value) {
//            if (appList.isEmpty() && first.value) {
//                initAppList()
//            }
//        } else {
//            if (taskList.isEmpty() && first.value) {
//                initTask()
//            }
//        }

        Column(
            modifier = Modifier.fillMaxSize().fillMaxHeight().background(route_right_background).padding(10.dp)
        ) {
            LazyColumn {
                stickyHeader {
                    Row(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                        if (appManage.value) {
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
                        } else {
                            Text(text = "-A",
                                color = route_left_item_color,
                                modifier = Modifier.align(Alignment.CenterVertically).clickable {
                                    checkA.value = !checkA.value
                                })
                            Checkbox(
                                checkA.value,
                                onCheckedChange = {
                                    checkA.value = it
                                    initTask()
                                },
                                colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                            )
                        }

                        TextField(
                            appKeyword.value,
                            trailingIcon = {
                                if (appKeyword.value.isNotBlank()) Icon(
                                    Icons.Default.Close,
                                    null,
                                    modifier = Modifier.width(20.dp).height(20.dp).clickable {
                                        appKeyword.value = ""
                                    },
                                    tint = route_left_item_color
                                )
                            },
                            singleLine = true,
                            placeholder = { Text("keyword") },
                            onValueChange = { appKeyword.value = it },
                            modifier = Modifier.weight(1f).height(48.dp).padding(end = 10.dp).onKeyEvent {
                                if (it.key.keyCode == Key.Enter.keyCode && it.type ==  KeyEventType.KeyUp){
                                    if (appManage.value) initAppList() else initTask()
                                    PropertiesUtil.setValue("appKeyword", appKeyword.value, "")
                                    return@onKeyEvent true
                                }
                                return@onKeyEvent false
                            }
                        )
                        Text(text = if (appManage.value) "进程管理" else "应用管理",
                            color = route_left_item_color,
                            modifier = Modifier.align(Alignment.CenterVertically).clickable {
                                appManage.value = !appManage.value
                            })
                        Checkbox(
                            appManage.value,
                            onCheckedChange = {
                                PropertiesUtil.setValue("appManage", if (appManage.value) "0" else "1", "")
                                appManage.value = it
                                initAppList()
                            },
                            colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                        )
                        if (appManage.value) {
                            Text(text = "系统",
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
                        }

                        Button(
                            onClick = {
                                if (appManage.value) initAppList() else initTask()
                                PropertiesUtil.setValue("appKeyword", appKeyword.value, "")
                            },
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Text(text = "刷新")
                        }
                        if (appManage.value) {
                            Button(onClick = {
                                val path = PathSelector.selectPath("选择安装包", JFileChooser.FILES_ONLY, "apk")
                                if (path.isNotBlank()) {
                                    CoroutineScope(Dispatchers.Default).launch {
                                        install(path)
                                        if (showToast.value) {
                                            delay(1000)
                                        }
                                        currentToastTask.value = "AppManageInstall"
                                        toastText.value = "安装成功"
                                        showToast.value = true
                                        initAppList()
                                    }
                                }
                            }, modifier = Modifier.fillMaxHeight().padding(start = 4.dp)) {
                                Text(text = "安装")
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
                                            currentToastTask.value = "AppManageLeastSelectOne"
                                        } else {
                                            if (currentToastTask.value == "AppManageLeastSelectOne")
                                                return@Button
                                            GlobalScope.launch {
                                                delay(1000)
                                                toastText.value = "至少选中一个"
                                                showToast.value = true
                                                currentToastTask.value = "AppManageLeastSelectOne"
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
                                            currentToastTask.value = "AppManageMoreUninstall"
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
                        } else {
                            Button(
                                onClick = {
                                    val stringBuilder = StringBuilder()
                                    for (i in 0 until checkedList1.size) {
                                        if (checkedList1[i]) {
                                            stringBuilder.append(taskList[i][1])
                                            stringBuilder.append(" ")
                                        }
                                    }
                                    kill(stringBuilder.toString())
                                    initTask()
                                    if (!showToast.value) {
                                        toastText.value = "命令执行完毕"
                                        currentToastTask.value = "AppManageKillAll"
                                        showToast.value = true
                                    } else {
                                        if (currentToastTask.value != "AppManageKillAll") {
                                            GlobalScope.launch {
                                                delay(1000)
                                                toastText.value = "命令执行完毕"
                                                showToast.value = true
                                                currentToastTask.value = "AppManageKillAll"
                                            }
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_RED),
                                modifier = Modifier.fillMaxHeight().padding(start = 4.dp)
                            ) {
                                Text(text = "结束", color = Color.White)
                            }
                        }
                    }
                    if (!appManage.value) {
                        TaskItem(listOf("USER", "PID", "PPID", "NAME", "RSS"), -1)
                    }
                }
                if (appManage.value) {
                    itemsIndexed(appList) { index, item ->
                        AppItem(item, index)
                    }
                } else {
                    itemsIndexed(taskList) { index, item ->
                        TaskItem(item, index)
                    }
                }
            }
        }
    }
}


@Composable
fun TaskItem(arr: List<String>, i: Int) {
    Row(
        modifier = Modifier.height(80.dp).fillMaxWidth().padding(top = 5.dp).background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.fillMaxHeight().weight(1f), verticalAlignment = Alignment.CenterVertically) {
            if (i != -1) {
                Checkbox(
                    checkedList1[i],
                    onCheckedChange = { checkedList1[i] = it },
                    colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                )
            } else {
                Checkbox(
                    checkAll1.value,
                    onCheckedChange = {
                        checkAll1.value = it
                        for (i in 0 until checkedList1.size) {
                            checkedList1[i] = it
                        }
                    },
                    colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                )
            }
            Row(modifier = Modifier.fillMaxHeight().weight(1f), verticalAlignment = Alignment.CenterVertically) {
                arr.forEach {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight().weight(1f)) {
                        Text(text = it, textAlign = TextAlign.Center)
                    }
                }
                if (i != -1) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight().weight(1f)) {
                        Button(
                            onClick = {
                                kill(arr[1])
                                initTask()
                                if (!showToast.value) {
                                    toastText.value = "命令执行完毕"
                                    currentToastTask.value = "AppManageKill"
                                    showToast.value = true
                                } else {
                                    if (currentToastTask.value != "AppManageKill") {
                                        GlobalScope.launch {
                                            delay(1000)
                                            toastText.value = "命令执行完毕"
                                            showToast.value = true
                                            currentToastTask.value = "AppManageKill"
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_RED),
                        ) {
                            Text(text = "结束", color = Color.White)
                        }
                    }
                } else {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight().weight(1f)) {
                        Text(text = "操作", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(str: String, i: Int) {
    Row(
        modifier = Modifier.height(80.dp).fillMaxWidth().padding(top = 5.dp).background(Color.White),
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
            Icon(
                painter = painterResource(getRealLocation("android")),
                null,
                tint = GOOGLE_GREEN,
                modifier = Modifier.size(40.dp)
            )
            Column(modifier = Modifier.fillMaxHeight().weight(1f).padding(start = 10.dp), verticalArrangement = Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SelectionContainer {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(SIMPLE_GRAY)) {
                                    append("包名: ")
                                }
                                withStyle(style = SpanStyle(GOOGLE_BLUE)) {
                                    append(packageName)
                                }
                            }
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SelectionContainer {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(SIMPLE_GRAY)) {
                                    append("路径: ")
                                    append(path)
                                }
                            }
                        )
                    }
                }
            }
        }
        TooltipArea(tooltip = {
            Text("详情")
        }) {
            Icon(
                painter = painterResource(getRealLocation("eye")),
                "详情",
                tint = GOOGLE_BLUE,
                modifier = Modifier.size(50.dp).padding(9.dp).clickable {
                    title.value = "应用信息"
                    titleColor.value = GOOGLE_GREEN
                    needRun.value = false
                    run.value = {}
                    GlobalScope.launch {
                        dialogText.value = dump(packageName, "version")
                        showingDialog.value = true
                    }
                })
        }
        Spacer(modifier = Modifier.width(8.dp))
        TooltipArea(tooltip = {
            Text("启动")
        }) {
            Icon(
                painter = painterResource(getRealLocation("start")),
                "启动",
                tint = GOOGLE_GREEN,
                modifier = Modifier.size(50.dp).padding(9.dp).clickable {
                    start(packageName)
                    if (!showToast.value) {
                        toastText.value = "命令已执行"
                        currentToastTask.value = "AppManageStartApp"
                        showToast.value = true
                    } else {
                        if (currentToastTask.value != "AppManageStartApp") {
                            GlobalScope.launch {
                                delay(1000)
                                toastText.value = "命令已执行"
                                showToast.value = true
                                currentToastTask.value = "AppManageStartApp"
                            }
                        }
                    }
                })
        }
        Spacer(modifier = Modifier.width(8.dp))
        TooltipArea(tooltip = {
            Text("清除")
        }) {
            Icon(
                painter = painterResource(getRealLocation("clear")),
                "清除数据",
                tint = GOOGLE_YELLOW,
                modifier = Modifier.size(50.dp).padding(9.dp).clickable {
                    title.value = "警告"
                    titleColor.value = GOOGLE_RED
                    dialogText.value = "是否清除${packageName}数据"
                    needRun.value = true
                    run.value = {
                        run.value = {}
                        needRun.value = false
                        GlobalScope.launch {
                            clear(packageName)
                            toastText.value = "${packageName}数据已清理"
                            showToast.value = true
                            currentToastTask.value = "AppManageClearAppData"
                            initAppList()
                        }
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
                    painter = painterResource(getRealLocation("delete")),
                    "卸载",
                    tint = GOOGLE_RED,
                    modifier = Modifier.size(50.dp).padding(10.dp).clickable {
                        title.value = "警告"
                        titleColor.value = GOOGLE_RED
                        dialogText.value = "是否卸载${packageName}"
                        needRun.value = true
                        run.value = {
                            run.value = {}
                            needRun.value = false
                            GlobalScope.launch {
                                uninstall(packageName)
                                toastText.value = "${packageName}已卸载"
                                showToast.value = true
                                currentToastTask.value = "AppManageOneUninstall"
                                initAppList()
                            }
                        }
                        showingDialog.value = true
                    })
            }
        }
    }
}

fun initAppList() {
    appList.clear()
    checkedList.clear()
    checkAll.value = false
    var cmd = ""
    if (isWindows)
        cmd += if (appKeyword.value.isEmpty()) "" else "\""
    cmd += "pm list packages -f"
    cmd += if (systemApp.value) "" else " -3"
    cmd += if (appKeyword.value.isEmpty()) "" else " | grep -E '${appKeyword.value}'"
    if (isWindows)
        cmd += if (appKeyword.value.isEmpty()) "" else "\""
//    val cmd = "\"pm dump * | grep -E 'Package |version|codePath'\""
    val packages = shell(cmd)
    val split = packages.trim().split("\n").filter { it.isNotBlank() }.map { it.substring(8) }
    split.forEach {
        appList.add(it)
        checkedList.add(false)
    }
}

fun initTask() {
    if (first.value) {
        first.value = false
    }
    taskList.clear()
    checkedList1.clear()
    checkAll1.value = false
    val res = ps(appKeyword.value, checkA.value)
    var split = res.trim().split("\n").filter { it.isNotBlank() }
    if (appKeyword.value.isBlank())
        split = split.subList(1, split.size)
    split.forEach {
        val contentArr = it.split(" ").filter {
            it.trim().isNotEmpty()
        }
        checkedList1.add(false)
        taskList.add(listOf(contentArr[0], contentArr[1], contentArr[2], contentArr[8], contentArr[4]))
    }
}
