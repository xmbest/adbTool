package pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.SimpleDialog
import components.Toast
import config.route_left_background
import config.route_left_item_color
import entity.File
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import status.currentDevice
import theme.*
import utils.*
import java.text.DecimalFormat
import javax.swing.JFileChooser


val fileList = mutableStateListOf<File>()
val defaultDir = mutableStateOf("/sdcard/")
val toastText = mutableStateOf("")
val showToast = mutableStateOf(false)
val currentToastId =  mutableStateOf(0)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileManage() {
    if (fileList.isEmpty() && defaultDir.value == "/sdcard/") {
        root()
        initFile()
    }
    if (currentDevice.value.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("请先连接设备")
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(10.dp, top = 0.dp)) {
                val back = File("", defaultDir.value, "返回上级", "", true)
                LazyColumn {
                    stickyHeader {
                        Row(modifier = Modifier.background(Color.White)) {
                            FileView(back) {
                                backParent()
                            }
                        }
                    }
                    items(fileList) {
                        FileView(it)
                    }
                }
                if (fileList.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("没有找到文件", color = route_left_item_color)
                    }
                }
            }
            Toast(showToast, toastText)
        }
    }
}

fun backParent() {
    if (defaultDir.value != "/") {
        defaultDir.value = defaultDir.value.substring(0, defaultDir.value.lastIndexOf("/"))
        defaultDir.value = defaultDir.value.substring(0, defaultDir.value.lastIndexOf("/") + 1)
        initFile()
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileView(
    file: File, runnable: () -> Unit = {
        if (file.isDir) {
            defaultDir.value += file.name
            defaultDir.value += "/"
            initFile()
        }
    }
) {
    val showingDialog = remember { mutableStateOf(false) }
    val needRun = remember { mutableStateOf(false) }
    val title = remember { mutableStateOf("警告") }
    val titleColor = remember { mutableStateOf(Color.Blue) }
    val dialogText = remember { mutableStateOf("测试") }
    val rm = remember { mutableStateOf("") }
    val run = remember {
        mutableStateOf({})
    }
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp).padding(top = 5.dp).background(Color.White).clip(
            RoundedCornerShape(5.dp)
        ).combinedClickable(onDoubleClick = { runnable() }, onClick = {}),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(getFileIcon(file.name, file.isDir)),
            "icon",
            tint = if (file.isDir) GOOGLE_BLUE else SIMPLE_GRAY,
            modifier = Modifier.size(36.dp)
        )
        Column(modifier = Modifier.padding(start = 10.dp).weight(1f)) {
            Row {
                Text(file.name)
                Text(
                    file.permission,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                    color = SIMPLE_GRAY
                )
            }
            Row {
                Text(file.date, fontSize = 12.sp, color = SIMPLE_GRAY)
                Text(
                    file.size,
                    fontSize = 12.sp,
                    color = SIMPLE_GRAY,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }
        if (file.name.isNotBlank()) {
            TooltipArea(tooltip = {
                Text("重命名")
            }){
                Icon(
                    painter = painterResource("reload.png"),
                    "icon",
                    tint = GOOGLE_BLUE,
                    modifier = Modifier.size(50.dp).clickable {
                        //todo 重命名逻辑
                    }.padding(10.dp)
                )}
            Spacer(modifier = Modifier.width(5.dp))
            TooltipArea(tooltip = {
                Text("复制")
            }){
            Icon(
                painter = painterResource("link.png"),
                "icon",
                tint = GOOGLE_BLUE,
                modifier = Modifier.size(50.dp).clickable {
                    ClipboardUtil.setSysClipboardText(defaultDir.value + file.name)
                    if (!showToast.value) {
                        showToast.value = true
                        currentToastId.value = 1
                        toastText.value = "路径复制成功"
                    } else {
                        if (currentToastId.value == 1)
                            return@clickable
                        GlobalScope.launch {
                            delay(1000)
                            showToast.value = true
                            currentToastId.value = 1
                            toastText.value = "路径复制成功"
                        }
                    }
                }.padding(10.dp)
            )}
            Spacer(modifier = Modifier.width(5.dp))
            TooltipArea(tooltip = {
                Text("上传")
            }){
            Icon(
                painter = painterResource("upload.png"),
                "icon",
                tint = GOOGLE_GREEN,
                modifier = Modifier.size(50.dp).clickable {
                    JFileChooser().apply {
                        showOpenDialog(ComposeWindow())
                        val path = selectedFile?.absolutePath ?: ""
                        if (path.isNotBlank()) {
                            val start =
                                if (path.contains("\\")) path.lastIndexOf("\\") + 1 else path.lastIndexOf("/") + 1
                            val path1 = path.substring(start, path.length)
                            title.value = "警告"
                            titleColor.value = GOOGLE_RED
                            dialogText.value = "是否把文件$path1 push到 ${file.name}"
                            needRun.value = true
                            run.value = {
                                GlobalScope.launch {
                                    push(path, defaultDir.value + file.name)
                                    initFile()
                                }
                                if (!showToast.value) {
                                    showToast.value = true
                                    currentToastId.value = 2
                                    toastText.value = "文件push成功"
                                    run.value = {}
                                }
                            }
                            showingDialog.value = true
                        }
                    }
                }.padding(10.dp)
            )
        }
            Spacer(modifier = Modifier.width(5.dp))
            TooltipArea(tooltip = {
                Text("下载")
            }){
            Icon(
                painter = painterResource("download.png"),
                "icon",
                tint = GOOGLE_YELLOW,
                modifier = Modifier.size(50.dp).clickable {
                    JFileChooser().apply {
                        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                        showOpenDialog(ComposeWindow())
                        val path = selectedFile?.absolutePath ?: ""
                        if (path.isNotBlank()) {
                            GlobalScope.launch {
                                val line = if (path.contains("\\")) "\\" else "/"
                                pull("${defaultDir.value}${file.name}", "$path$line${file.name}")
                            }
                            if (!showToast.value) {
                                showToast.value = true
                                currentToastId.value = 3
                                toastText.value = "文件已保存到$path"
                            } else {
                                if (currentToastId.value == 3)
                                    return@clickable
                                GlobalScope.launch {
                                    delay(1000)
                                    showToast.value = true
                                    currentToastId.value = 3
                                    toastText.value = "文件已保存到$path"
                                }
                            }
                        }
                    }
                }.padding(10.dp)
            )
        }
            Spacer(modifier = Modifier.width(5.dp))
            TooltipArea(tooltip = {
                Text("删除")
            }) {
                Icon(
                    painter = painterResource("delete.png"),
                    "icon",
                    tint = GOOGLE_RED,
                    modifier = Modifier.size(50.dp).clickable {
                        title.value = "警告"
                        titleColor.value = GOOGLE_RED
                        dialogText.value = "是否删除${file.name}"
                        rm.value = defaultDir.value + file.name
                        needRun.value = true
                        run.value = {
                            GlobalScope.launch {
                                shell("rm -rf '${rm.value}'")
                                initFile()
                            }
                            if (!showToast.value) {
                                showToast.value = true
                                currentToastId.value = 3
                                toastText.value = "${rm.value}已删除"
                                run.value = {}
                            }
                        }
                        showingDialog.value = true
                    }.padding(10.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
            }
        } else {
            TooltipArea(tooltip = {
                Text("上传")
            }){
                Icon(painter = painterResource("upload.png"), null, modifier = Modifier.size(50.dp).clickable {
                    JFileChooser().apply {
                        showOpenDialog(ComposeWindow())
                        val path = selectedFile?.absolutePath ?: ""
                        if (path.isNotBlank()) {
                            val start =
                                if (path.contains("\\")) path.lastIndexOf("\\") + 1 else path.lastIndexOf("/") + 1
                            val path1 = path.substring(start, path.length)
                            title.value = "警告"
                            titleColor.value = GOOGLE_RED
                            dialogText.value = "是否把文件$path1 push到 ${defaultDir.value}"
                            needRun.value = true
                            run.value = {
                                GlobalScope.launch {
                                    push(path,defaultDir.value + path1)
                                    initFile()
                                }
                                if (!showToast.value) {
                                    showToast.value = true
                                    currentToastId.value = 2
                                    toastText.value = "文件push成功"
                                    run.value = {}
                                }
                            }
                            showingDialog.value = true
                        }
                    }
                }.padding(10.dp), tint = GOOGLE_BLUE)
            }
            Spacer(modifier = Modifier.width(10.dp))
            TooltipArea(tooltip = {
                Text("返回")
            }) {
                Icon(painter = painterResource("back.png"), null, modifier = Modifier.size(50.dp).clickable {
                    backParent()
                }.padding(10.dp), tint = GOOGLE_BLUE)
            }
            Spacer(modifier = Modifier.width(10.dp))
            TooltipArea(tooltip = {
                Text("刷新")
            }) {
                Icon(painter = painterResource("sync.png"), null, modifier = Modifier.size(50.dp).clickable {
                    initFile()
                }.padding(10.dp), tint = GOOGLE_BLUE)
            }
            Spacer(modifier = Modifier.width(15.dp))
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
}

fun initFile() {
    fileList.clear()
    val res = shell("ls -l ${defaultDir.value}")
    var arr = res.trim().split("\n")
    arr = arr.subList(1, arr.size)
    arr.forEach {
        val contentArr = it.split(" ").filter {
            it.trim().isNotEmpty()
        }
        val permission = contentArr[0].substring(1).replace("-".toRegex(), "")
        val size = contentArr[4]
        val date = "${contentArr[5]}  ${contentArr[6]}".substring(2)
        val isDir = !contentArr[0].startsWith("-")
        val name = contentArr[7]
        fileList.add(File(name, permission, setSize(size), date, isDir))
    }
}


fun setSize(oldSize: String): String {
    var size = 0
    size = try {
        oldSize.toInt()
    } catch (e: Exception) {
        NumberValueUtil.getStrNumber(oldSize)
    }
    //获取到的size为：1705230
    val GB = 1024 * 1024 * 1024 //定义GB的计算常量
    val MB = 1024 * 1024 //定义MB的计算常量
    val KB = 1024 //定义KB的计算常量
    val df = DecimalFormat("0.00") //格式化小数
    var resultSize = ""
    resultSize = if (size / GB >= 1) {
        //如果当前Byte的值大于等于1GB
        df.format((size / GB.toFloat()).toDouble()) + "GB   "
    } else if (size / MB >= 1) {
        //如果当前Byte的值大于等于1MB
        df.format((size / MB.toFloat()).toDouble()) + "MB   "
    } else if (size / KB >= 1) {
        //如果当前Byte的值大于等于1KB
        df.format((size / KB.toFloat()).toDouble()) + "KB   "
    } else {
        size.toString() + "B   "
    }
    return resultSize
}

fun getFileIcon(fileName: String, isDir: Boolean): String {
    return if (isDir) "folder.png"
    else if (fileName.endsWith(".apk"))
        "android.png"
    else if (fileName.endsWith(".jar"))
        "java.png"
    else if (fileName.endsWith(".json"))
        "json.png"
    else if (fileName.endsWith(".so"))
        "dependency.png"
    else if (fileName.endsWith(".cfg") || fileName.endsWith(".conf"))
        "settings.png"
    else if (fileName.endsWith(".txt") || fileName.endsWith(".xml"))
        "file-text.png"
    else if (fileName.endsWith(".png") || fileName.endsWith(".jpg"))
        "file-image.png"
    else if (fileName.endsWith(".zip") || fileName.endsWith(".tar") || fileName.endsWith(".gz") || fileName.endsWith(".7z"))
        "file-zip.png"
    else if (fileName.endsWith(".mp3") || fileName.endsWith(".wav") || fileName.endsWith(".rf"))
        "music.png"
    else
        "file.png"
}