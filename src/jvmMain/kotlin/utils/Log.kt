package utils

import status.saveLog
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Log {
    companion object{
        private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        fun init(){
            val logParent = File(BashUtil.workDir,"log")
            if (!logParent.exists()){
                logParent.mkdirs()
            }
            val file = File(logParent,"result.log")
            if (file.exists()){
                file.delete()
            }
        }
        fun d(msg:String):String{
            val date = sdf.format(Date())
            val str = String.format("%s D %s", date,msg)
            println(str)
            if (saveLog.value){
                flush(str)
            }
            return str
        }

        fun e(msg:String):String{
            val date = sdf.format(Date())
            val str = String.format("%s E %s", date,msg)
            println(str)
            if (saveLog.value){
                flush(str)
            }
            return str
        }

        fun flush(msg: String){
            val sdf1 = SimpleDateFormat("yyyy_MM_dd_HH")
            val date = sdf1.format(Date())
            val parent = File(BashUtil.workDir,"log")
            if (!parent.exists()){
                parent.mkdirs()
            }
            val file = File(parent,"${date}.log")
            if (!file.exists()){
                file.createNewFile()
            }
            val output = BufferedOutputStream(FileOutputStream(file,true))
            output.write((msg + "\n").toByteArray())
            output.flush()
            output.close()
        }

        fun flushRes(msg: String){
            if (!saveLog.value)
                return
            val parent = File(BashUtil.workDir,"log")
            if (!parent.exists()){
                parent.mkdirs()
            }
            val file = File(parent,"result.log")
            val output = BufferedOutputStream(FileOutputStream(file,true))
            output.write(msg.toByteArray())
            output.flush()
            output.close()
        }
    }
}