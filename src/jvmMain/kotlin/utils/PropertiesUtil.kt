package utils

import pages.appKeyword
import pages.boardCustomer
import status.*
import java.io.*
import java.util.*

class PropertiesUtil {
    companion object {
        fun init() {
            LogUtil.d("workDir: " + BashUtil.workDir)
            val cfgParent = File(BashUtil.workDir, "cfg")
            if (!cfgParent.exists()) {
                cfgParent.mkdirs()
            }
            val file = File(cfgParent, "cfg.properties")
            if (!file.exists()) {
                try {
                    file.createNewFile()
                    //自动刷新时间
                    setValue("checkDevicesTime", "${checkDevicesTime.value}", "")
                    //是否自动刷新
                    setValue("autoSync", if (autoSync.value) "1" else "0", "")
                    //软件起始页
                    setValue("index", "${index.value}", "")
                    //保存日志
                    setValue("saveLog", if (saveLog.value) "1" else "0", "")
                } catch (e: IOException) {
                    e.message?.let { LogUtil.e(it) }
                }
            } else {
                checkDevicesTime.value = getValue("checkDevicesTime")?.toInt() ?: 5
                index.value = getValue("index")?.toInt() ?: 0
                autoSync.value = getValue("autoSync")?.toInt() == 1
                saveLog.value = getValue("saveLog")?.toInt() == 1
                desktop.value = getValue("desktop") ?: desktop.value
                adb.value = getValue("adb") ?: "adb"
                //应用关键词
                appKeyword.value = getValue("appKeyword") ?: ""
                //自定义广播
                boardCustomer.value = getValue("boardCustomer") ?: "am broadcast -a xxx --es xxx"
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
                val cfgParent = File(BashUtil.workDir, "cfg")
                if (!cfgParent.exists()) {
                    cfgParent.mkdir()
                }
                val file = File(cfgParent, "cfg.properties")
                file.setExecutable(true)
                fileOutputStream = FileOutputStream(file)
                properties.store(fileOutputStream, comments)
            } catch (e: FileNotFoundException) {
                e.message?.let { LogUtil.e(it) }
            } catch (e: IOException) {
                e.message?.let { LogUtil.e(it) }
            } finally {
                try {
                    fileOutputStream?.close()
                } catch (e: IOException) {
                    LogUtil.e("cfg.properties文件流关闭出现异常")
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
        private fun getProperties(): Properties? {
            val properties = Properties()
            var inputStream: InputStream? = null
            try {
                val cfgParent = File(BashUtil.workDir, "cfg")
                if (!cfgParent.exists()) {
                    cfgParent.mkdir()
                }
                val file = File(cfgParent, "cfg.properties")
                inputStream = FileInputStream(file)
                properties.load(inputStream)
            } catch (e: FileNotFoundException) {
                LogUtil.e("cfg.properties文件未找到!")
            } catch (e: IOException) {
                println("出现IOException")
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    LogUtil.e("cfg.properties文件流关闭出现异常")
                }
            }
            return properties
        }
    }
}