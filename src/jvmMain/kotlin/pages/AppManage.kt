package pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import config.route_left_item_color
import entity.App
import status.currentDevice
import theme.GOOGLE_BLUE
import theme.GOOGLE_RED
import utils.shell

val appList = mutableStateListOf<String>()
val keyword = mutableStateOf("")
val systemApp = mutableStateOf(false)

@Composable
fun AppManage() {
    if (appList.isEmpty())
        initAppList()
    if (currentDevice.value.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("请先连接设备")
        }
    } else {
        val scroll = rememberScrollState()
        Column(modifier = Modifier.fillMaxSize().fillMaxHeight().padding(10.dp).verticalScroll(scroll)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    keyword.value,
                    trailingIcon = {
                        if (keyword.value.isNotBlank())
                            Icon(
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
                Text(
                    text = "系统应用",
                    color = route_left_item_color,
                    modifier = Modifier.align(Alignment.CenterVertically).clickable {
                        systemApp.value = !systemApp.value
                    }
                )
                Checkbox(
                    systemApp.value,
                    onCheckedChange = { systemApp.value = it },
                    colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                )
                Button(onClick = { initAppList() }, modifier = Modifier.fillMaxHeight()) {
                    Text(text = "刷新")
                }
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxHeight().padding(start = 4.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_RED)
                ) {
                    Text(text = "卸载", color = Color.White)
                }
            }
            appList.forEach {
                Text(it)
            }
        }

    }
}

fun initAppList() {
    appList.clear()
    val cmd =
        "\"pm list packages" + if (systemApp.value) "" else " -3" + if (keyword.value.isEmpty()) "" else " | grep ${keyword.value}" + "\""
    val packages = shell(cmd)
    val split = packages.trim().split("\n")
    split.forEach {
        appList.add(it.substring(8))
    }
}

fun getPath(packageName: String): App? {
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
//
//@Composable
//fun AppItem() {
//    ConstraintLayout(
//        modifier = Modifier
//            .height(100.dp)
//            .fillMaxWidth()
//            .background(Color.Yellow)
//    ) {
//        val (icon, primaryText, secondlyText, checkBox) = createRefs()
//
//        Icon(
//            imageVector = Icons.Default.AccountBox,
//            contentDescription = null,
//            modifier = Modifier.constrainAs(icon) {
//                top.linkTo(parent.top)
//                start.linkTo(parent.start, margin = 8.dp)
//                bottom.linkTo(parent.bottom)
//            })
//
//        Text(
//            "Primary Text",
//            fontSize = 25.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.constrainAs(primaryText) {
//                start.linkTo(icon.end, margin = 8.dp)
//                top.linkTo(parent.top)
//            })
//
//        Text("secondly text", modifier = Modifier.constrainAs(secondlyText) {
//            start.linkTo(primaryText.start)
//            top.linkTo(primaryText.top)
//            bottom.linkTo(parent.bottom)
//        })
//    }
//}