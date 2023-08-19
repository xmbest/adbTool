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
                    while (!appIsMinimized.value && autoSync.value) {
                        if (BashUtil.runing){
                            continue
                        }
                        getDevices()
                        Log.d("checkDevicesTime = ${checkDevicesTime.value}")
                        delay(1000L * checkDevicesTime.value)
                    }
                }
            }
        }
    }
}