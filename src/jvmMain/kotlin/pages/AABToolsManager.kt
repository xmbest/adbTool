package pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import components.*
import kotlinx.coroutines.selects.select
import theme.GOOGLE_RED
import theme.LIGHT_GRAY
import theme.SIMPLE_GRAY
import utils.AABUtils
import utils.PropertiesUtil
import utils.shell


val datas = mutableListOf(
    "配置未选择",
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AABToolsManager() {
    val selectItem = remember {
        mutableStateOf("配置未选择")
    }
    Column(modifier = Modifier.fillMaxSize().fillMaxHeight().verticalScroll(rememberScrollState())) {
        General(title = "配置管理", height = 1, content = {
            ContentMoreRowColumn {
                ContentNRow {
                    Spinner(modifier = Modifier.wrapContentSize(),
                        dropDownModifier = Modifier.wrapContentSize(),
                        items = datas,
                        selectedItem = selectItem.value,
                        onItemSelected = {
                            selectItem.value = it
                        },
                        selectedItemFactory = { modifier, item ->
                            Row(
                                modifier = modifier.padding(10.dp).wrapContentSize().clip(RoundedCornerShape(3.dp))
                            ) {
                                Box(Modifier.background(LIGHT_GRAY).height(40.dp)) {
                                    Text(item, Modifier.width(600.dp).align(Alignment.Center).padding(start = 10.dp))
                                }
                            }
                        },
                        dropdownItemFactory = { item, _ ->
                            Text(text = item, Modifier.width(600.dp).padding(start = 10.dp))
                        })
                    Button(content = {
                        Text("添加配置")
                    }, onClick = {

                    })
                    Button(content = {
                        Text("修改配置")
                    }, onClick = {

                    })
                }
            }
        })
        General(title = "当前配置", color = GOOGLE_RED, height = 3, content = {
            ContentMoreRowColumn {
                ContentNRow {

                }
            }
        })
    }
}