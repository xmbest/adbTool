package pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import components.SimpleDialog
import config.route_left_item_color
import config.route_right_background
import entity.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import status.currentDevice
import theme.GOOGLE_BLUE
import theme.GOOGLE_GREEN
import theme.GOOGLE_RED
import theme.GOOGLE_YELLOW
import utils.ClipboardUtil
import utils.shell

val appList = mutableStateListOf<String>()
val checkedList = mutableStateListOf<Boolean>()
val keyword = mutableStateOf("")
val systemApp = mutableStateOf(false)
val showingDialog = mutableStateOf(false)
val first = mutableStateOf(true)

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
        Column(
            modifier = Modifier.fillMaxSize().fillMaxHeight().background(route_right_background).padding(10.dp)
        ) {
            LazyColumn {
                stickyHeader {
                    Row(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                        TextField(
                            keyword.value,
                            trailingIcon = {
                                if (keyword.value.isNotBlank()) Icon(
                                    Icons.Default.Close, null, modifier = Modifier.width(20.dp).height(20.dp).clickable {
                                        keyword.value = ""
                                    }, tint = route_left_item_color
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
                            onCheckedChange = { systemApp.value = it },
                            colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                        )
                        Button(onClick = { initAppList() }, modifier = Modifier.fillMaxHeight()) {
                            Text(text = "刷新")
                        }
                        Button(
                            onClick = {
                                checkedList.forEach {
                                    println(it)
                                }
                            },
                            modifier = Modifier.fillMaxHeight().padding(start = 4.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_RED)
                        ) {
                            Text(text = "卸载", color = Color.White)
                        }
                    }
                }
                itemsIndexed(appList){index, item ->
                    AppItem(item,index)
                }
            }
            if (showingDialog.value)
                SimpleDialog(showingDialog)
        }

    }
}


@Composable
fun AppItem(str: String, i: Int) {
    Row(
        modifier = Modifier.height(80.dp).fillMaxWidth().padding(8.dp).background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.fillMaxWidth().weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checkedList[i],
                onCheckedChange = { checkedList[i] = it },
                colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
            )
            Icon(painter = painterResource("android.png"), null, tint = GOOGLE_GREEN, modifier = Modifier.size(40.dp))
            val index = str.lastIndexOf("=")
            val packageName = str.substring(index + 1)
            val path = str.substring(0, index)
            Column(modifier = Modifier.fillMaxHeight().weight(1f), verticalArrangement = Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("packageName:$packageName")
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(painter = painterResource("copy.png"), null, modifier = Modifier.size(15.dp).clickable {
                        ClipboardUtil.setSysClipboardText(packageName)
                    }, tint = route_left_item_color)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("path:$path")
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(painter = painterResource("copy.png"), null, modifier = Modifier.size(15.dp).clickable {
                        ClipboardUtil.setSysClipboardText(path)
                    }, tint = route_left_item_color)
                }
            }
        }
        Button(onClick = {}) {
            Icon(painter = painterResource("eye.png"), "详情", tint = Color.White, modifier = Modifier.size(16.dp))
            Text("详情", color = Color.White)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {}, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_YELLOW)) {
            Icon(
                painter = painterResource("clear.png"), "清除数据", tint = Color.White, modifier = Modifier.size(16.dp)
            )
            Text("清除", color = Color.White)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            showingDialog.value = true
        }, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_RED)) {
            Icon(painter = painterResource("delete.png"), "卸载", tint = Color.White, modifier = Modifier.size(16.dp))
            Text("卸载", color = Color.White)
        }
    }
}

fun initAppList() {
    if (first.value) {
        first.value = false;
    }
    appList.clear()
    checkedList.clear()
    var cmd = "\"pm list packages -f"
    cmd += if (systemApp.value) "" else " -3"
    cmd += if (keyword.value.isEmpty()) "" else " | grep ${keyword.value}"
    cmd += "\""
//    val cmd = "\"pm dump * | grep -E 'Package |version|codePath'\""
    val packages = shell(cmd)
    println(packages)
    val split = packages.trim().split("\n").map { it.substring(8) }
    split.forEach {
        appList.add(it)
        checkedList.add(false)
    }
}

fun getDetails(packageName: String): App? {
    val shell = shell("\"pm dump packageName | grep version\"")
    val map = extractAppInfo(shell)
    var app: App? = null
    if (map.isNotEmpty()) {
        app = App(packageName, "", map["versionCode"], map["minSdk"], map["targetSdk"], map["versionName"])
    } else {
        println("无法提取应用程序信息")
    }
    return app
}

fun extractAppInfo(input: String): Map<String, String> {
    val regex = """versionCode=(.*)\sminSdk=(.*)\stargetSdk=(.*)\s+versionName=(.*)\s""".toRegex()
    val matchResult = regex.find(input)
    val map = HashMap<String, String>()
    if (matchResult != null) {
        map["versionCode"] = matchResult.groupValues[1]
        map["minSdk"] = matchResult.groupValues[2]
        map["targetSdk"] = matchResult.groupValues[3]
        map["versionName"] = matchResult.groupValues[4]
    }
    return map
}
