package pages

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.currentToastTask
import components.showToast
import components.toastText
import config.route_left_item_color
import entity.BroadParam
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import status.currentDevice
import utils.PropertiesUtil
import utils.board
import utils.shell

val boardCustomer = mutableStateOf("")

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
        Column(modifier = Modifier.fillMaxSize()) {
            Title("自定义广播")
            Row(modifier = Modifier.fillMaxWidth().weight(1f).padding(5.dp)) {
                val scroll = rememberScrollState()
                TextField(
                    boardCustomer.value,
                    modifier = Modifier.fillMaxSize().scrollable(scroll, Orientation.Vertical).fillMaxHeight(),
                    trailingIcon = {
                        if (boardCustomer.value.isNotEmpty()) {
                            Box(
                                contentAlignment = Alignment.BottomEnd,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Button(onClick = {
                                    PropertiesUtil.setValue("boardCustomer",boardCustomer.value,"")
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
                                }, modifier = Modifier.padding(end = 5.dp)) {
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

@Composable
fun ButtonClick(str: String, click: () -> Unit) {
    Button(onClick = {
        click()
    }, modifier = Modifier.padding(start = 5.dp)) {
        Text(text = str)
    }
}