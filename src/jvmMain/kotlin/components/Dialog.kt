package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.PopupAlertDialogProvider.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyDialog(
    showingDialog: MutableState<Boolean>,
    title: String = "警告",
    titleColor: Color = GOOGLE_RED,
    text: String = "测试",
    content: @Composable (() -> Unit) = {
        Column(
            modifier = Modifier.height(160.dp).width(320.dp).background(route_left_background).clip(RoundedCornerShape(5.dp))
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp).clip(RoundedCornerShape(5.dp))) {
                Text(color = titleColor, text = title, fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp))
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(color = Color.Gray, text = text, fontSize = 16.sp, modifier = Modifier)
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        showingDialog.value = false
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = GOOGLE_BLUE),
                    modifier = Modifier.padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    Text(text = "确定", color = Color.White)
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