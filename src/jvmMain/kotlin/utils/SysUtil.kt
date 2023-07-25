package utils

fun getOsType():String{
    val type = System.getProperty("os.name").uppercase()
    return with(type){
        when {
            contains("WIN") -> "windows"
            contains("MAC") -> "mac"
            contains("LIN") -> "linux"
            else -> "other"
        }
    }
}