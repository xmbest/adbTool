package components

import CustomDialogProvider
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import entity.AABToolsCfgBean
import kotlinx.coroutines.DelicateCoroutinesApi
import pages.aabToolsPopKey
import utils.PropertiesUtil

@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterialApi::class)
@Composable
fun AABFormDialog(openDialog: MutableState<Boolean>, aabToolsBeanState: MutableState<AABToolsCfgBean>) {

    if (openDialog.value) {
        val keyStoryPath = mutableStateOf("")
        val keyStoryPwd = mutableStateOf("")
        val keyAlias = mutableStateOf("")
        val keyPwd = mutableStateOf("")
        val cfgName = mutableStateOf("")
        AlertDialog(dialogProvider = CustomDialogProvider, modifier = Modifier.clip(RoundedCornerShape(14.dp)), onDismissRequest = {
            openDialog.value = false
        }, title = {
            Text(text = "配置KeyStore参数")
        }, text = {
            Column {
                Column(
                    modifier = Modifier.padding(all = 5.dp), verticalArrangement = Arrangement.Center
                ) {
                    if (keyStoryPath.value.isNotEmpty()) {
                        Text(keyStoryPath.value)
                    }
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        val selectFile = FileSelector.selectFile("jks")
                        keyStoryPath.value = selectFile
                    }) {
                        Text("选择KeyStore路径")
                    }
                }
                Text("输入KeyStore的密钥库口令", modifier = Modifier.padding(top = 10.dp))
                TextField(value = keyStoryPwd.value, onValueChange = { keyStoryPwd.value = it })
                Text("输入KeyStore的Alias", modifier = Modifier.padding(top = 10.dp))
                TextField(value = keyAlias.value, onValueChange = { keyAlias.value = it })
                Text("输入KeyStore的口令", modifier = Modifier.padding(top = 10.dp))
                TextField(value = keyPwd.value, onValueChange = { keyPwd.value = it })
                Text("为配置取个名吧", modifier = Modifier.padding(top = 10.dp))
                TextField(value = cfgName.value, onValueChange = { cfgName.value = it })
            }
        }, buttons = {
            Row(
                modifier = Modifier.padding(all = 14.dp), horizontalArrangement = Arrangement.Center
            ) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    openDialog.value = false
                    aabToolsBeanState.value = AABToolsCfgBean(
                        keyStoryPath.value,
                        keyStoryPwd.value,
                        keyAlias.value,
                        keyPwd.value,
                        cfgName.value,
                    )
                    PropertiesUtil.addValueToList(aabToolsPopKey, Gson().toJson(aabToolsBeanState.value), "")
                }) {
                    Text("Done")
                }
            }
        })
    }
}