package pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import components.*
import entity.AABToolsCfgBean
import theme.GOOGLE_RED
import theme.LIGHT_GRAY
import utils.PropertiesUtil


private val notSelectedCfg = "配置未选择"

val cfg = mutableStateOf(
    mutableListOf(
        notSelectedCfg,
    )
)

var popKey = "aabToolsCfg"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AABToolsManager() {
    val selectItem = remember {
        mutableStateOf(notSelectedCfg)
    }
    val openDialog = remember { mutableStateOf(false) }
    val aabToolsBeanState: MutableState<AABToolsCfgBean> = remember { mutableStateOf(AABToolsCfgBean()) }
    refreshCfg()
    Column(modifier = Modifier.fillMaxSize().fillMaxHeight().verticalScroll(rememberScrollState())) {
        General(title = "配置管理", height = 1, content = {
            ContentMoreRowColumn {
                ContentNRow {
                    Spinner(modifier = Modifier.wrapContentSize(),
                        dropDownModifier = Modifier.wrapContentSize(),
                        items = cfg.value,
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
                        openDialog.value = true
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
    if (openDialog.value) {
        AABFormDialog(openDialog, aabToolsBeanState)
    }
}


private fun refreshCfg() {
    cfg.value.let {
        it.clear()
        it.add(notSelectedCfg)
        PropertiesUtil.getListByKey(popKey).forEach { ele -> cfg.value.add(ele) }
    }
}