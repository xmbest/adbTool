package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.SimpleDialog
import theme.GOOGLE_BLUE
import theme.GOOGLE_GREEN
import theme.GOOGLE_RED
import theme.GOOGLE_YELLOW
import utils.ClipboardUtil
import utils.GenerexUtils


/**
 * 命令字生成页面
 */

//全局变量，页面切换保留数据
val text1 = mutableStateOf("")
val text2 = mutableStateOf("")
@Preview
@Composable
fun CommandGeneral() {
    val showingDialog = remember { mutableStateOf(false) }
    val dialogTitle = remember { mutableStateOf("警告") }
    val dialogTitleColor = remember { mutableStateOf(GOOGLE_RED) }
    val dialogText = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            CommandText(str = text1, hint = "输入正则表达式")
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            CommandButton("生成") {
                try {
                    text2.value = GenerexUtils.generateAll(text1.value)
                } catch (_: Exception) {
                    dialogTitle.value = "警告"
                    dialogTitleColor.value = GOOGLE_RED
                    dialogText.value = "请检查正则格式是否正确!!!"
                    showingDialog.value = true
                }
            }
            CommandButton("粘贴", backgroundColor = GOOGLE_GREEN) {
                text1.value = ClipboardUtil.getSysClipboardText()
            }
            CommandButton("复制", backgroundColor = GOOGLE_YELLOW) {
                ClipboardUtil.setSysClipboardText(text2.value)
            }
            CommandButton("清空", backgroundColor = GOOGLE_RED) {
                text1.value = ""
                text2.value = ""
            }
        }
        Row(modifier = Modifier.fillMaxWidth().weight(2f)) {
            CommandText(str = text2, hint = "结果")
        }
        if (showingDialog.value)
            SimpleDialog(showingDialog, title = dialogTitle.value, titleColor = dialogTitleColor.value, text = dialogText.value)
    }
}


@Composable
fun CommandText(str: MutableState<String>, hint: String) {
    val scroll = rememberScrollState()
    TextField(
        str.value,
        modifier = Modifier.fillMaxSize().scrollable(scroll, Orientation.Vertical).fillMaxHeight(),
        placeholder = { Text(hint) },
        trailingIcon = {
            if (str.value.isNotEmpty()) {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.fillMaxHeight().padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.Close, null, modifier = Modifier.clickable {
                        str.value = ""
                    }.hoverable(interactionSource = remember { MutableInteractionSource() }))
                }
            }
        },
        onValueChange = {
            str.value = it
        })
}

@Composable
fun CommandButton(
    str: String,
    backgroundColor: Color = GOOGLE_BLUE,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        modifier = Modifier.padding(end = 5.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Text(str, color = textColor)
    }
}


