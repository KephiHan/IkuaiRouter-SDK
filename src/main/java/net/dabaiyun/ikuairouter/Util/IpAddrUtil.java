package net.dabaiyun.ikuairouter.Util;

import net.dabaiyun.ikuairouter.Exception.IkuaiRouterException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class IpAddrUtil {

    /**
     * 功能：根据IP和位数返回该IP网段的所有IP
     * <p>
     * 格式：getIpRangeByIpAndMask("192.192.192.1", "23")
     */
    public static List<String> getIpRangeByIpAndMask(String ip, int mask) throws IkuaiRouterException {
        if(!isIpVaild(ip)){
            throw new IkuaiRouterException("IP非法");
        }
        List<String> list = new ArrayList<>();
        if ("32".equals(mask)) {
            list.add(ip);
        } else {
            String startIp = getBeginIpStr(ip, mask);
            String endIp = getEndIpStr(ip, mask);
            if (!"31".equals(mask)) {
                String subStart = startIp.split("\\.")[0] + "." + startIp.split("\\.")[1] + "." + startIp.split("\\.")[2] + ".";
                String subEnd = endIp.split("\\.")[0] + "." + endIp.split("\\.")[1] + "." + endIp.split("\\.")[2] + ".";
                startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]) + 1);
                endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]) - 1);
            }
            list = getIpRangeAllIp(startIp, endIp);
        }
        return list;
    }

    /**
     * 功能：获取两个IP之间的所有ip
     * <p>
     * 格式：getIpRangeAllIp("192.192.192.1","192.192.193.128")
     */
    public static List<String> getIpRangeAllIp(String ipfrom, String ipto) throws IkuaiRouterException {
        if(!isIpVaild(ipfrom) || !isIpVaild(ipto)){
            throw new IkuaiRouterException("IP非法");
        }
        List<String> ips = new ArrayList<String>();
        String[] ipfromd = ipfrom.split("\\.");
        String[] iptod = ipto.split("\\.");
        int[] int_ipf = new int[4];
        int[] int_ipt = new int[4];
        for (int i = 0; i < 4; i++) {
            int_ipf[i] = Integer.parseInt(ipfromd[i]);
            int_ipt[i] = Integer.parseInt(iptod[i]);
        }
        for (int A = int_ipf[0]; A <= int_ipt[0]; A++) {
            for (int B = (A == int_ipf[0] ? int_ipf[1] : 0); B <= (A == int_ipt[0] ? int_ipt[1] : 255); B++) {
                for (int C = (B == int_ipf[1] ? int_ipf[2] : 0); C <= (B == int_ipt[1] ? int_ipt[2] : 255); C++) {
                    for (int D = (C == int_ipf[2] ? int_ipf[3] : 0); D <= (C == int_ipt[2] ? int_ipt[3] : 255); D++) {
                        ips.add(A + "." + B + "." + C + "." + D);
                    }
                }
            }
        }
        return ips;
    }


    /**
     * 把long类型的Ip转为一般Ip类型：xx.xx.xx.xx
     *
     * @param ip
     * @return
     */
    public static String parseIpFromLongToString(Long ip) {
        String s1 = String.valueOf((ip & 4278190080L) / 16777216L);
        String s2 = String.valueOf((ip & 16711680L) / 65536L);
        String s3 = String.valueOf((ip & 65280L) / 256L);
        String s4 = String.valueOf(ip & 255L);
        return s1 + "." + s2 + "." + s3 + "." + s4;
    }

    /**
     * 把xx.xx.xx.xx类型的转为long类型的
     *
     * @param ip
     * @return
     */
    public static Long parseIpFromStringToLong(String ip) throws IkuaiRouterException {
        if(!isIpVaild(ip)){
            throw new IkuaiRouterException("IP非法");
        }
        Long ipLong = 0L;
        String ipTemp = ip;
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf('.')));
        ipTemp = ipTemp.substring(ipTemp.indexOf('.') + 1, ipTemp.length());
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf('.')));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1, ipTemp.length());
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf('.')));
        ipTemp = ipTemp.substring(ipTemp.indexOf('.') + 1, ipTemp.length());
        ipLong = ipLong * 256 + Long.parseLong(ipTemp);
        return ipLong;
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的字符串表示
     */
    public static String getBeginIpStr(String ip, int maskBit) throws IkuaiRouterException {
        if(!isIpVaild(ip)){
            throw new IkuaiRouterException("IP非法");
        }
        return parseIpFromLongToString(getBeginIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的长整型表示
     */
    public static Long getBeginIpLong(String ip, int maskBit) throws IkuaiRouterException {
        if(!isIpVaild(ip)){
            throw new IkuaiRouterException("IP非法");
        }
        return parseIpFromStringToLong(ip) & parseIpFromStringToLong(parseMaskBitToMask(maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的字符串表示
     */
    public static String getEndIpStr(String ip, int maskBit) throws IkuaiRouterException {
        if(!isIpVaild(ip)){
            throw new IkuaiRouterException("IP非法");
        }
        return parseIpFromLongToString(getEndIpLong(ip, maskBit));
    }


    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的长整型表示
     */
    public static Long getEndIpLong(String ip, int maskBit) throws IkuaiRouterException {
        if(!isIpVaild(ip)){
            throw new IkuaiRouterException("IP非法");
        }
        return getBeginIpLong(ip, maskBit) + ~parseIpFromStringToLong(parseMaskBitToMask(maskBit));
    }


    /**
     * 根据子网掩码转换为掩码位 如 255.255.255.252转换为掩码位 为 30
     *
     * @param maskStr
     * @return
     */
    public static int parseMaskToMaskBit(String maskStr) {
        StringBuilder sbf;
        String str;
        int inetmask = 0;
        int count = 0;
        String[] ipList = maskStr.split("\\.");
        for (String s : ipList) {
            StringBuilder result = new StringBuilder();
            int x = Integer.parseInt(s);
            result.append(x % 2);
            x /= 2;
            while (x > 0) {
                result.append(x % 2);
                x /= 2;
            }
            str = result.reverse().toString();
            count = 0;
            for (int i = 0; i < str.length(); i++) {
                i = str.indexOf('1', i);
                if (i == -1) {
                    break;
                }
                count++;
            }
            inetmask += count;
        }
        return inetmask;
    }

    /**
     * @param maskBit 掩码位数，如"28"、"30"
     * @return
     */
    public static String parseMaskBitToMask(String maskBit) throws IkuaiRouterException {
        return parseMaskBitToMask(Integer.parseInt(maskBit));
    }

    /**
     * @param maskBit 掩码位数，如"28"、"30"
     * @return
     */
    public static String parseMaskBitToMask(int maskBit) throws IkuaiRouterException {
        if(maskBit < 1 || maskBit > 32){
            throw new IkuaiRouterException("掩码长度非法");
        }
        int n1 = maskBit / 8;
        int n2 = maskBit % 8;
        int[] ipaddr_int = {0, 0, 0, 0};
        for (int i = 0; i < n1; i++) {
            ipaddr_int[i] = 255;
        }
        if(n1 == 4){
            ipaddr_int[3] = 255;
        }else{
            for(int j = 0;j < n2;j++){
                ipaddr_int[n1] += Math.pow(2, 7 - j);
            }
        }
        return ipaddr_int[0] + "." + ipaddr_int[1] + "." +ipaddr_int[2] + "." +ipaddr_int[3];
    }

    public static double ipToDouble(String ip) throws IkuaiRouterException {
        if(!isIpVaild(ip)){
            throw new IkuaiRouterException("IP非法");
        }
        String[] arr = ip.split("\\.");
        double d1 = Double.parseDouble(arr[0]);
        double d2 = Double.parseDouble(arr[1]);
        double d3 = Double.parseDouble(arr[2]);
        double d4 = Double.parseDouble(arr[3]);
        return d1 * Math.pow(256, 3) + d2 * Math.pow(256, 2) + d3 * 256 + d4;
    }

    /**
     * 功能：判断一个IP是不是在一个网段下的
     * <p>
     * 格式：isInRange("192.168.8.3", "192.168.9.10/22");
     */
    public static boolean isIpInRange(String ip, String cidr) throws IkuaiRouterException {
        if(!isIpVaild(ip)){
            throw new IkuaiRouterException("IP非法");
        }
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24) | (Integer.parseInt(ips[1]) << 16) | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);
        return (ipAddr & mask) == (cidrIpAddr & mask);
    }

    /**
     * 判断一个掩码长度是否合法
     * @param maskBit
     * @return
     */
    public static boolean isMaskBitVaild(int maskBit) {
        return maskBit >= 1 && maskBit <= 32;
    }


    /**
     * 功能：根据位数返回IP总数
     * <p>
     * 格式：getIpCountByMask("192.192.192.1", "23")
     */
    public static int getIpCountByMask(String mask) {
        return BigDecimal.valueOf(Math.pow(2, 32 - Integer.parseInt(mask))).setScale(0, RoundingMode.DOWN).intValue();//IP总数，去小数点
    }


    /**
     * 功能：
     * <p>
     * 格式：isIP("192.192.192.1")
     */
    public static boolean isIpVaild(String str) {
        Pattern pattern = Pattern.compile("^(((\\d{1,2})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))$");
        return pattern.matcher(str).matches();
    }

    /**
     * 获取后面的第N个IP
     * @param ip
     * @param N
     * @return
     */
    public static String getNextNIp(String ip, int N) throws IkuaiRouterException {
        String[] ipd = ip.split("\\.");
        int[] int_ipf = new int[4];
        for (int i = 0; i < 4; i++) {
            int_ipf[i] = Integer.parseInt(ipd[i]);
        }
        for (int i = 0; i < N; i++) {
            //判断第四进位
            if (int_ipf[3] == 255) {
                //判断第三进位
                if (int_ipf[2] == 255) {
                    //判断第二进位
                    if (int_ipf[1] == 255) {
                        //判断第一进位
                        if (int_ipf[0] == 255) {
                            //第一位已经是255还要进行进位
                            throw new IkuaiRouterException("IP out of index");
                        }else{
                            int_ipf[0] ++;
                        }
                        int_ipf[1] = 0;
                    }else{
                        int_ipf[1] ++;
                    }
                    int_ipf[2] = 0;
                }else{
                    int_ipf[2] ++;
                }
                int_ipf[3] = 0;
            }else{
                int_ipf[3] ++;
            }
        }
        return int_ipf[0] + "." + int_ipf[1] + "."  + int_ipf[2] + "."  + int_ipf[3];
    }

    /**
     * 获取前N个ip
     * @param ip
     * @param N
     */
    public static String getPreviousNIp(String ip, int N) throws IkuaiRouterException {
        String[] ipd = ip.split("\\.");
        int[] int_ipf = new int[4];
        for (int i = 0; i < 4; i++) {
            int_ipf[i] = Integer.parseInt(ipd[i]);
        }
        for (int i = 0; i < N; i++) {
            //判断第四进位
            if (int_ipf[3] == 0) {
                //判断第三退位
                if (int_ipf[2] == 0) {
                    //判断第二退位
                    if (int_ipf[1] == 0) {
                        //判断第一退位
                        if (int_ipf[0] == 0) {
                            //第一位已经是255还要进行退位
                            throw new IkuaiRouterException("IP out of index");
                        }else{
                            int_ipf[0] --;
                        }
                        int_ipf[1] = 255;
                    }else{
                        int_ipf[1] --;
                    }
                    int_ipf[2] = 255;
                }else{
                    int_ipf[2] --;
                }
                int_ipf[3] = 255;
            }else{
                int_ipf[3] --;
            }
        }
        return int_ipf[0] + "." + int_ipf[1] + "."  + int_ipf[2] + "."  + int_ipf[3];
    }
}
