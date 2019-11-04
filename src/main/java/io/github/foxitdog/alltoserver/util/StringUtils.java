package io.github.foxitdog.alltoserver.util;

import org.apache.logging.log4j.core.util.StringBuilderWriter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;


/**
 * 字符串工具
 *
 * @author sudy-liangyun
 */
public class StringUtils {

    static boolean IsJava8 = System.getProperty("java.version").startsWith("1.8");

    /**
     * 26字母+0-9
     */
    final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    /**
     * 获取随机字符
     *
     * @param num 个数
     * @return
     */
    public static String getRandomString(int num) {
        StringBuilder sb = new StringBuilder(num);
        Random r = new Random();
        for (int i = num; i > 0; i--) {
            sb.append(digits[r.nextInt(36)]);
        }
        return sb.toString();
    }

    /**
     * 字节转为十六进制字符串
     *
     * @return 十六进制字符串
     * @param字节
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    /**
     * 字节转为十六进制字符串c格式
     *
     * @return 十六进制字符串
     * @param字节
     */
    public static String byte2hex4c(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "\\x0" + stmp;
            } else {
                hs = hs + "\\x" + stmp;
            }
        }
        return hs;
    }

    /**
     * 十六进制字符转为字节
     *
     * @return 字节
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("byte length is not correct");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 字符串转换成十六进制值
     *
     * @param bin String 我们看到的要转换成十六进制的字符串
     * @return
     */
    public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    /**
     * 字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String formUrlencoded(Map<String, String> map){
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = map.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        boolean isFirst = true;
        for (String k : keyArray) {
            String value = map.get(k);
            if (value == null) {
                value="";
            }
            if (!isFirst) {
                sb.append("&");
            }
            sb.append(k).append("=").append(value);
            isFirst = false;
        }
        return sb.toString();
    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    public static String replaceEnter(String str) {
        return str.replace("\r", "").replace("\n", "");
    }

    public static String readKey(String str) {
        return replaceEnter(str.replaceAll(".*-BEGIN.*", "").replaceAll(".*-END.*", ""));
    }

    /**
     * 获取错误栈字符串
     *
     * @param e exception
     * @return 错误栈字符串
     */
    public static String getExceptionStack(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }

    public static String getLengthSubString(String format, int length) {
        if (format.length()<length){
            return format;
        }else {
            return format.substring(0,length);
        }
    }

    /**
     * 分转元，保留2位小数
     * @param fen
     * @return
     */
    public static String getAmount(String fen) {
        return String.format("%.2f",Double.valueOf(fen) / 100);
    }

    public static String getAmount(Double fen) {
        return String.format("%.2f",Double.valueOf(fen) / 100);
    }

    /**
     * 获取方法名
     * 0 = 调用该方法的方法名
     * i = 调用该方法的上i级方法
     * @param i
     * @return
     */
    public static String getMethodName(int i){
        return getStackTraceElement(i+1).getMethodName();
    }

    public static String getNowMethodName(){
        return getMethodName(1);
    }

    public static String getJavaNowLocation(){
            return getStackTraceElement(1).toString();
    }
    /**
     * i=0时为调用getStackTraceElement方法的方法栈帧
     * i>0时为调用getStackTraceElement方法的方法的上i层
     * @param i
     * @return
     */
    public static StackTraceElement getStackTraceElement(int i){
        if(IsJava8){
            i=i+2;
        }
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        if(traces.length>i){
            return traces[i];
        }else{
            throw new ArrayIndexOutOfBoundsException("请求的堆栈大于现在的堆栈");
        }
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
