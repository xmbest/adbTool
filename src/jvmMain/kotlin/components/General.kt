package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun General(
    title: String = "title",
    color: Color = Color.Red,
    height: Int = 1,
    topRight: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null
) {
    Card(modifier = Modifier.fillMaxWidth().padding(10.dp).height((height * 120).dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.height(30.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box {
                    Canvas(modifier = Modifier.size(2.dp)) {
                        drawLine(color, start = Offset(10f, 2f), end = Offset(10f, 18f), strokeWidth = 2f)
                    }
                    Text(title, modifier = Modifier.padding(start = 16.dp))
                }
                Box(modifier = Modifier.padding(end = 16.dp)) {
                    if (topRight != null)
                        topRight()
                }
            }
            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (content != null)
                    content()
            }
        }
    }
}
