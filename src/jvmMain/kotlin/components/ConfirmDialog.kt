package components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.PopupAlertDialogProvider.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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


@OptIn( DelicateCoroutinesApi::class, ExperimentalMaterialApi::class)
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
    height: Int = 220,
    content: @Composable (() -> Unit) = {
        val error = mutableStateOf(false)
        Column(
            modifier = Modifier.height(height.dp).width(width.dp).clip(RoundedCornerShape(5.dp))
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp).clip(RoundedCornerShape(5.dp))) {
                Text(color = titleColor, text = title, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    text.value,
                    onValueChange = { text.value = it },
                    placeholder = { Text(hint) },
                    isError = error.value,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
                            error.value = true
                            isError = true
                            errorMsg = "请正确输入内容"
                        }
                        if (!list.isNullOrEmpty()) {
                            if (list.contains(text.value.trim())) {
                                error.value = true
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
                        error.value = false
                        showingDialog.value = false
                        if (needRun) {
                            runnable!!.invoke()
                        }
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_BLUE),
                    modifier = Modifier.padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    Text(text = "确定", color = Color.White)
                }
                if (needRun) {
                    Button(
                        onClick = {
                            showingDialog.value = false
                        }, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_RED),
                        modifier = Modifier.padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                    ) {
                        Text(text = "取消", color = Color.White)
                    }
                }
            }
        }
    }
) {
    AlertDialog(onDismissRequest = {
//        showingDialog.value = false
    }, content = {
        content()
    })
}