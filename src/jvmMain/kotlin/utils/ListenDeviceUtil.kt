package utils

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import status.appIsMinimized
import status.autoSync
import status.checkDevicesTime

class ListenDeviceUtil {
    companion object {

        /**
         * 轮询查找当前连接设备
         */
        @OptIn(DelicateCoroutinesApi::class)
        fun listenDevices() {
            if (autoSync.value){
                GlobalScope.launch {
                    //判断是否在前台
                    while (!appIsMinimized.value) {
                        if (BashUtils.runing){
                            continue
                        }
                        getDevices()
                        delay(1000 * checkDevicesTime.value)
                    }
                }
            }
        }
    }
}