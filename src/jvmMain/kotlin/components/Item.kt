package components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import config.*
import status.currentDevice

@Composable
fun Item(icon: String, label: String, runnable: () -> String = {
    ""
}) {
    val showingDialog = remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.height(item_height).width(item_width)
            .clip(RoundedCornerShape(item_clicked_rounded))
            .clickable {
                if (currentDevice.value.isBlank()){
                    showingDialog.value = true
                }
                runnable()
            }
    ) {
        Icon(
            painter = painterResource(icon),
            null,
            modifier = Modifier.width(item_img_width).height(item_img_height),
            tint = route_left_item_color
        )
        Text(label, fontSize = item_text_fontSize)
        if (showingDialog.value)
            SimpleDialog(showingDialog, text = "请连接ADB后重试")
    }

}

@Composable
fun ContentRow(content: @Composable (() -> Unit)) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        content()
    }
}

@Composable
fun ContentNRow(content: @Composable (() -> Unit)) {
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
        content()
    }
}

@Composable
fun ContentMoreRowColumn(content: @Composable (() -> Unit)) {
    Column(verticalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        content()
    }
}