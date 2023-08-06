package utils

import com.mifmif.common.regex.Generex

/**
 * 生成对应规则文本
 */
class GenerexUtils {
    companion object {
        fun generateAll(text: String): String {
            val text1 = text.replace("\\[(\\d*|\\d+-\\d+)]".toRegex(), "【$1】")
                .replace("[", "(")
                .replace("]", ")?")
                .replace("（", "(")
                .replace("）", ")")
                .replace("【", "[")
                .replace("】", "]")
                .replace("\n", "|")
            val generex = Generex(text1)
            val str = StringBuilder()
            val allMatchedStrings = generex.allMatchedStrings
            for (i in 0 until allMatchedStrings.size) {
                str.append("\"" + NumberValueUtil.num2CNStr(allMatchedStrings.get(i)) + "\"")
                if (i < allMatchedStrings.size - 1)
                    str.append(",")
            }
            return str.toString()
        }
    }
}