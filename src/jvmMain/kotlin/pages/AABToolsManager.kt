package pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.General

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AABToolsManager() {
    Column(modifier = Modifier.fillMaxSize().fillMaxHeight().verticalScroll(rememberScrollState())) {
        General(title = "AAB解Apks", height = 2, content = {

        })
    }
}