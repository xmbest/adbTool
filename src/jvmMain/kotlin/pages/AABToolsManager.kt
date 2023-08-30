package pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import components.*
import entity.AABToolsCfgBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theme.GOOGLE_RED
import theme.LIGHT_GRAY
import utils.AABUtils
import utils.PropertiesUtil
import utils.getRealLocation


private val notSelectedCfg = "配置未选择"

val cfg = mutableStateOf(
    mutableListOf(
        notSelectedCfg,
    )
)

var aabToolsPopKey = "aabToolsCfg"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AABToolsManager() {
    val selectItem = remember {
        mutableStateOf(notSelectedCfg)
    }
    val openDialog = remember { mutableStateOf(false) }
    val aabToolsBeanState: MutableState<AABToolsCfgBean> = remember { mutableStateOf(AABToolsCfgBean()) }
    val aabPath = mutableStateOf("")
    val apksPath = mutableStateOf("")
    val keyStoryPath = mutableStateOf("")
    val keyStoryPwd = mutableStateOf("")
    val keyAlias = mutableStateOf("")
    val keyPwd = mutableStateOf("")
    val cfgName = mutableStateOf("")
    refreshCfg()
    Column(modifier = Modifier.fillMaxSize().fillMaxHeight().verticalScroll(rememberScrollState())) {
        General(title = "配置管理", height = 1, content = {
            ContentMoreRowColumn {
                ContentNRow {
                    Spinner(modifier = Modifier.wrapContentSize(),
                        dropDownModifier = Modifier.wrapContentSize(),
                        items = cfg,
                        selectedItem = selectItem.value,
                        onItemSelected = {
                            selectItem.value = it
                        },
                        selectedItemFactory = { modifier, item ->
                            Row(
                                modifier = modifier.padding(10.dp).wrapContentSize().clip(RoundedCornerShape(3.dp))
                            ) {
                                Box(Modifier.background(LIGHT_GRAY).height(40.dp)) {
                                    Text(getCfgName(item), Modifier.width(600.dp).align(Alignment.Center).padding(start = 10.dp))
                                }
                                aabToolsBeanState.value = getCfgBean(item)
                                keyStoryPath.value = aabToolsBeanState.value.keyStoryPath
                                keyStoryPwd.value = aabToolsBeanState.value.keyStoryPwd
                                keyAlias.value = aabToolsBeanState.value.keyAlias
                                keyPwd.value = aabToolsBeanState.value.keyPwd
                                cfgName.value = aabToolsBeanState.value.cfgName
                            }
                        },
                        dropdownItemFactory = { item, _ ->
                            Row {
                                Text(text = getCfgName(item), Modifier.width(150.dp).padding(start = 10.dp))
                                if (item != notSelectedCfg) {
                                    Icon(painter = painterResource(getRealLocation("clear")), null, modifier = Modifier.size(20.dp).clickable {
                                        PropertiesUtil.deleteValue4List(aabToolsPopKey, item)
                                        refreshCfg()
                                    })
                                }
                            }
                        })
                    Button(content = {
                        Text("添加配置")
                    }, onClick = {
                        openDialog.value = true
                    })
                }
            }
        })
        General(title = "当前配置", color = GOOGLE_RED, height = 3, content = {
            ContentMoreRowColumn(modifier = Modifier.padding(horizontal = 30.dp)) {
                ContentNRow {
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally).weight(1F).padding(end = 10.dp)) {
                        Text("AAB包路径:", modifier = Modifier.align(Alignment.CenterVertically))
                        TextField(value = aabPath.value, modifier = Modifier.align(Alignment.CenterVertically).fillMaxWidth().clickable(true) {
                            val selectFile = FileSelector.selectFile("aab")
                            if (selectFile.isNotEmpty()) aabPath.value = selectFile
                        }, maxLines = 1, onValueChange = {
                            aabPath.value = it
                        }, enabled = false)
                    }
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally).weight(1F).padding(start = 10.dp)) {
                        Text("KeyStory路径:", modifier = Modifier.align(Alignment.CenterVertically))
                        TextField(value = keyStoryPath.value,
                            modifier = Modifier.align(Alignment.CenterVertically).fillMaxWidth(),
                            maxLines = 1,
                            onValueChange = { keyStoryPath.value = it })
                    }
                }
                ContentNRow {
                    Row(
                        modifier = Modifier.weight(0.5F).align(Alignment.CenterHorizontally).padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("配置名:", modifier = Modifier.align(Alignment.CenterVertically))
                        Text(
                            cfgName.value,
                            style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Row(
                        modifier = Modifier.weight(1F).align(Alignment.CenterHorizontally).padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Key别名:", modifier = Modifier.align(Alignment.CenterVertically))
                        TextField(value = keyAlias.value, modifier = Modifier.align(Alignment.CenterVertically), onValueChange = { keyAlias.value = it })
                    }
                    Row(
                        modifier = Modifier.weight(1F).align(Alignment.CenterHorizontally).padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("KeyStory密钥:", modifier = Modifier.align(Alignment.CenterVertically))
                        TextField(value = keyStoryPwd.value, modifier = Modifier.align(Alignment.CenterVertically), onValueChange = { keyStoryPwd.value = it })
                    }
                    Row(
                        modifier = Modifier.weight(1F).align(Alignment.CenterHorizontally).padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Key密钥:", modifier = Modifier.align(Alignment.CenterVertically))
                        TextField(value = keyPwd.value, modifier = Modifier.align(Alignment.CenterVertically), onValueChange = { keyPwd.value = it })
                    }
                }
                ContentNRow {

                }
                ContentNRow {
                    Button(modifier = Modifier.weight(1F).padding(horizontal = 10.dp), onClick = {
                        CoroutineScope(Dispatchers.Default).launch {
                            val apksPath = AABUtils.AAB2Apks(aabPath.value, aabToolsBeanState.value)
                            if (apksPath.isEmpty()) {
                                toastText.value = "生成错误"
                                showToast.value = true
                            } else {
                                toastText.value = "生成成功,在AAB同级目录下"
                                showToast.value = true
                            }
                        }
                    }, content = {
                        Text("生成APKS")
                    })
                    Button(modifier = Modifier.weight(1F).padding(horizontal = 10.dp), onClick = {

                    }, content = {
                        Text("安装APKS到手机")
                    })
                }
            }
        })
    }
    if (openDialog.value) {
        AABFormDialog(openDialog, aabToolsBeanState)
    }
}

fun getCfgName(item: String): String {
    return try {
        Gson().fromJson(item, AABToolsCfgBean::class.java).cfgName
    } catch (e: Exception) {
        item
    }
}

fun getCfgBean(item: String): AABToolsCfgBean {
    return try {
        Gson().fromJson(item, AABToolsCfgBean::class.java)
    } catch (e: Exception) {
        AABToolsCfgBean()
    }
}


private fun refreshCfg() {
    val value = cfg.value
    value.let {
        it.clear()
        it.add(notSelectedCfg)
        PropertiesUtil.getListByKey(aabToolsPopKey).forEach { ele -> value.add(ele) }
    }
    cfg.value = value
}