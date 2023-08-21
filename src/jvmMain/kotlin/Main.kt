import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import theme.GOOGLE_BLUE
import config.window_height
import config.window_width
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pages.Route
import status.appIsMinimized
import utils.BashUtil
import utils.ListenDeviceUtil.Companion.listenDevices
import utils.Log
import utils.PropertiesUtil
import utils.getRealLocation
import java.awt.Dimension
import java.awt.Toolkit

@Composable
@Preview
fun App() {
    MaterialTheme(lightColors(primary = GOOGLE_BLUE)) {
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
        onCloseRequest = ::exitApplication, title = "ADBTool", state = state, icon = painterResource(getRealLocation("logo"))
    ) {
        App()
        Log.init()
        BashUtil.init()
        PropertiesUtil.init()
        LaunchedEffect(state){
            snapshotFlow { state.isMinimized }
                .onEach(::onMinimized).launchIn(this)
        }
    }
}

private fun onMinimized(isMinimized: Boolean) {
    Log.d("isMinimized: $isMinimized")
    appIsMinimized.value = isMinimized
    if (!isMinimized)
        listenDevices()
}



