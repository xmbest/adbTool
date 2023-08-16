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
        fun d(msg:String):String{
            val date = sdf.format(Date())
            val str = String.format("%s D %s", date,msg)
            println(str)
            if (saveLog){
                flush(str)
            }
            return str
        }

        fun e(msg:String):String{
            val date = sdf.format(Date())
            val str = String.format("%s E %s", date,msg)
            println(str)
            if (saveLog){
                flush(str)
            }
            return str
        }

        fun flush(msg: String){
            val sdf1 = SimpleDateFormat("yyyy_MM_dd_HH")
            val date = sdf1.format(Date())
            val parent = File(BashUtils.workDir,"log")
            if (!parent.exists()){
                parent.mkdir()
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
            val file = File(BashUtils.workDir,"res.log")
            val output = BufferedOutputStream(FileOutputStream(file,true))
            output.write(msg.toByteArray())
            output.flush()
            output.close()
        }
    }
}