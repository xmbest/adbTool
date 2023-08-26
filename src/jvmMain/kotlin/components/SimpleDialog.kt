package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.PopupAlertDialogProvider.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextIndent
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
    height: Int = 220,
    content: @Composable (() -> Unit) = {
        Column(
            modifier = Modifier.height(height.dp).width(width.dp).clip(RoundedCornerShape(5.dp))
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp).clip(RoundedCornerShape(5.dp))) {
                Text(color = titleColor, text = title, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
            }
            val scroll = rememberScrollState()
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(5.dp).verticalScroll(scroll),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectionContainer{
                    Text(color = Color.Gray, text = "   $text", fontSize = 16.sp)
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
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
        }
    }
) {
    AlertDialog(onDismissRequest = {
//        showingDialog.value = false
    }, content = {
        content()
    })
}