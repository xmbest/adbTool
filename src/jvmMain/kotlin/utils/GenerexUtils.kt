package utils

import com.mifmif.common.regex.Generex

/**
 * 生成对应规则文本
 */
class GenerexUtils {
    companion object {
        fun generateAll(text: String, toCn: Boolean): String {
            val text1 = text.trim().replace("\\[(\\d*|\\d+-\\d+)]".toRegex(), "【$1】")
                .replace("[", "(")
                .replace("]", ")?")
                .replace("（", "(")
                .replace("）", ")")
                .replace("【", "[")
                .replace("】", "]")
                .replace("；", "")
                .replace(";", "")
                .replace(",", "")
                .replace("，", "")
                .replace("。", "")
                .replace("\n", "|")
                .replace("\\d*\\.".toRegex(), "")
            println(text1)
            val generex = Generex(text1)
            val str = StringBuilder()
            val allMatchedStrings = generex.allMatchedStrings
            for (i in 0 until allMatchedStrings.size) {
                val s = if (toCn) NumberValueUtil.num2CNStr(allMatchedStrings[i]) else allMatchedStrings[i]
                str.append("\"" + s + "\"")
                if (i < allMatchedStrings.size - 1)
                    str.append(",")
            }
            return str.toString()
        }

        /**
         * 英文谐音需求
         * @param oldStr 要替换的英文
         * @param newStr 要替换成的中文
         * @param strings 要被处理的命令字
         */
        fun replace(oldStr: String?, newStr: List<String?>, strings: List<String>): String {
            val stringBuilder = java.lang.StringBuilder()
            var res = ""
            for (i in newStr.indices) {
                for (string in strings) {
                    stringBuilder.append("\"" + string.replace(oldStr!!, newStr[i]!!) + "\",")
                }
                res += "\"" + newStr[i] + "\""
                if (i != newStr.size - 1) {
                    res += ","
                }
            }
            stringBuilder.append("\nTXZAsrManager.getInstance().setRealFictitiousCmds(\"$oldStr\",$res);")
            return stringBuilder.toString()
        }
    }
}