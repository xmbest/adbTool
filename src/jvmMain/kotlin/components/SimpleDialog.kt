package components

import CustomDialogProvider
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.GOOGLE_BLUE
import theme.GOOGLE_RED


/**
 * 对话框属性
 */
val showingDialog = mutableStateOf(false)
val dialogTitle = mutableStateOf("警告")
val dialogTitleColor = mutableStateOf(GOOGLE_RED)
val dialogText = mutableStateOf("")
val run = mutableStateOf({})
val needRun = mutableStateOf(false)
val title = mutableStateOf("警告")
val titleColor = mutableStateOf(Color.Blue)
val showingConfirmDialog = mutableStateOf(false)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleDialog(
    showingDialog: MutableState<Boolean>,
    title: String = "警告",
    titleColor: Color = GOOGLE_RED,
    text: String = "测试",
    needRun: Boolean = false,
    runnable: (() -> Unit)? = null,
    width: Int = 320,
    height: Int = 80,
    content: @Composable (() -> Unit) = {
        Column(
            modifier = Modifier.height(height.dp).width(width.dp)
        ) {
            val scroll = rememberScrollState()
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(5.dp).verticalScroll(scroll),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectionContainer {
                    Text(color = Color.Gray, text = "   $text", fontSize = 16.sp)
                }
            }
        }
    }
) {

    AlertDialog(
        dialogProvider = CustomDialogProvider, modifier = Modifier.clip(RoundedCornerShape(5.dp)),
        onDismissRequest = {

        }, buttons = {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth().padding(bottom = 3.dp, end = 2.dp)
            ) {
                Button(
                    onClick = {
                        showingDialog.value = false
                        if (needRun) {
                            runnable!!.invoke()
                        }
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_BLUE),
                    modifier = Modifier.padding(end = 5.dp)
                ) {
                    Text(text = "确定", color = Color.White)
                }
                if (needRun) {
                    Button(
                        onClick = {
                            showingDialog.value = false
                        }, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_RED),
                        modifier = Modifier.padding(end = 5.dp)
                    ) {
                        Text(text = "取消", color = Color.White)
                    }
                }
            }
        }, text = {
            content()
        }, title = {
            Text(color = titleColor, text = title)
        })
}