import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialogProvider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import java.awt.event.KeyEvent

/**
 * 这个东西源码和[PopupAlertDialogProvider]是一样的,只修改了Surface背景为透明
 */
@OptIn(ExperimentalMaterialApi::class)
object CustomDialogProvider : AlertDialogProvider {
    @Composable
    override fun AlertDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
        // Popups on the desktop are by default embedded in the component in which
        // they are defined and aligned within its bounds. But an [AlertDialog] needs
        // to be aligned within the window, not the parent component, so we cannot use
        // [alignment] property of [Popup] and have to use [Box] that fills all the
        // available space. Also [Box] provides a dismiss request feature when clicked

        // outside of the [AlertDialog] content.
        Popup(
            popupPositionProvider = object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize
                ): IntOffset = IntOffset.Zero
            },
            focusable = true,
            onDismissRequest = onDismissRequest,
            onKeyEvent = {
                if (it.type == KeyEventType.KeyDown && it.awtEventOrNull?.keyCode == KeyEvent.VK_ESCAPE) {
                    onDismissRequest()
                    true
                } else {
                    false
                }
            },
        ) {
            val scrimColor = Color.Black.copy(alpha = 0.32f) //todo configure scrim color in function arguments
            Box(
                modifier = Modifier.fillMaxSize().background(scrimColor).pointerInput(onDismissRequest) {
                    detectTapGestures(onPress = { onDismissRequest() })
                }, contentAlignment = Alignment.Center
            ) {
                Surface(
                    Modifier.pointerInput(onDismissRequest) {
                        detectTapGestures(onPress = {
                            // Workaround to disable clicks on Surface background
                        })
                    }, elevation = 24.dp, color = Color.Transparent  // 修改背景色为透明
                ) {
                    content()
                }
            }
        }
    }
}