package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import config.*

@Composable
fun Item(icon: String, label: String) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.height(item_height).width(item_width)
            .clip(RoundedCornerShape(item_clicked_rounded))
            .clickable {

            }
    ) {
        Image(painter = painterResource(icon), null, modifier = Modifier.width(item_img_width).height(item_img_height))
        Text(label, fontSize = item_text_fontSize)
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