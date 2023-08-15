package pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import config.item_clicked_rounded
import config.route_left_item_color
import config.window_height
import status.autoSync
import status.checkDevicesTime
import status.index
import theme.GOOGLE_BLUE
import utils.ListenDeviceUtil

private val devicesSyncTime = mutableStateOf(checkDevicesTime)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Settings() {
    val pattern = remember { Regex("^\\d+\$") }
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = (window_height - 60).dp),
            verticalAlignment = Alignment.CenterVertically,
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
                        })
                }
            }
            Row(modifier = Modifier.padding(start = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("自动刷新时间(s): ", color = route_left_item_color)
                TextField("${devicesSyncTime.value}", onValueChange = {
                    if (it.isBlank()){
                        devicesSyncTime.value = 0L
                    }else if (it.matches(pattern)) {
                        devicesSyncTime.value = it.toLong()
                    }
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
            Row(modifier = Modifier.padding(start = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("默认打开标签: ", color = route_left_item_color)
                Text(pages[index].name)
            }
        }
    }
}

