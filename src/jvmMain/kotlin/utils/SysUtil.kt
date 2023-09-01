package utils

var isMac = false
var isWindows = false
var isOther = false
var isLinux = false

fun initOsType(){
    val type = System.getProperty("os.name").uppercase()
    return with(type){
        when {
            contains("WIN") -> isWindows = true
            contains("MAC") -> isMac = true
            contains("LIN") -> isLinux = true
            else -> isOther = true
        }
    }
}
