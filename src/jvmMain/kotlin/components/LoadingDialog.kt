package components

import CustomDialogProvider
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
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

val showingLoadingDialog = mutableStateOf(false)
val loadingTitle = mutableStateOf("\u5904\u7406\u4e2d")
val loadingText = mutableStateOf("\u8bf7\u7a0d\u5019...")

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoadingDialog(
    showingDialog: MutableState<Boolean>,
    title: String = "\u5904\u7406\u4e2d",
    titleColor: Color = GOOGLE_BLUE,
    text: String = "\u8bf7\u7a0d\u5019...",
    width: Int = 300,
    height: Int = 120,
) {
    AlertDialog(
        dialogProvider = CustomDialogProvider,
        modifier = Modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(5.dp)),
        onDismissRequest = {
            showingDialog.value = false
        },
        buttons = {
        },
        title = { Text(color = titleColor, text = title) },
        text = {
            Row(
                modifier = Modifier.width(width.dp).height(height.dp).padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(color = GOOGLE_BLUE, strokeWidth = 3.dp)
                Text(text = text, fontSize = 16.sp, color = Color.Gray)
            }
        }
    )
}
