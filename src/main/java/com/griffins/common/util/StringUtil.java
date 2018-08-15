package com.griffins.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.Character.UnicodeBlock;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class StringUtil.
 */
public class StringUtil {

    private static Logger LOG = LoggerFactory.getLogger(StringUtil.class);

    private static final int BUFFER_SIZE = 16 * 1024; // BUFFER_SIZE

    /**
     * Checks if is empty.
     *
     * @param str
     * @return true, if is empty
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.equalsIgnoreCase("null") || str.length() == 0);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;
        else if (obj instanceof String)
            return isEmpty(obj.toString());
        else
            return false;
    }

    /**
     * 주어진 문장열이 공백(빈칸,개행문자,탭문자)로만 이루어져 있는지 확인한다.
     *
     * @param str :검사할 문자열
     * @return 공백으로만 이루어졌다면 tue 아니면 false
     */
    public static boolean hasOnlySpace(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * if String is null or "".
     *
     * @param str
     * @return true/false
     */
    public static boolean isBlank(String str) {

        return (str == null || "".equals(str));
    }

    /**
     * return default value for integer if the object is null.
     *
     * @param i
     * @return the int
     */
    public static int nvlInt(Integer i, Integer b) {
        b = b == null ? 0 : b;
        return i == null ? b : i;
    }

    public static int nvlInt(Integer i) {
        return i == null ? 0 : i;
    }

    /**
     * if String is "" , return Default Value.
     *
     * @param str1
     * @param str2
     * @return the string
     */
    public static String defVal(String str1, String str2) {

        return ("".equals(nvl(str1)) ? str2 : str1);
    }

    /**
     * Def val int.
     *
     * @param int1
     * @param int2
     * @return the int
     */
    public static int defValInt(int int1, int int2) {
        return (nvlInt(int1) == 0 ? int2 : int1);
    }

    /**
     * Def val double.
     *
     * @param str
     * @return the double
     */
    public static double defValDouble(String str) {

        if ("".equals(nvl(str))) {

            return 0;

        } else {

            try {
                return new Double(str);
            } catch (NumberFormatException nfex) {
                return 0;
            }
        }
    }

    /**
     * if request.getParameter is null , return ""
     *
     * @param request
     * @param pname
     * @return "" or request.getParameter
     */
    public static String getParam(HttpServletRequest request, String pname) {
        return request.getParameter(pname);
    }

    /**
     * if request.getParameterValues is null , return []
     *
     * @param request
     * @param pname
     * @return "" or request.getParameterValues
     */
    public static String[] getParamValues(HttpServletRequest request, String pname) {
        return request.getParameterValues(pname);
    }

    public static String getParameter(HttpServletRequest request, String pname) {
        String tempValue = request.getParameter(pname);
        if (tempValue == null) {
            tempValue = "";
        }

        return tempValue;
    }

    /**
     * if request.getParameter is null , return 0
     *
     * @param request
     * @param pname
     * @return the param int
     */
    public static int getParamInt(HttpServletRequest request, String pname) {

        if (request.getParameter(pname) == null) {

            return 0;

        } else {

            try {
                return Integer.parseInt(request.getParameter(pname));
            } catch (NumberFormatException nfex) {
                return 0;
            }
        }

    }

    /**
     * Gets the params.
     *
     * @param request
     * @param pname
     * @return the params
     */
    @SuppressWarnings("unchecked")
    public static String getParams(HttpServletRequest request, String pname) {

        String values = "";
        Enumeration<String> paramNames = request.getAttributeNames();

        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();

            if (name.equals(pname)) {
                values = nvl((String) request.getAttribute(name));
                break;
            }
        }

        return values;
    }

    /**
     * The Constant HEX_DIGITS.
     */
    private static final String HEX_DIGITS = "0123456789abcdef";

    /**
     * 문자열이 null일때 ""를 리턴한다.
     *
     * @param obj the obj
     * @return String
     */
    public static String nvl(Object obj) {
        return nvl(obj, "");
    }

    /**
     * 문자열이 null일때 ""를 리턴한다.
     *
     * @param obj
     * @param ifNull
     * @return String
     */
    public static String nvl(Object obj, String ifNull) {
        return (obj != null && !obj.toString().equalsIgnoreCase("null")) ? obj.toString() : ifNull;
    }

    /**
     * HTML 문자들을 HTML에서 사용되는 특수문자로 변환한다. 대상 HTML 문자 : <, >, “, ‘, &, ⓒ
     *
     * @param src
     * @return String
     */
    public static String encodeScript(String src) {
        src = src.replaceAll("<", "&lt;");
        src = src.replaceAll(">", "&gt;");
        src = src.replaceAll("\"", "&quot;");
        src = src.replaceAll("'", "&#39;");
        src = src.replaceAll("&", "&amp;");
        src = src.replaceAll("ⓒ", "&#169;");
        return src;
    }

    /**
     * xss 공격에 사용되는 HTML 문자들을 특수문자로 변환한다.
     * decode 는 decodeScript 메소드를 사용하면 된다.
     *
     * @param src
     * @return
     */
    public static String encodeXss(String src) {
        return src.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\\(", "&#40;")
                .replaceAll("\\)", "&#41;")
                .replaceAll("'", "&#39;")
                .replaceAll("eval\\((.*)\\)", "")
                .replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
    }

    /**
     * HTML에서 사용되는 특수문자를 HTML 문자들로 변환한다. 대상 HTML 문자 : <, >, “, ‘, &, ⓒ
     *
     * @param src
     * @return String
     */
    public static String decodeScript(String src) {
        return src.replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("&#39;", "'")
                .replaceAll("&#169;", "ⓒ")
                .replaceAll("&#40;", "(")
                .replaceAll("&#41;", ")");
    }

    /**
     * String 타입이 아닌 경우 변환을 하지 않고 그대로 반환
     *
     * @param src
     * @return
     * @author 이재철
     * @date 2016. 11. 7.
     */
    public static Object decodeScript(Object src) {
        if (src != null && src instanceof String) {
            return decodeScript(src.toString());
        }
        return src;
    }

    public static String decodeSummernoteCode(String value) {
        return toString(value).replaceAll("[\n\r]", "").replaceAll("\"", "'");
    }

    public static String getOnlyFilename(String path) {
        if (isNotEmpty(path)) {
            if (path.contains("/")) {
                path = path.substring(path.lastIndexOf("/") + 1);
            }
            if (path.contains("\\")) {
                path = path.substring(path.lastIndexOf("\\") + 1);
            }
        }
        return path;
    }

    /**
     * JavaScript 변수전달시 스크립트 풀림 방지. 대상 HTML 문자 : " ' test('ad's') ->
     * test('ad&#146;s')
     *
     * @param src
     * @return String
     */
    public static String escapeJavaScriptParam(String src) {
        src = src.replaceAll("\"", "&quot;");
        src = src.replaceAll("'", "&#146;");
        return src;
    }

    /**
     * JavaScript 변수전달시 스크립트 풀림 방지 복원. 대상 HTML 문자 : " '
     *
     * @param src
     * @return String
     */
    public static String unescapeJavaScriptParam(String src) {
        src = src.replaceAll("&quot;", "\"");
        src = src.replaceAll("&#146;", "'");
        return src;
    }

    /**
     * 일반 텍스트에서 '\n' 문자를 HTML의 '<br>
     * ' 로 바꿔준다.
     *
     * @param src
     * @return String
     */
    public static String convertNewLineToBRTag(String src) {
        if (src != null) {
            return src.replaceAll("\n", "<br>");
        } else {
            return src;
        }
    }

    /**
     * 주어진 문자열에서 html tag를 모두 제가한 값을 반환한다.
     *
     * @param str
     * @return String
     */
    public static String removeAllHtmlTag(String str) {

        if (str == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();

        char[] c = str.toCharArray();
        int len = c.length;
        boolean inTag = false;
        for (int index = 0; index < len; index++) {
            if (!inTag) {
                if (c[index] == '<') {
                    inTag = true;
                } else {
                    buffer.append(c[index]);
                }
            } else {
                if (c[index] == '>') {
                    inTag = false;
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 문자열을 지정한 size 공간내에서 가운데로 정렬시킨다.
     *
     * @param str
     * @param size
     * @return String - 가운데로 정렬된 문자열.
     */
    public static String center(String str, int size) {
        return center(str, size, " ");
    }

    /**
     * 문자열을 지정한 size 공간내에서 가운데로 정렬시킨다.
     *
     * @param str
     * @param size
     * @param delim
     * @return String - 가운데로 정렬된 문자열.
     * @throws NullPointerException
     * @throws ArithmeticException
     */
    public static String center(String str, int size, String delim) {
        // int sz = str.length();
        // 한글 처리를 위해 다음의 코드로 바꿈.
        int sz = lengthHan(str);
        int p = size - sz;
        if (p < 1) {
            return str;
        }
        str = leftPad(str, sz + p / 2, delim);
        str = rightPad(str, size, delim);
        return str;
    }

    /**
     * str 을 포함하는 size 크기의 문자열을 만든다.<br>
     * str 의 length 가 size 보다 작다면 str 왼쪽 공간은 공백으로 채워진다.
     *
     * @param str
     * @param size
     * @return left padded String
     * @throws NullPointerException if str is null
     */
    public static String leftPad(String str, int size) {
        return leftPad(str, size, " ");
    }

    /**
     * str 을 포함하는 size 크기의 문자열을 만든다.<br>
     * str 의 length 가 size 보다 작다면 str 왼쪽 공간은 delim으로 채워진다.
     *
     * @param str
     * @param size
     * @param delim
     * @return left padded String
     * @throws NullPointerException - if str or delim is null
     * @throws ArithmeticException  - if delim is the empty string
     */
    public static String leftPad(String str, int size, String delim) {
        // size = (size - str.length()) / delim.length();
        // 한글 처리를 위해 다음의 코드로 바꿈.
        size = (size - lengthHan(str)) / lengthHan(delim);
        if (size > 0) {
            str = repeat(delim, size) + str;
        }
        return str;
    }

    /**
     * str 을 포함하는 size 크기의 문자열을 만든다. str 의 length 가 size 보다 작다면 str 오른쪽 공간은 공백으로
     * 채워진다.
     *
     * @param str  - 문자열.
     * @param size - 전체 문자열 크기.
     * @return right padded String
     * @throws NullPointerException if str is null.
     */
    public static String rightPad(String str, int size) {
        return rightPad(str, size, " ");
    }

    /**
     * Right pad a String with a specified string. Pad to a size of n.
     * <p>
     * str 을 포함하는 size 크기의 문자열을 만든다. str 의 length 가 size 보다 작다면 str 오른쪽 공간은
     * delim 문자열로 반복해 채워진다.
     *
     * @param str   문자열.
     * @param size  전체 문자열 크기.
     * @param delim 반복할 문자열.
     * @return right padded String
     * @throws NullPointerException if str or delim is null
     * @throws ArithmeticException  if delim is the empty string
     */
    public static String rightPad(String str, int size, String delim) {
        // size = (size - str.length()) / delim.length();
        // 한글 처리를 위해 다음의 코드로 바꿈.
        size = (size - lengthHan(str)) / lengthHan(delim);
        if (size > 0) {
            str += repeat(delim, size);
        }
        return str;
    }

    public static String rightPad2(String str, int size, String delim) {

        size = (size - str.length()) / lengthHan(delim);

        if (size > 0) {
            str += repeat(delim, size);
        }
        return str;
    }

    /**
     * 지정한 문자열에서 왼쪽 len 크기 만큼 잘라낸다.
     *
     * @param str - 원본 문자열.
     * @param len - 왼쪽에서 잘라낼 크기.
     * @return String - 잘라내어진 문자열.
     * @throws IllegalArgumentException if len is less than zero
     */
    public static String left(String str, int len) {
        if (len < 0) {
            throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
        }
        if ((str == null) || (str.length() <= len)) {
            return str;
        } else {
            return str.substring(0, len);
        }
    }

    /**
     * 지정한 문자열에서 오른쪽 len 크기 만큼 잘라낸다.
     *
     * @param str - 원본 문자열.
     * @param len - 왼쪽에서 잘라낼 크기.
     * @return String - 잘라내어진 문자열.
     * @throws IllegalArgumentException if len is less than zero
     */
    public static String right(String str, int len) {
        if (len < 0) {
            throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
        }
        if ((str == null) || (str.length() <= len)) {
            return str;
        } else {
            return str.substring(str.length() - len);
        }
    }

    /**
     * 지정한 문자열에서 pos 위치부터 len 크기까지 잘라낸다.
     *
     * @param str - 원본 문자열.
     * @param pos - 시작 위치.
     * @param len - 왼쪽에서 잘라낼 크기.
     * @return String - 잘라내어진 문자열.
     * @throws IndexOutOfBoundsException if pos is out of bounds
     * @throws IllegalArgumentException  if len is less than zero
     */
    public static String mid(String str, int pos, int len) {
        if ((pos < 0) || (str != null && pos > str.length())) {
            throw new StringIndexOutOfBoundsException("String index " + pos + " is out of bounds");
        }
        if (len < 0) {
            throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
        }
        if (str == null) {
            return null;
        }
        if (str.length() <= (pos + len)) {
            return str.substring(pos);
        } else {
            return str.substring(pos, pos + len);
        }
    }

    /**
     * 문자열에서 시작으로"[@"와 끝으로 "]"의 패턴으로 감싸진 패턴의 안에 있는 문자열의 값을 순서대로 1개씩 치환한다.
     *
     * @param source the source
     * @param args   the args
     * @return String
     */
    public static String replaceMessage(String source, List<String> args) {
        StringBuffer buffer = null;

        if (isEmpty(source) || args == null || args.size() == 0 || source.indexOf("[@") == -1 || source.indexOf("]") == -1) {
            return source;
        }

        buffer = new StringBuffer();

        for (String arg : args) {
            int strIdx = source.indexOf("[@");
            if (strIdx != -1) {
                buffer.append(source.substring(0, strIdx));
                int endIdx = source.indexOf("]");

                if (endIdx != -1) {
                    buffer.append(arg);
                    source = source.substring(endIdx + 1);
                }
            }
        }

        if (source.length() != 0) {
            buffer.append(source);
        }

        return buffer.toString();
    }

    /**
     * 문자열에서 시작으로"[@"와 끝으로 "]"의 패턴으로 감싸진 패턴의 안에 있는 문자열의 값을 치환한다.
     *
     * @param source the source
     * @param args   the args
     * @return String
     */
    public static String replaceMessage(String source, HashMap<String, String> args) {
        return replaceMessage(source, args, "[@", "]");
    }

    /**
     * 문자열에서 특정한 시작패턴과 특정한 끝패턴으로 감싸진 패턴의 안에 있는 문자열의 값을 치환한다.
     *
     * @param source            the source
     * @param args              the args
     * @param startBracePattern the start brace pattern
     * @param endBracePattern   the end brace pattern
     * @return String
     */
    public static String replaceMessage(String source, HashMap<String, String> args, String startBracePattern, String endBracePattern) {

        StringBuffer buffer = new StringBuffer();
        int position = 0;
        int markEndPos = 0;

        String remainder;
        String argname;
        String value;

        while (source.length() > 0) {
            position = source.indexOf(startBracePattern);
            if (position == -1) {
                buffer.append(source);
                break;
            }
            if (position != 0) {
                buffer.append(source.substring(0, position));
            }

            if (source.length() == position + 2) {
                break;
            }
            remainder = source.substring(position + 2);

            markEndPos = remainder.indexOf(endBracePattern);
            if (markEndPos == -1) {
                break;
            }

            argname = remainder.substring(0, markEndPos).trim();
            value = (String) args.get(argname);
            if (value != null) {
                buffer.append(value);
            }

            if (remainder.length() == markEndPos + 1) {
                break;
            }
            source = remainder.substring(markEndPos + 1);
        }

        return buffer.toString();
    }

    /**
     * repeat 만큼 반복된 문자열을 만든다.
     * <p>
     * e.g.)
     *
     * @param str    원본 문자열.
     * @param repeat 반복 횟수.
     * @return repeat 만큼 반복된 문자열.
     */
    public static String repeat(String str, int repeat) {
        StringBuffer buffer = new StringBuffer(repeat * str.length());
        for (int index = 0; index < repeat; index++) {
            buffer.append(str);
        }
        return buffer.toString();
    }

    /**
     * 문자열에서 지정한 문자를 기준으로 최대 max 만큼 분리한다.
     *
     * @param inputStr 지정한 문자열.
     * @param divider  기준문자.
     * @param max      분리할 최대 수.
     * @return separator 를 기준으로 max 개까지 분리된 String 배열.
     */
    public static String[] split(String inputStr, String divider, int max) {

        StringTokenizer tokenizer = null;

        // Divider가 null이면 divider를 사용하지 않음.GeneralRule사용
        if (divider == null) {
            tokenizer = new StringTokenizer(inputStr);
        } else {
            tokenizer = new StringTokenizer(inputStr, divider);
        }

        int tokenSize = tokenizer.countTokens();

        int listSize;
        if (max > 0 && tokenSize > max) {
            listSize = max;
        } else {
            listSize = tokenSize;
        }

        String[] returnList = new String[listSize];

        int tokenStartIdx = 0;
        int tokenEndIdx = 0;

        for (int i = 0; tokenizer.hasMoreTokens(); i++) {

            returnList[i] = tokenizer.nextToken();
            tokenStartIdx = inputStr.indexOf(returnList[i], tokenEndIdx);
            tokenEndIdx = tokenStartIdx + returnList[i].length();

            // 마지막 token 인 경우
            if (i == listSize - 1) {

                // tokenSize보다 listSize가 작을 경우 나머지 String 전부를 resultList[i]에 저장
                if (listSize < tokenSize) {
                    returnList[i] = inputStr.substring(tokenStartIdx);
                }
                break;
            }

        }

        return returnList;
    }

    /**
     * 해당 문자열이 모두 알파벳인지 확인.
     *
     * @param str 검사할 문자열.
     * @return 숫자/공백문자/특수문자(\n\r...) 문자는 false.
     */
    public static boolean isAlpha(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int index = 0; index < sz; index++) {
            if (Character.isLetter(str.charAt(index)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 해당 문자열이 모두 숫자인지 확인.
     *
     * @param str 검사할 문자열.
     * @return 한/영문/공백문자/특수문자(\n\r...) 문자는 false.
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int index = 0; index < sz; index++) {
            if (Character.isDigit(str.charAt(index)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 해당 문자열이 모두 알파벳 이거나 숫자인지 확인. 단, _ 가능
     *
     * @param str 검사할 문자열.
     * @return 한/공백문자/특수문자(\n\r...) 문자는 false.
     */
    public static boolean isAlphaOrDigit(String str) {

        String regex = "[a-zA-Z0-9 *\\_\\.]+";

        if (str == null) {
            return false;
        }

        return str.matches(regex);
    }

    /**
     * 해당 문자열이 모두 알파벳 이거나 숫자인지 확인.
     *
     * @param str 검사할 문자열.
     * @return 한/공백문자/특수문자(\n\r...) 문자는 false.
     */
    public static boolean isAlphaOrDigitOrSpecialLetter(String str) {

        String regex = "[a-zA-Z0-9 *\\_([#;*{}<>&]|\\.-:/)+]+";

        if (str == null) {
            return false;
        }

        /*
         * str.matches(regex); if( str.matches(regex)){ return false; }
         */

        /*
         * int sz = str.length(); for ( int index = 0 ; index < sz ; index++ ) {
         * if ( Character.isLetterOrDigit(str.charAt(index)) == false ) { return
         * false; } }
         */

        return str.matches(regex);
    }

    /**
     * 문자열이 비어 있지 않은지 확인.
     *
     * @param str 원본 문자열.
     * @return true if the String is non-null, and not length zero
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.equalsIgnoreCase("null") && str.length() > 0;
    }

    public static boolean isNotEmpty(Object obj) {
        if (obj == null)
            return false;
        else if (obj instanceof String)
            return isNotEmpty(obj.toString());
        else
            return true;
    }

    /**
     * 문자열을 특정길이만큰 축약하여 ... 붙여주는 메소드
     *
     * @param str    the str
     * @param maxNum the max num
     * @return String
     */
    public static String contractString(String str, int maxNum) {
        int tLen = str.length();
        int count = 0;
        char c;
        int index = 0;
        for (index = 0; index < tLen; index++) {
            c = str.charAt(index);
            if (count >= maxNum) {
                break;
            }
            if (c > 127) {
                count += 2;
            } else {
                count++;
            }
        }
        return (tLen > index) ? str.substring(0, index) + "..." : str;
    }

    /**
     * Byte 배열 문자열을 헥사코드로 변환.
     *
     * @param digest the digest
     * @return String
     */
    public static String toHex(byte[] digest) {
        StringBuffer sb = new StringBuffer(digest.length * 2);
        for (int index = 0; index < digest.length; index++) {
            int b = digest[index] & 0xFF;
            sb.append(HEX_DIGITS.charAt(b >>> 4)).append(HEX_DIGITS.charAt(b & 0xF));
        }
        return sb.toString();
    }

    /**
     * 주어진 문자열을 StringTokenizer해서 List<String>형태로 반환.
     *
     * @param source    the source
     * @param separater the separater
     * @return List<String>
     */
    public static List<String> tokenize(String source, String separater) {
        StringTokenizer t = (separater != null) ? new StringTokenizer(source, separater) : new StringTokenizer(source);

        List<String> list = new ArrayList<String>();
        while (t.hasMoreElements()) {
            list.add((String) t.nextElement());
        }
        return list;
    }

    /**
     * String의 순서를 뒤집어준다.
     *
     * @param str the str
     * @return String
     */
    public static String reverse(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(str);
        return sb.reverse().toString();
    }

    /**
     * 입력 받은문자열의 제일 첫글자를 대문자로 만든다.
     * <p>
     * 영문일 경우에만 변환된다.
     *
     * @param str 문자열
     * @return String 변환된 문자열.
     */
    public static String capitalize(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return "";
        }
        return new StringBuffer(str.length()).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    /**
     * 한/영문이 같이 들어 있는 문자열의 길이를 계산한다.
     * <p>
     * Java에서의 String.length() 메소드는 한글을 1 자리 로 처리하기 때문에 한글을 2 자리로 계산할 경우에 사용된다.
     * <p>
     *
     * @param str 길이를 구할 문자열.
     * @return int 문자열의 길이
     */
    public static int lengthHan(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int length = str.length();
        int strLength = 0;
        char c;
        for (int index = 0; index < length; index++) {
            c = str.charAt(index);
            if (c < 0xac00 || 0xd7a3 < c) {
                strLength++;
            } else {
                strLength += 2;
            }
        }
        return strLength;
    }

    /**
     * 한/영 혼용 문자열의 길이를 check
     *
     * @param str       문자열
     * @param maxLength 최대 길이
     * @return boolean
     */
    public static boolean checkLength(String str, int maxLength) {
        if (lengthHan(str) > maxLength) {
            return false;
        }

        return true;
    }

    /**
     * 문자열내에 해당 pattern 이 들어있는지 check
     *
     * @param str     문자열
     * @param pattern 특수문자
     * @return boolean
     */
    public static boolean checkSpecialChars(String str, String pattern) {
        if (str.indexOf(pattern) > 0) {
            return false;
        }

        return true;
    }

    /**
     * 문자열내에 공백이 들어있는지 check
     *
     * @param str 문자열
     * @return boolean
     */
    public static boolean checkSpace(String str) {
        return checkSpecialChars(str, " ");
    }

    /**
     * Gets the VARCHAR2 ( for UTF-8 Oracle DB ) length.
     *
     * @param str the str
     * @return the varchar2 length
     */
    public static int varchar2Length(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }

        int length = 0;

        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) >= 0 && str.charAt(i) < 256) {
                length += 1;
            } else {
                length += 3;
            }
        }

        return length;
    }

    /**
     * Checks if is not blank.
     *
     * @param str the str
     * @return true, if is not blank
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 문자와 숫자가 섞인 문자열에서 숫자만 반환하는 메소드.
     *
     * @param str
     * @return String / 숫자만 남은 문자열
     */
    public static String toNumeralString(String str) {
        Pattern pattern = Pattern.compile("\\D");
        Matcher matcher = pattern.matcher(str);
        str = matcher.replaceAll("");
        return str == null ? "" : str;
    }

    /**
     * 문자열 Validation 검사
     * <p>
     * <pre>
     * 문자열이 빈 문자열인지 검사한다.
     * </pre>
     *
     * @param String str / Validation Check 문자열
     * @return boolean / 적합한 문자열 : true, 잘못된 문자열 : false
     */
    public static boolean CheckValidationText(String str) {
        return CheckValidationText(str, 0, 0);
    }

    /**
     * 문자열 Validation 검사
     * <p>
     * <pre>
     * minSize == 0 : 최소 문자열 사이즈 무시
     * maxSize == 0 : 최대 문자열 사이즈 무시
     * </pre>
     *
     * @param String str / Validation Check 문자열
     * @param int    minSize / 최소 문자열 길이 제한
     * @param int    maxSize / 최대 문자열 길이 제한
     * @return boolean / 적합한 문자열 : true, 잘못된 문자열 : false
     */
    public static boolean CheckValidationText(String str, int minSize, int maxSize) {
        boolean chkFlag = true;
        if (isNotBlank(str)) {
            int length = lengthHan(str);
            if ((minSize != 0 && length < minSize) || (maxSize != 0 && maxSize < length)) {
                chkFlag = false;
            }
        } else {
            chkFlag = false;
        }
        return chkFlag;
    }

    /**
     * Text 개행문자를 &lt;BR/> TAG로 변경
     *
     * @param String txt / 변환할 문자열
     * @return String / &lt;BR/> 추가된 문자열
     */
    public static String addBrTag(String txt) {
        if (txt != null && txt.length() > 0) {
            return txt.replaceAll("\\n", "<BR/> \n");
        } else {
            return "";
        }
    }

    /**
     * 문자열 Validation 검사
     * <p>
     * <pre>
     * minSize == 0 : 최소 문자열 사이즈 무시
     * maxSize == 0 : 최대 문자열 사이즈 무시
     * </pre>
     *
     * @param String str / Validation Check 문자열
     * @param int    minSize 최소 문자열 길이 제한
     * @param int    maxSize 최대 문자열 길이 제한
     * @return boolean / 적합한 문자열 : true, 잘못된 문자열 : false
     */
    public static boolean CheckValidationNumber(String str, int minSize, int maxSize) {
        return CheckValidationText(toNumeralString(str), minSize, maxSize);
    }

    /**
     * E-mail 유효성 검사
     *
     * @param String email / 유효성 검사 대상 E-mail 주소
     * @return boolean / 적합한 E-mail : true, 잘못된 E-mail : false
     */
    public static boolean isValidEmail(String email) {

        if (CheckValidationText(email, 5, 50)) {

            /** .html,*.gif,*.jpg,*.js,*.css,*.png,*.swf,*.flv,*.xml,*.jar */
            String[] noPermitPostfix = {".html", ".gif", ".jpg", ".js", ".css", ".png", ".swf", ".flv", ".xml", ".jar"};
            if (email != null) {
                for (int i = 0; i < noPermitPostfix.length; i++) {
                    if (email.toLowerCase().endsWith(noPermitPostfix[i])) {
                        return false;
                    }
                }
            }

            Pattern p = Pattern.compile("^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$");
            Matcher m = p.matcher(email);
            return m.matches();
        } else {
            return false;
        }
    }

    /**
     * A-Store Password 유효성 검사
     *
     * @param String password / 유효성 검사 대상 Password
     * @return boolean / 적합한 Password : true, 잘못된 Password : false
     */
    public static boolean isVaildPassword(String password) {
        return isVaildPassword(password, null);
    }

    /**
     * B-Store Password 유효성 검사
     *
     * @param String pw / 유효성 검사 대상 Password
     * @param String id / 비교 대상 ID
     * @return boolean / 적합한 Password : true, 잘못된 Password : false
     */
    public static boolean isVaildPassword(String pw, String id) {
        // 특수문자 및 길이
        //Pattern p = Pattern.compile("^[\\w+!+@+*+$+^+%+&]{8,15}$");
        Pattern p = Pattern.compile("^[\\w~`!@#[$]%\\^&[*]\\(\\)-=[+][|]\\\\\\{\\}\\[\\]'\"<,>.\\?]{8,15}$");
        Matcher m = p.matcher(pw);
        if (!m.matches()) {
            return false;
        }

        Pattern pnum = Pattern.compile("[\\d]+"); // 0-9
        Pattern palpha = Pattern.compile("[a-zA-Z]+"); // a-zA-Z


        if (pnum.matcher(pw).matches() || palpha.matcher(pw).matches()) {
            return false;
        }

        // 동일문자 3회연속 불가
        if (!isSameCharacters(pw)) {
            return false;
        }

        // 순차증가 3자리 숫자 불가
        if (!isSequentialNumbers(pw)) {
            return false;
        }

        // ID 포함여부
        if (isNotEmpty(id)) {
            if (pw.indexOf(id) > -1) {
                return false;
            }
        }

        return true;
    }

    private static boolean isSameCharacters(String password) {

        int strSize = password.length();

        for (int i = 0; i < strSize; i++) {
            int sameCount = 0;
            char searchChar = password.charAt(i);

            int charPos = 0;
            while (charPos != -1) {
                charPos = password.indexOf(searchChar, charPos);
                if (charPos != -1) {
                    charPos++;
                    sameCount++;
                    if (sameCount > 2) {
                        return false;
                    }
                }
            }

        }
		/*char[] chars = new char[password.length() ];
		for (int i = 0; i < password.length(); i++) {
			chars[i] = password.charAt(i);
		}
		for (char c : chars) {

			int count = 0;

			Pattern p = Pattern.compile(String.valueOf(c) );
			Matcher m = p.matcher(password);

			for (int i = 0; m.find(i); i = m.end() ) {
				count++;
			}

			if (count > 2) {
				return false;
			}
		}*/

        return true;
    }

    private static boolean isSequentialNumbers(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            String subPw = password.substring(i, i + 3);
            if (isNumeric(subPw) == false) {
                continue;
            }

            int a = Integer.parseInt(subPw.substring(0, 1));
            int b = Integer.parseInt(subPw.substring(1, 2));
            int c = Integer.parseInt(subPw.substring(2, 3));

            if (b - a == 1 && c - b == 1) {
                return false;
            } else if (a - b == 1 && b - c == 1) {
                return false;
            }
        }

        return true;
    }

    /* 추가 by 이재철 */
    public static String toString(Object obj) {
        if (obj == null)
            return "";
        return String.valueOf(obj);
    }

    public static String toString(Object obj, String isNullString) {
        if (obj == null)
            return isNullString;
        return String.valueOf(obj);
    }

    /**
     * 클라이언트의 콘솔에 출력할 수 있도록 커테이션 기호를 변경한다.
     *
     * @param src
     * @return
     */
    public static String toClientString(String src) {
        if (StringUtil.isNotEmpty(src)) {
            return src.replaceAll("\"", "'").replaceAll("\\n", "\\\\n");
        } else {
            return src;
        }
    }

    public static boolean isSame(Object a, Object b) {
        return toString(a).equals(toString(b));
    }

    public static boolean isSameIgnoreCase(Object a, Object b) {
        return toString(a).equalsIgnoreCase(toString(b));
    }
    /* 추가 끝 */

    /**
     * 입력된 숫자를 자리수에 맞는 문자열을 출력한다.
     * <p>
     * <pre>
     * 예: intToString(324, 5) = 00324
     * </pre>
     *
     * @param int number / 입력 숫자
     * @param int length / 자리수
     * @return String
     */
    public static String intToString(int number, int length) {
        String strNum = Integer.toString(number);
        return leftPad(strNum, length, "0");
    }

    /**
     * 입력된 Double 을 지정된 Format 형식의 String 반환한다.
     * <p>
     * <pre>
     * 예: dblToString(64550.0, &quot;#############.00&quot;) = 00324
     * </pre>
     *
     * @param number double value
     * @param format the format "#############.00"
     * @return the string
     */
    public static String dblToString(double number, String format) {

        DecimalFormat df = new DecimalFormat(format);

        return df.format(number);
    }

    /**
     * 숫자를 생월(MM), 생일(DD) 문자열로 변환한다.
     *
     * @param int number / 입력 숫자
     * @return String / 변환된 문자열
     */
    public static String toBirthText(int number) {
        return intToString(number, 2);
    }

    /**
     * 구분자 사이에 empty space 를 넣어서 split 한다.
     *
     * @param String
     * @param String
     * @return String / 구분된 문자열배열
     */
    public static String[] split(String dest, String regex) {
        if (dest == null) {
            return null;
        } else {
            if (dest.indexOf(regex) != -1) {
                /* 공백을 앞뒤에 붙여 split 하고 나중에 trim() 한다. */
                String[] ar = dest.replaceAll(regex, " " + regex + " ").split(regex);
                int index = 0;
                for (String s : ar) {
                    // System.out.println("################## ar.length="+ar.length+"/ar[index]="+ar[index]+"/index="+index);
                    ar[index++] = s.trim();
                }
                return ar;
            } else {
                return new String[]{dest};
            }
        }
    }

    /**
     * 메일 전송 메시지에서 메일 전송 API에서 금지하고 있는 특수문자를 HTML코드값으로 치환한다.
     *
     * @param msg the msg / 메일 전송용 원본 메일 메시지 내용
     * @return the string / 특수문자 치환이 완료된 메일 메시지 내용
     */
    public static String replaceMailMessage(String msg) {
        /*
         * 사용금지문자: "%", "--", "..", "'", "\\", script, </script, <SCRIPT,
         * </SCRIPT
         */
        msg = msg.replace("%", "&#37;");
        msg = msg.replace("--", "&#45;&#45;");
        msg = msg.replace("..", "&#46;&#46;");
        msg = msg.replace("'", "&#39;");
        // msg = msg.replace("\\\\", "&#92;&#92;"); /* "\\"는 현재 허용되고 있다.*/
        msg = msg.replace("script", "&#115;&#99;&#114;&#105;&#112;&#116;");
        msg = msg.replace("SCRIPT", "&#83;&#67;&#82;&#73;&#80;&#84;");

        return msg;
    }

    /**
     * 입력된 문자로 시작되는 문자열인지 확인
     *
     * @param str
     * @param prefix
     * @return
     */
    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }

    /**
     * 입력된 문자로 시작되는 문자열인지 확인
     *
     * @param str
     * @param prefix
     * @return
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    /**
     * 입력된 문자로 시작되는 문자열인지 확인
     *
     * @param str
     * @param prefix
     * @param ignoreCase
     * @return
     */
    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return str == null && prefix == null;
        }
        if (prefix.length() > str.length()) {
            return false;
        } else {
            return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
        }
    }

    public static String trim(String s) {
        if (s == null) {
            return "";
        } else {
            return s.trim();
        }
    }

    /**
     * 자바스크립트의 join 메소드와 동일한 기능
     *
     * @param 배열값
     * @param split문자열
     * @return join된문자열
     */
    public static String join(String[] array, String splitString) {
        if (array == null) {
            return "";
        }
        String result = "";
        for (int i = 0; i < array.length; i++) {
            result += trim(array[i]);
            if (i < array.length - 1) {
                result += splitString;
            }
        }
        return result;
    }

    /**
     * (역)허용되지 않은 특수문자를 가능한 문자로 치환하여 교체한다.
     *
     * @param replace
     * @return 치환된 문자열
     */
    public static String deReplace(String replaceStr) {
        if (replaceStr == null) {
            return "";
        }
        replaceStr = replaceStr.replaceAll("0xxx01", "#");
        replaceStr = replaceStr.replaceAll("0xxx02", ";");
        replaceStr = replaceStr.replaceAll("0xxx03", "\\\\");
        replaceStr = replaceStr.replaceAll("0xxx04", "*");
        replaceStr = replaceStr.replaceAll("0xxx05", "{");
        replaceStr = replaceStr.replaceAll("0xxx06", "}");
        replaceStr = replaceStr.replaceAll("0xxx07", "<");
        replaceStr = replaceStr.replaceAll("0xxx08", ">");
        replaceStr = replaceStr.replaceAll("0xxx09", "&");
        replaceStr = replaceStr.replaceAll("0xxx10", "\"");
        replaceStr = replaceStr.replaceAll("0xxx11", "'");
        replaceStr = replaceStr.replaceAll("0xxx12", "-");
        replaceStr = replaceStr.replaceAll("0xxx13", "/");
        replaceStr = replaceStr.replaceAll("0xxx14", "&nbsp;");
        return replaceStr;
    }

    /**
     * 문자열 속의 특수알파벳들을 Escape Sequence Text로 변환
     * <p>
     * <pre>
     * 공지사항 조회시 특수알파벳에 대한 검색어를 Escape Sequence Text
     * 변환하여 검색하기 위해 사용.
     * </pre>
     *
     * @param src
     * @return 치환된 문자열
     */
    public static String toEscapeSequenceText(String src) {
        if (src != null) {
            src = src.replaceAll("À", "&Agrave;");
            src = src.replaceAll("Á", "&Aacute;");
            src = src.replaceAll("Â", "&Acirc;");
            src = src.replaceAll("Ã", "&Atilde;");
            src = src.replaceAll("Ä", "&Auml;");
            src = src.replaceAll("Å", "&Aring;");
            src = src.replaceAll("Æ", "&AElig;");
            src = src.replaceAll("Ç", "&Ccedil;");
            src = src.replaceAll("È", "&Egrave;");
            src = src.replaceAll("É", "&Eacute;");
            src = src.replaceAll("Ê", "&Ecirc;");
            src = src.replaceAll("Ë", "&Euml;");
            src = src.replaceAll("Ì", "&Igrave;");
            src = src.replaceAll("Í", "&Iacute;");
            src = src.replaceAll("Î", "&Icirc;");
            src = src.replaceAll("Ï", "&Iuml;");
            src = src.replaceAll("Ð", "&ETH;");
            src = src.replaceAll("Ñ", "&Ntilde;");
            src = src.replaceAll("Ò", "&Ograve;");
            src = src.replaceAll("Ó", "&Oacute;");
            src = src.replaceAll("Ô", "&Ocirc;");
            src = src.replaceAll("Õ", "&Otilde;");
            src = src.replaceAll("Ö", "&Ouml;");
            src = src.replaceAll("×", "&times;");
            src = src.replaceAll("Ø", "&Oslash;");
            src = src.replaceAll("Ù", "&Ugrave;");
            src = src.replaceAll("Ú", "&Uacute;");
            src = src.replaceAll("Û", "&Ucirc;");
            src = src.replaceAll("Ü", "&Uuml;");
            src = src.replaceAll("Ý", "&Yacute;");
            src = src.replaceAll("Þ", "&THORN;");
            src = src.replaceAll("ß", "&szlig;");
            src = src.replaceAll("à", "&agrave;");
            src = src.replaceAll("á", "&aacute;");
            src = src.replaceAll("â", "&acirc;");
            src = src.replaceAll("ã", "&atilde;");
            src = src.replaceAll("ä", "&auml;");
            src = src.replaceAll("å", "&aring;");
            src = src.replaceAll("æ", "&aelig;");
            src = src.replaceAll("ç", "&ccedil;");
            src = src.replaceAll("è", "&egrave;");
            src = src.replaceAll("é", "&eacute;");
            src = src.replaceAll("ê", "&ecirc;");
            src = src.replaceAll("ë", "&euml;");
            src = src.replaceAll("ì", "&igrave;");
            src = src.replaceAll("í", "&iacute;");
            src = src.replaceAll("î", "&icirc;");
            src = src.replaceAll("ï", "&iuml;");
            src = src.replaceAll("ð", "&eth;");
            src = src.replaceAll("ñ", "&ntilde;");
            src = src.replaceAll("ò", "&ograve;");
            src = src.replaceAll("ó", "&oacute;");
            src = src.replaceAll("ô", "&ocirc;");
            src = src.replaceAll("õ", "&otilde;");
            src = src.replaceAll("ö", "&ouml;");
            src = src.replaceAll("÷", "&divide;");
            src = src.replaceAll("ø", "&oslash;");
            src = src.replaceAll("ù", "&ugrave;");
            src = src.replaceAll("ú", "&uacute;");
            src = src.replaceAll("û", "&ucirc;");
            src = src.replaceAll("ü", "&uuml;");
            src = src.replaceAll("ý", "&yacute;");
            src = src.replaceAll("þ", "&thorn;");
        }
        return src;
    }

    public static boolean checkFacetField(String str) {

        boolean result = false;

        if (str.equals("category") || str.equals("contentType") || str.equals("serviceProvider")) {
            result = true;
        }

        return result;
    }

    /**
     * 해당 문자열이 Field 값으로 유효한지 검사한다
     *
     * @param str
     * @return 유효하지 않을 경우 문자는 false.
     */
    public static boolean checkField(String str) {

        boolean result = false;

        if (str.equals("title") || str.equals("actor") || str.equals("director") || str.equals("genre") || str.equals("serviceProvider") || str.equals("all") || str.equals("people")) {
            result = true;
        }

        return result;
    }

    /**
     * 해당 문자열이 AutoCompleteField 값으로 유효한지 검사한다
     *
     * @param str
     * @return 유효하지 않을 경우 문자는 false.
     */
    public static boolean checkAutoCompleteField(String str) {

        boolean result = false;


        if (str.equals("title") || str.equals("actor") || str.equals("director") || str.equals("all") || str.equals("people")) {
            result = true;
        }

        return result;
    }

    /**
     * 해당 문자열이 MediaRating값으로 유효한지 검사한다
     *
     * @param str 검사할 문자열.
     * @return 유효하지 않을 경우 문자는 false.
     */
    public static boolean checkMediaRating(String str) {

        String regex = "\\[(-?[1-9]?[0-9]?|\\*) TO (-?[1-9]?[0-9]?|\\*)\\]";

        if (str == null) {
            return false;
        }

        return str.matches(regex);
    }

    /**
     * 한글이 포함되어 있는지 확인.
     *
     * @param str
     * @return
     */
    public static boolean containsKoreanLanguage(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            UnicodeBlock unicodeBlock = UnicodeBlock.of(ch);
            if (UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock)
                    || UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock)
                    || UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 입력받은 문자열의 Null여부를 체크하고 널일 경우 ""문자열을 반환한다.
     * <p>
     *
     * @param psParam
     * @return
     */
//	public static String checkNull(String psParam) {
//		if (psParam == null) {
//			return "";
//		}
//
//		return psParam;
//	}


    /**
     * @param filename
     * @return
     */
    public static String getFilenameWithoutExstension(String filename) {
        int pos = filename.lastIndexOf(".");
        if (pos >= 0 && pos < filename.length()) {
            return filename.substring(0, pos);
        } else {
            return "";
        }
    }


    /**
     * 파일 크기를 가져옵니다
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception {

        long s = 0;
        FileInputStream fis = null;

        try {
            if (f.exists()) {
                fis = new FileInputStream(f);
                s = fis.available();
            } else {
                f.createNewFile();
                System.out.println("파일이 존재하지 않습니다");
            }
        } finally {
            fis.close();
        }

        return s;
    }

    /**
     * 파일 확장자 명 가져오기
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String getExtention(String fileName) {
        int pos = nvl(fileName).lastIndexOf(".");
        return fileName.substring(pos > -1 ? pos : 0);
    }

    public static String getFilenameOnly(String fileName) {
        int pos = nvl(fileName).lastIndexOf(".");
        return fileName.substring(0, pos > -1 ? pos : 0);
    }

    public static String getExtentionOnly(String fileName) {
        int pos = nvl(fileName).lastIndexOf(".");
        return fileName.substring(pos > -1 ? pos + 1 : 0);
    }

    public static String getImagePath(String fileName) {
        return "image/" + fileName.replaceFirst("(.+)\\.([^\\.]+)$", "$1/$2");
    }


    /**
     * 파일 복사
     *
     * @param src
     * @param dst
     */
    public static void copy(File src, File dst) {
        System.out.println("000000   src dst " + src.getPath() + "   "
                + dst.getPath());
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src),
                        BUFFER_SIZE);
                System.out.println("=== in === [" + in.toString());
                out = new BufferedOutputStream(new FileOutputStream(dst),
                        BUFFER_SIZE);
                System.out.println("=== out === [" + out.toString());

                // 업로드 된 파일은 1 KB 미만의 최소 또는 찾을 수 없습니다 오류 파일이 될 수 없습니다
                byte[] buffer = new byte[BUFFER_SIZE];
                while (in.read(buffer) > 0) {
                    out.write(buffer);
                }
                System.out.println("=== end === ");
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ****************************************************
     * byte단위로 문자열을 자르기.
     *
     * @param str
     * @param map
     * @return 잘려진문자열
     * @throws UnsupportedEncodingException ****************************************************
     */
    public static String getStringByte(String str, int iLimitSize) {

        return strCut(str, iLimitSize, 0);

    }

    /**
     * ****************************************************
     * byte단위로 문자열을 자르기.
     *
     * @param str
     * @param map
     * @return 잘려진문자열
     * @throws UnsupportedEncodingException ****************************************************
     */
    public static String strCut(String szText, int nLength, int nPrev) {  // 문자열 자르기

        String reVal = szText;

        int oF = 0, oL = 0, rF = 0, rL = 0;

        int nLengthPrev = 0;

//		System.out.println("\n ==================================szText[" + szText);       
        if (szText == null) {
            return "";
        }
        if (szText.trim().equals("")) {
            return "";
        }

        reVal = reVal.replaceAll("&amp;", "&");

        reVal = reVal.replaceAll("(!/|\r|\n|&nbsp;)", "");  // 공백제거

        try {

            byte[] bytes = reVal.getBytes("UTF-8");     // 바이트로 보관
            //
            //           if(szKey != null && !szKey.equals("")) {
            //
            //               nLengthPrev = (reVal.indexOf(szKey) == -1)? 0: reVal.indexOf(szKey);  // 일단 위치찾고
            //
            //               nLengthPrev = reVal.substring(0, nLengthPrev).getBytes("MS949").length; // 위치까지길이를 byte로 다시 구한다
            //
            //               nLengthPrev = (nLengthPrev-nPrev >= 0)? nLengthPrev-nPrev:0;    // 좀 앞부분부터 가져오도록한다.
            //
            //           }

            // x부터 y길이만큼 잘라낸다. 한글안깨지게.

            int j = 0;

            if (nLengthPrev > 0) while (j < bytes.length) {

                if ((bytes[j] & 0x80) != 0) {

                    oF += 2;
                    rF += 3;

                    if (oF + 2 > nLengthPrev) {
                        break;
                    }
                    j += 3;

                } else {
                    if (oF + 1 > nLengthPrev) {
                        break;
                    }
                    ++oF;
                    ++rF;
                    ++j;
                }

            }

            j = rF;

            while (j < bytes.length) {

                if ((bytes[j] & 0x80) != 0) {

                    if (oL + 2 > nLength) {
                        break;
                    }
                    oL += 2;
                    rL += 3;
                    j += 3;

                } else {
                    if (oL + 1 > nLength) {
                        break;
                    }
                    ++oL;
                    ++rL;
                    ++j;
                }

            }

            reVal = new String(bytes, rF, rL, "UTF-8");  // charset 옵션

            //           if(isAdddot && rF+rL+3 <= bytes.length) {
            //           	reVal+="...";
            //       	}  // ...을 붙일지말지 옵션

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return reVal;
    }


    public static char[] arrChoSung = {0x3131, 0x3132, 0x3134, 0x3137, 0x3138,
            0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148,
            0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};
    /**
     * 중성 - 가(ㅏ), 야(ㅑ), 뺨(ㅑ)
     */
    public static char[] arrJungSung = {0x314f, 0x3150, 0x3151, 0x3152,
            0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a,
            0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162,
            0x3163};
    /**
     * 종성 - 가(없음), 갈(ㄹ) 천(ㄴ)
     */
    public static char[] arrJongSung = {0x0000, 0x3131, 0x3132, 0x3133,
            0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c,
            0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145,
            0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};


    /* **********************************************
     * 알파벳으로 변환
     * 설연수 -> tjfdustn, 멍충 -> ajdcnd
     * **********************************************/
    /**
     * 초성 - 가(ㄱ), 날(ㄴ) 닭(ㄷ)
     */
    public static String[] arrChoSungEng = {"r", "R", "s", "e", "E",
            "f", "a", "q", "Q", "t", "T", "d", "w",
            "W", "c", "z", "x", "v", "g"};

    /**
     * 중성 - 가(ㅏ), 야(ㅑ), 뺨(ㅑ)
     */
    public static String[] arrJungSungEng = {"k", "o", "i", "O",
            "j", "p", "u", "P", "h", "hk", "ho", "hl",
            "y", "n", "nj", "np", "nl", "b", "m", "ml",
            "l"};

    /**
     * 종성 - 가(없음), 갈(ㄹ) 천(ㄴ)
     */
    public static String[] arrJongSungEng = {"", "r", "R", "rt",
            "s", "sw", "sg", "e", "f", "fr", "fa", "fq",
            "ft", "fx", "fv", "fg", "a", "q", "qt", "t",
            "T", "d", "w", "c", "z", "x", "v", "g"};

    /**
     * 단일 자음 - ㄱ,ㄴ,ㄷ,ㄹ... (ㄸ,ㅃ,ㅉ은 단일자음(초성)으로 쓰이지만 단일자음으론 안쓰임)
     */
    public static String[] arrSingleJaumEng = {"r", "R", "rt",
            "s", "sw", "sg", "e", "E", "f", "fr", "fa", "fq",
            "ft", "fx", "fv", "fg", "a", "q", "Q", "qt", "t",
            "T", "d", "w", "W", "c", "z", "x", "v", "g"};

    public static String koToEng(String word) {
        String resultEng = "";
        for (int i = 0; i < word.length(); i++) {

            char chars = (char) (word.charAt(i) - 0xAC00);

            if (chars >= 0 && chars <= 11172) {    /* A. 자음과 모음이 합쳐진 글자인경우 */
                /* A-1. 초/중/종성 분리 */
                int chosung = chars / (21 * 28);
                int jungsung = chars % (21 * 28) / 28;
                int jongsung = chars % (21 * 28) % 28;

                /* 알파벳으로 */
                resultEng = resultEng + arrChoSungEng[chosung] + arrJungSungEng[jungsung];
                if (jongsung != 0x0000) {
                    /* A-3. 종성이 존재할경우 result에 담는다 */
                    resultEng = resultEng + arrJongSungEng[jongsung];
                }

            } else {    /* B. 한글이 아니거나 자음만 있을경우 */
                /* 알파벳으로 */
                if (chars >= 34097 && chars <= 34126) {
                    /* 단일자음인 경우 */
                    int jaum = (chars - 34097);
                    resultEng = resultEng + arrSingleJaumEng[jaum];
                } else if (chars >= 34127 && chars <= 34147) {
                    /* 단일모음인 경우 */
                    int moum = (chars - 34127);
                    resultEng = resultEng + arrJungSungEng[moum];
                } else {
                    /* 알파벳인 경우 */
                    resultEng = resultEng + ((char) (chars + 0xAC00));
                }
            }

        }
        return resultEng;
    }

    public static String convertHangul(String money) {
        String[] han1 = {"", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
        String[] han2 = {"", "십", "백", "천"};
        String[] han3 = {"", "만", "억", "조", "경"};

        StringBuffer result = new StringBuffer();
        int len = money.length();
        for (int i = len - 1; i >= 0; i--) {
            result.append(han1[Integer.parseInt(money.substring(len - i - 1, len - i))]);
            if (Integer.parseInt(money.substring(len - i - 1, len - i)) > 0)
                result.append(han2[i % 4]);
            if (i % 4 == 0)
                result.append(han3[i / 4]);
        }

        return result.toString();
    }
}
