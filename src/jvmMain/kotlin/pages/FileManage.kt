package pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
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
import config.route_left_item_color
import entity.File
import status.currentDevice
import theme.GOOGLE_BLUE
import theme.GOOGLE_GREEN
import theme.GOOGLE_RED
import theme.SIMPLE_GRAY
import utils.*
import java.text.DecimalFormat
import javax.swing.JFileChooser


val fileList = mutableStateListOf<File>()
val defaultDir = mutableStateOf("/sdcard/")

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
    }else{
        Text("文件管理", color = Color.Black)
    }
    return resultSize
}