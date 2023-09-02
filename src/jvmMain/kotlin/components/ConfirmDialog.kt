package components

import CustomDialogProvider
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import theme.GOOGLE_BLUE
import theme.GOOGLE_RED
import theme.GOOGLE_YELLOW

val checkBox = mutableStateOf(false)
val showCheckBox = mutableStateOf(false)
val checkBoxText = mutableStateOf("选择")
val runBoolean = mutableStateOf({false})
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfirmDialog(
    showingDialog: MutableState<Boolean>,
    title: String = "提示",
    titleColor: Color = GOOGLE_YELLOW,
    text: MutableState<String>,
    hint: String = "请输入内容",
    runnable: (() -> Boolean)? = null,
    width: Int = 320,
    height: Int = 150,
    checkBox:MutableState<Boolean>,
    showCheckBox:MutableState<Boolean>,
    checkBoxText:MutableState<String>,
    content: @Composable (() -> Unit) = {
        Column(
            modifier = Modifier.height(if (showCheckBox.value) height.dp else (height - (height - 100)).dp ).width(width.dp).clip(RoundedCornerShape(5.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    text.value,
                    onValueChange = { text.value = it },
                    placeholder = { Text(hint) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            if (showCheckBox.value){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Checkbox(
                        checkBox.value,
                        onCheckedChange = {
                            checkBox.value = it
                        },
                        colors = CheckboxDefaults.colors(checkedColor = GOOGLE_BLUE)
                    )
                    Text(checkBoxText.value, modifier = Modifier.clickable {
                        checkBox.value = !checkBox.value
                    })
                }
            }
        }
    }
) {
    AlertDialog(
        dialogProvider = CustomDialogProvider,
        modifier = Modifier.clip(RoundedCornerShape(5.dp)),
        onDismissRequest = {

        },
        buttons = {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp, end = 20.dp)
            ) {
                Button(
                    onClick = {
                        if (runnable == null || !runnable.invoke())
                            return@Button
                        showingDialog.value = false
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_BLUE),
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Text(text = "确定", color = Color.White)
                }
                Button(
                    onClick = {
                        showingDialog.value = false
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_RED)
                ) {
                    Text(text = "取消", color = Color.White)
                }
            }
        },
        title = { Text(color = titleColor, text = title) },
        text = { content() })
}