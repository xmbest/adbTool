package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 阿拉伯数字与中文数值互转工具类
 */
public class NumberValueUtil {
    public static String getNumber(String str){
        // 控制正则表达式的匹配行为的参数(小数)
        Pattern p = Pattern.compile("(\\d+\\.\\d+)");
        //Matcher类的构造方法也是私有的,不能随意创建,只能通过Pattern.matcher(CharSequence input)方法得到该类的实例.
        Matcher m = p.matcher(str);
        //m.find用来判断该字符串中是否含有与"(\\d+\\.\\d+)"相匹配的子串
        if (m.find()) {
            //如果有相匹配的,则判断是否为null操作
            //group()中的参数：0表示匹配整个正则，1表示匹配第一个括号的正则,2表示匹配第二个正则,在这只有一个括号,即1和0是一样的
            str = m.group(1) == null ? "" : m.group(1);
        } else {
            //如果匹配不到小数，就进行整数匹配
            p = Pattern.compile("(\\d+)");
            m = p.matcher(str);
            if (m.find()) {
                //如果有整数相匹配
                str = m.group(1) == null ? "" : m.group(1);
            } else {
                //如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
                str = "";
            }
        }
        return str;
    }

    /**
     * 从字符串中提取出所有中文数值（无法提取负xxx）
     * @param text 输入字符串
     * @return 返回字符串中的中文数值列表
     */
    public static List<String> getCNNumberFromStr(String text){
        if (text == null || text.length() == 0){
            return null;
        }
        List<String> res = new ArrayList<>();
        String regex = "[零一二两三四五六七八九十][十百千万]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        StringBuilder zhStr = new StringBuilder();
        String findStr;
        int lastEnd = -1;
        while (matcher.find()){
            findStr = matcher.group();
            if (matcher.start() == lastEnd){
                zhStr.append(findStr);
            }else {
                if (zhStr.length() != 0){
                    res.add(zhStr.toString());
                }
                zhStr = new StringBuilder(findStr);
            }
            lastEnd = matcher.end();
        }
        if (lastEnd == -1){
            return null;
        }
        res.add(zhStr.toString());
        return res;
    }

    /**
     * 从字符串中提取出所有阿拉伯数字
     * @param text 输入字符串
     * @return 返回字符串中的阿拉伯数字列表
     */
    public static List<Integer> getNumberFromStr(String text){
        if (text == null || text.length() == 0){
            return null;
        }
        List<Integer> res = new ArrayList<>();
        String regex = "-?\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            res.add(Integer.valueOf(matcher.group()));
        }
        if (res.size() == 0) return null;
        return res;
    }

    /**
     * 将字符串中的中文数值转成阿拉伯数字后返回（无法转换负xxx）
     * @param text 输入字符串
     * @return 返回转换后的新字符串
     */
    public static String cn2NumStr(String text){
        StringBuilder res = new StringBuilder();
        StringBuilder zhStr = new StringBuilder();
        String regex = "[零一二两三四五六七八九十][十百千万]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        int lastEnd = -1;
        int lastStart = -1;
        int appendIndex = 0;
        while (matcher.find()){
            String s = matcher.group();
            if (matcher.start() == lastEnd){
                zhStr.append(s);
            }else {
                if (zhStr.length() != 0){
                    String num = cnToNum(zhStr.toString());
                    res.append(text.substring(appendIndex, lastStart));
                    res.append(num);
                    appendIndex = lastEnd;
                }
                zhStr = new StringBuilder(s);
                lastStart = matcher.start();
            }
            lastEnd = matcher.end();
        }
        if (lastEnd == -1){
            return text;
        }
        res.append(text.substring(appendIndex, lastStart));
        String num = cnToNum(zhStr.toString());
        res.append(num);
        res.append(text.substring(lastEnd));
        return res.toString();
    }

    /**
     * 提取字符串数字
     * @param str 字符串
     * @return 字符串中数字
     */
    public static int getStrNumber(String str){
        StringBuilder res = new StringBuilder();
        String regex = "-?\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        String s = null;
        while (matcher.find()) {
            s = matcher.group();
        }
        return Integer.parseInt(s);
    }

    /**
     * 将单个中文数值转成阿拉伯数字后返回。例：输入五百七十八，返回578
     * @param zh 需转换的中文数值
     * @return 阿拉伯数字
     */
    public static String cnToNum(String zh){
        if (zh.length() == 1){
            return numDic.get(zh);
        }
        if (zh.charAt(0) == '十'){
            return "1" + numDic.get(zh.substring(1));
        }
        String regex = "[十百千万]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(zh);
        int unitIndex;
        int unitLastIndex = 100;
        int start = -1;
        int res = 0;
        int unit;
        int num;
        while (matcher.find()){
            String s = matcher.group();
            unitIndex = zhUnitList.indexOf(s);
            start = matcher.start();
            unit = getUnitNum(s);
            num = Integer.parseInt(numDic.get(zh.substring(start - 1, start)));
            if (unitIndex > unitLastIndex){
                res = res * unit + num * unit;
            }else {
                res += unit * num;
            }
            unitLastIndex = unitIndex;
        }
        if (start != -1 && zh.length() -1 != start){
            num = Integer.parseInt(numDic.get(zh.substring(zh.length() -1)));
            res += num;
        }
        return String.valueOf(res);
    }

    /**
     * 将字符串中的阿拉伯数字转成中文数值后返回
     * @param text 输入字符串
     * @return 返回转换后的新字符串
     */
    public static String num2CNStr(String text){
        StringBuilder res = new StringBuilder();
        String regex = "-?\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        String s;
        int lastIndex = 0;
        while (matcher.find()){
            s = matcher.group();
            res.append(text.substring(lastIndex, matcher.start()));
            res.append(numToCN(Integer.parseInt(s)));
            lastIndex = matcher.end();
        }
        res.append(text.substring(lastIndex));
        return res.toString();
    }

    /**
     * 将单个阿拉伯数字转成中文数值后返回。例：输入578，返回五百七十八
     * @param value 需转换的阿拉伯数字
     * @return 中文数值
     */
    public static String numToCN(int value){
        StringBuilder res = new StringBuilder();
        if (value < 0){
            res.append("负");
        }
        value = Math.abs(value);
        if (value < 11){
            res.append(zhDic.get(String.valueOf(value)));
            return res.toString();
        }
        if (value < 20){
            res.append("十");
            res.append(zhDic.get(String.valueOf(value - 10)));
            return res.toString();
        }
        String unit = "十百千万十百千亿";
        StringBuilder res1 = new StringBuilder();
        String value1 = String.valueOf(value);
        res1.append(zhDic.get(value1.charAt(value1.length() - 1) + ""));
        String s;
        char u;
        for (int i = value1.length() - 2,j = 0; i >= 0; i--,j++) {
            s = zhDic.get(value1.charAt(i) + "");
            u = unit.charAt(j % unit.length());
            if (s.equals("二") && (u == '百' || u == '千' ||  u == '万' || u == '亿')){
                s = "两";
            }
            if (!s.equals("零")){
                res1.append(u);
            }else if (u == '万' || u == '亿'){
                res1.append(u);
                continue;
            }
            res1.append(s);
        }
        res1.reverse();
        res.append(res1);
        String regex = "^零+|零+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(res);
        String finalStr = matcher.replaceAll("");
        regex = "零{2,}";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(finalStr);
        finalStr = matcher.replaceAll("零");
        return finalStr;
    }


    private static final Map<String, String> zhDic = new HashMap<String, String>() {
        {
            put("0", "零");
            put("1", "一");
            put("2", "二");
            put("3", "三");
            put("4", "四");
            put("5", "五");
            put("6", "六");
            put("7", "七");
            put("8", "八");
            put("9", "九");
            put("10", "十");
        }
    };

    private static final List<String> zhUnitList = new ArrayList<String>(){
        {
            add("十");
            add("百");
            add("千");
            add("万");
            add("十万");
            add("百万");
            add("千万");
        }
    };

    private static final Map<String, String> numDic = new HashMap<String, String>() {
        {
            put("零", "0");
            put("一", "1");
            put("二", "2");
            put("三", "3");
            put("四", "4");
            put("五", "5");
            put("六", "6");
            put("七", "7");
            put("八", "8");
            put("九", "9");
            put("十", "10");
            put("两", "2");
        }
    };

    private static int getUnitNum(String unit){
        switch (unit){
            case "十":
                return 10;
            case "百":
                return 100;
            case "千":
                return 1000;
            case "万":
                return 10000;
            case "十万":
                return 100000;
            case "百万":
                return 1000000;
            case "千万":
                return 10000000;
            default:
                return 0;
        }
    }
}
