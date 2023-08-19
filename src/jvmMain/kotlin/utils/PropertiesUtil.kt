package utils

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import status.*
import java.io.*
import java.util.*

class PropertiesUtil {
    companion object{

        fun init() {
            Log.d("workDir: " + BashUtil.workDir)
            val cfgParent = File(BashUtil.workDir, "cfg")
            if (!cfgParent.exists()){
                cfgParent.mkdir()
            }

            //205，207环境
            val file205 = File(cfgParent,"back.inline.dev.conn.cfg.txz~dat")
            if (!file205.exists()){
                val fileOutputStream = FileOutputStream(file205)
                fileOutputStream.write(BashUtil.dev)
                fileOutputStream.close()
                Log.d("create back.inline.dev.conn.cfg.txz~dat")
            }

            val file207 = File(cfgParent,"back.inline.test.conn.cfg.txz~dat")
            if (!file207.exists()){
                val fileOutputStream = FileOutputStream(file207)
                fileOutputStream.write(BashUtil.test)
                fileOutputStream.close()
                Log.d("back.inline.test.conn.cfg.txz~dat")
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
                    setValue("saveLog",if (saveLog.value) "1" else "0","")
                } catch (e: IOException) {
                    e.message?.let { Log.e(it) }
                }
            }else{
                checkDevicesTime.value = getValue("checkDevicesTime")?.toInt() ?: 5
                index.value = getValue("index")?.toInt() ?: 0
                autoSync.value = getValue("autoSync")?.toInt() == 1
                saveLog.value = getValue("saveLog")?.toInt() == 1
                desktop.value = getValue("desktop")?: desktop.value
                adb.value = getValue("adb")?: "adb"
            }

            //adb环境
            val adb1 = File(cfgParent,"adb.exe")
            if (!adb1.exists() && BashUtil.split == "\\"){
                val inputStream = ClassLoader.getSystemResourceAsStream("adb.exe")
                GlobalScope.launch {
                    FileUtil.copyFileUsingFileStreams(inputStream,adb1)
                    adb.value = adb1.absolutePath
                    setValue("adb", adb.value,"")
                    Log.d("create adb.exe")
                }
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
                if (!cfgParent.exists()){
                    cfgParent.mkdir()
                }
                val file = File(cfgParent, "cfg.properties")
                fileOutputStream = FileOutputStream(file)
                properties.store(fileOutputStream, comments)
            } catch (e: FileNotFoundException) {
                e.message?.let { Log.e(it) }
            } catch (e: IOException) {
                e.message?.let { Log.e(it) }
            } finally {
                try {
                    fileOutputStream?.close()
                } catch (e: IOException) {
                    Log.e("cfg.properties文件流关闭出现异常")
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
                val cfgParent = File(BashUtil.workDir, "cfg")
                if (!cfgParent.exists()){
                    cfgParent.mkdir()
                }
                val file = File(cfgParent, "cfg.properties")
                inputStream = FileInputStream(file)
                properties.load(inputStream)
            } catch (e: FileNotFoundException) {
                Log.e("cfg.properties文件未找到!")
            } catch (e: IOException) {
                println("出现IOException")
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    Log.e("cfg.properties文件流关闭出现异常")
                }
            }
            return properties
        }
    }
}