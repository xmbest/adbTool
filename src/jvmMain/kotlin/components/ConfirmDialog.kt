package components

import CustomDialogProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import theme.GOOGLE_BLUE
import theme.GOOGLE_RED
import theme.GOOGLE_YELLOW


@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterialApi::class)
@Composable
fun ConfirmDialog(
    showingDialog: MutableState<Boolean>,
    title: String = "提示",
    titleColor: Color = GOOGLE_YELLOW,
    text: MutableState<String>,
    hint: String = "请输入内容",
    needRun: Boolean = false,
    runnable: (() -> Unit)? = null,
    list: List<String>? = null,
    width: Int = 320,
    height: Int = 80,
    content: @Composable (() -> Unit) = {
        Column(
            modifier = Modifier.height(height.dp).width(width.dp).clip(RoundedCornerShape(5.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    text.value,
                    onValueChange = { text.value = it },
                    placeholder = { Text(hint) },
                    modifier = Modifier.fillMaxWidth()
                )
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        var isError = false
                        var errorMsg = ""
                        if (text.value.isBlank()) {
                            isError = true
                            errorMsg = "请正确输入内容"
                        }
                        if (!list.isNullOrEmpty()) {
                            if (list.contains(text.value.trim())) {
                                errorMsg = "名称已存在"
                                isError = true
                            }
                        }
                        if (isError) {
                            if (!showToast.value) {
                                currentToastTask.value = "ConfirmDialogRenameError"
                                toastText.value = errorMsg
                                showToast.value = true
                            } else {
                                if (currentToastTask.value != "ConfirmDialogRenameError") {
                                    GlobalScope.launch {
                                        delay(1000)
                                        currentToastTask.value = "ConfirmDialogRenameError"
                                        toastText.value = errorMsg
                                        showToast.value = true
                                    }
                                }
                            }
                            return@Button
                        }
                        showingDialog.value = false
                        if (needRun) {
                            runnable!!.invoke()
                        }
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_BLUE),
                    modifier = Modifier.padding(end = 3.dp)
                ) {
                    Text(text = "确定", color = Color.White)
                }
                if (needRun) {
                    Button(
                        onClick = {
                            showingDialog.value = false
                        }, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_RED),
                        modifier = Modifier.padding(end = 3.dp)
                    ) {
                        Text(text = "取消", color = Color.White)
                    }
                }
            }
        },
        title = { Text(color = titleColor, text = title)},
        text = { content() })
}