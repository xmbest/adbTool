package pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.*
import config.route_left_item_color
import entity.BroadParam
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import status.currentDevice
import utils.board
import utils.shell

private val boardCommand = mutableStateOf("")
private val boardCustomer =
    mutableStateOf("am broadcast -a com.txznet.adapter.recv --es action ac.air.status --ei key_type 2080")

@Composable
fun BoardManage() {
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
            Column(modifier = Modifier.fillMaxSize()) {
                Title("core塞文本")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().height(48.dp).padding(start = 5.dp)
                ) {
                    TextField(
                        boardCommand.value,
                        trailingIcon = {
                            if (boardCommand.value.isNotBlank()) Icon(
                                Icons.Default.Close,
                                null,
                                modifier = Modifier.width(20.dp).height(20.dp).clickable {
                                    boardCommand.value = ""
                                },
                                tint = route_left_item_color
                            )
                        },
                        placeholder = { Text("text") },
                        onValueChange = { boardCommand.value = it },
                        modifier = Modifier.weight(1f).height(48.dp).padding(end = 5.dp)
                    )
                    Button(
                        onClick = {
                            if (boardCommand.value.isBlank()) {
                                if (!showToast.value) {
                                    currentToastTask.value = "BoardManageCommand"
                                    toastText.value = "内容不可空"
                                    showToast.value = true
                                } else {
                                    if (currentToastTask.value == "BoardManageCommand")
                                        return@Button
                                    GlobalScope.launch {
                                        delay(1000)
                                        currentToastTask.value = "BoardManageCommand"
                                        toastText.value = "内容不可空"
                                        showToast.value = true
                                    }
                                }
                                return@Button
                            } else {
                                board(
                                    "com.txznet.txz.invoke",
                                    "-d \"txznet://com.txznet.txz/comm.asr.startWithRawText?\$text\"${boardCommand.value}"
                                )
                            }
                        },
                        modifier = Modifier.width(86.dp).height(46.dp).padding(end = 5.dp)
                    ) {
                        Text(text = "发送")
                    }
                }
                Title("常用")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().height(60.dp).padding(start = 5.dp)
                ) {
                    ButtonByBoard("窗口打开/关闭", action = "txz.window.auto", key = 2400)
                    ButtonByBoard("进入倒车", "system.status", 2010, "--es", "status", "reverse.enter")
                    ButtonByBoard("退出倒车", "system.status", 2010, "--es", "status", "reverse.quit")
                    ButtonByBoard("打开空调", "ac.status", 2080, "--ez", "type", "true")
                    ButtonByBoard("关闭空调", "ac.status", 2080, "--ez", "type", "false")
                }
                Title("蓝牙")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().height(60.dp).padding(start = 5.dp)
                ) {
                    ButtonByBoard("打开", "bluetooth.open", 2040)
                    ButtonByBoard("关闭", "bluetooth.close", 2040)
                    ButtonByBoard("连接", "bluetooth.connect", 2040)
                    ButtonByBoard("断开", "bluetooth.disconnect", 2040)
                    ButtonByBoard("空闲", "bluetooth.idle", 2040)
                    ButtonByBoard("接通", "bluetooth.offhook", 2040)
                    ButtonByBoard("挂断hangup", "bluetooth.hangup", 2040)
                    ButtonByBoard("挂断reject", "bluetooth.reject", 2040)
                }
                Title("自定义")
                Row(modifier = Modifier.fillMaxWidth().weight(1f).padding(5.dp)) {
                    val scroll = rememberScrollState()
                    TextField(
                        boardCustomer.value,
                        modifier = Modifier.fillMaxSize().scrollable(scroll, Orientation.Vertical).fillMaxHeight(),
                        trailingIcon = {
                            if (boardCustomer.value.isNotEmpty()) {
                                Box(
                                    contentAlignment = Alignment.BottomEnd,
                                    modifier = Modifier.fillMaxHeight().padding(bottom = 8.dp)
                                ) {
                                    Button(onClick = {
                                        shell(boardCustomer.value)
                                        if (!showToast.value) {
                                            currentToastTask.value = "BoardManageCustomerSend"
                                            toastText.value = "发送成功"
                                            showToast.value = true
                                        } else {
                                            if (currentToastTask.value == "BoardManageCustomerSend")
                                                return@Button
                                            GlobalScope.launch {
                                                delay(1000)
                                                currentToastTask.value = "BoardManageCustomerSend"
                                                toastText.value = "发送成功"
                                                showToast.value = true
                                            }
                                        }
                                    }, modifier = Modifier.padding(5.dp)) {
                                        Text(text = "发送")
                                    }
                                }
                            }
                        },
                        onValueChange = {
                            boardCustomer.value = it
                        })
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            Toast(showToast, toastText)
        }
    }
}

@Composable
fun Title(title: String) {
    Text(text = title, color = route_left_item_color, modifier = Modifier.padding(5.dp))
}

@Composable
fun ButtonByBoard(
    str: String,
    action: String = "",
    key: Int = -1,
    paramType: String = "",
    param: String = "",
    value: String = ""
) {
    Button(onClick = {
        board(action, key, BroadParam(paramType, param, value))
    }, modifier = Modifier.padding(start = 5.dp)) {
        Text(text = str)
    }
}