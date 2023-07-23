package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import config.route_left_background
import config.route_left_item_color
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import theme.GOOGLE_BLUE

/*
* 自定义Toast
* */
@Composable
fun Toast(
    showToast: MutableState<Boolean>,
    text: MutableState<String>,
    showTime: Long = 1000,
    background: Color = route_left_background,
    close:Boolean = false
) {
    if (showToast.value) {
        Box(modifier = Modifier.fillMaxSize().padding(bottom = 30.dp)) {
            Card(modifier = Modifier.height(40.dp).align(Alignment.BottomCenter)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(
                        background
                    ).padding(start = 20.dp, end = 20.dp)
                ) {
                    Text(text = text.value, color = route_left_item_color)
                    if (close){
                        Text(text = "关闭", color = GOOGLE_BLUE, modifier = Modifier.clickable {
                            showToast.value = false
                        }.align(Alignment.CenterVertically))
                    }
                }
                autoClose(showToast, showTime)
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun autoClose(showToast: MutableState<Boolean>, showTime: Long) {
    GlobalScope.launch {
        delay(showTime)
        if (showToast.value) {
            showToast.value = false
        }
    }
}