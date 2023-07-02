package utils

import com.mifmif.common.regex.Generex

class GenerexUtils {
    companion object {
        fun generateAll(text: String): String {
            val text1 = text.replace("\\[(\\d.*\\d)]".toRegex(),"【$1】")
                .replace("[", "(")
                .replace("]", ")")
                .replace("（", "(")
                .replace("）", ")")
                .replace("【", "[")
                .replace("】", "]")
            val generex = Generex(text1)
            val str = StringBuilder()
            val allMatchedStrings = generex.allMatchedStrings
            for (i in 0 until allMatchedStrings.size) {
                str.append("\"" + allMatchedStrings.get(i) + "\"")
                if (i < allMatchedStrings.size - 1)
                    str.append(",")
            }
            return str.toString()
        }
    }
}