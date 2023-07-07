package components

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.PopupAlertDialogProvider.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProcessDialogComponent(
    dialogState: MutableState<Boolean>,
    text: String = "Precessing"
) {

    if (dialogState.value) {
        AlertDialog(onDismissRequest = { dialogState.value = false }) {
            //圆形进度条--无限循环
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.width(200.dp).height(200.dp)
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.requiredHeight(10.dp))
                Text(text)
            }
        }
    }
}