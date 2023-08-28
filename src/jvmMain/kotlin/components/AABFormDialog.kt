package components

import CustomDialogProvider
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import entity.AABToolsCfgBean
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterialApi::class)
@Composable
fun AABFormDialog(openDialog: MutableState<Boolean>, aabToolsBeanState: MutableState<AABToolsCfgBean>) {

    if (openDialog.value) {
        val aabToolsBean = AABToolsCfgBean()
        val selectPath: MutableState<String> = mutableStateOf("")
        AlertDialog(dialogProvider = CustomDialogProvider, modifier = Modifier.clip(RoundedCornerShape(14.dp)), onDismissRequest = {
            openDialog.value = false
        }, title = {
            Text(text = "配置KeyStore参数")
        }, text = {
            Column {
                Column(
                    modifier = Modifier.padding(all = 5.dp), verticalArrangement = Arrangement.Center
                ) {
                    if (selectPath.value.isNotEmpty()) {
                        Text(selectPath.value)
                    }
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        val selectFile = FileSelector.selectFile("jks")
                        selectPath.value = selectFile
                        aabToolsBean.keyStoryPath = selectFile
                    }) {
                        Text("选择KeyStore路径")
                    }
                }
                Text("输入KeyStore的密钥库口令", modifier = Modifier.padding(top = 10.dp))
                TextField(value = aabToolsBean.keyStoryPwd, onValueChange = { aabToolsBean.keyStoryPwd = it })
                Text("输入KeyStore的Alias", modifier = Modifier.padding(top = 10.dp))
                TextField(value = aabToolsBean.keyAlias, onValueChange = { aabToolsBean.keyAlias = it })
                Text("输入KeyStore的口令", modifier = Modifier.padding(top = 10.dp))
                TextField(value = aabToolsBean.keyPwd, onValueChange = { aabToolsBean.keyPwd = it })
                Text("为配置取个名吧", modifier = Modifier.padding(top = 10.dp))
                TextField(value = aabToolsBean.cfgName, onValueChange = { aabToolsBean.cfgName = it })
            }
        }, buttons = {
            Row(
                modifier = Modifier.padding(all = 14.dp), horizontalArrangement = Arrangement.Center
            ) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    openDialog.value = false
                    aabToolsBeanState.value = aabToolsBean
                }) {
                    Text("Done")
                }
            }
        })
    }
}