import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import config.window_height
import config.window_width
import pages.Route
import java.awt.Dimension
import java.awt.Toolkit

@Composable
@Preview
fun App() {
    MaterialTheme {
        Route()
    }
}

fun main() = application {
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    //显示大小
    val width = window_width
    val height = window_height
    //居中显示
    val x: Double = screenSize.getWidth() / 2 - width / 2
    val y: Double = screenSize.getHeight() / 2 - height / 2
    val state = rememberWindowState(width = width.dp, height = height.dp, position = WindowPosition(x.dp, y.dp))
    Window(
        onCloseRequest = ::exitApplication, title = "工具箱", state = state, icon = painterResource("logo.png")
    ) {
        App()
    }
}



