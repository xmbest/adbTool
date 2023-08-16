package utils

import status.autoSync
import status.checkDevicesTime
import status.index
import java.io.*
import java.util.*

class PropertiesUtil {
    companion object{

        fun init() {
            val file = File(BashUtils.workDir, "data.properties")
            val file2 = File(BashUtils.workDir,"res.log")
            if (file2.exists()){
                file2.delete()
            }
            Log.d("workDir: " + BashUtils.workDir)
            if (!file.exists()) {
                try {
                    file.createNewFile()
                    setValue("checkDevicesTime", "$checkDevicesTime", "自动刷新时间")
                    setValue("autoSync", if (autoSync.value) "1" else "0", "是否自动刷新")
                    setValue("index", "$index", "软件打开进入页")
                } catch (e: IOException) {
                    e.message?.let { Log.e(it) }
                }
            }else{
                checkDevicesTime = getValue("checkDevicesTime")?.toLong() ?: 5L
                index = getValue("index")?.toInt() ?: 0
                autoSync.value = getValue("autoSync")?.toInt() == 1
            }
        }

        /**
         * 新增/修改数据
         * @param key
         * @param value
         */
        fun setValue(key: String?, value: String?, comments: String?) {
            val properties = getProperties()
            properties!!.setProperty(key, value)
            var fileOutputStream: FileOutputStream? = null
            try {
                fileOutputStream = FileOutputStream(File(BashUtils.workDir, "data.properties"))
                properties.store(fileOutputStream, comments)
            } catch (e: FileNotFoundException) {
                e.message?.let { Log.e(it) }
            } catch (e: IOException) {
                e.message?.let { Log.e(it) }
            } finally {
                try {
                    fileOutputStream?.close()
                } catch (e: IOException) {
                    Log.e("data.properties文件流关闭出现异常")
                }
            }
        }

        /**
         * 根据key查询value值
         * @param key key
         * @return
         */
        fun getValue(key: String?): String? {
            val properties = getProperties()
            return properties!!.getProperty(key)
        }

        /**
         * 获取Properties对象
         * @return
         */
        fun getProperties(): Properties? {
            val properties = Properties()
            var inputStream: InputStream? = null
            try {
                //data.properties在resources目录下
                val file = File(BashUtils.workDir, "data.properties")
                inputStream = FileInputStream(file)
                properties.load(inputStream)
            } catch (e: FileNotFoundException) {
                Log.e("data.properties文件未找到!")
            } catch (e: IOException) {
                println("出现IOException")
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    Log.e("data.properties文件流关闭出现异常")
                }
            }
            return properties
        }
    }
}