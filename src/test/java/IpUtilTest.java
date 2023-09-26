import net.dabaiyun.ikuairouter.Exception.IkuaiRouterException;
import net.dabaiyun.ikuairouter.Util.IpAddrUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class IpUtilTest {

    @Test
    public void test1() throws IkuaiRouterException {
        String ip = "172.31.0.4";//ip
        int maskBit = 24;//位数，如果只知道子网掩码不知道位数的话在参考getMaskMap()方法
        String submark = IpAddrUtil.parseMaskBitToMask(maskBit);
        System.out.println("子网掩码为：" + submark);
        //获得起始IP和终止IP的方法（包含网络地址和广播地址）
        String startIp = IpAddrUtil.getBeginIpStr(ip, maskBit);
        String endIp = IpAddrUtil.getEndIpStr(ip, maskBit);
        System.out.println("起始IP：" + startIp + "终止IP：" + endIp);
        //根据位数查询IP数量
        int ipCount = IpAddrUtil.getIpCountByMask("24");
        System.out.println("ip个数：" + ipCount);
        //获得起始IP和终止IP的方法（不包含网络地址和广播地址）
        String subStart = startIp.split("\\.")[0] + "." + startIp.split("\\.")[1] + "." + startIp.split("\\.")[2] + ".";
        String subEnd = endIp.split("\\.")[0] + "." + endIp.split("\\.")[1] + "." + endIp.split("\\.")[2] + ".";
        startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]) + 1);
        endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]) - 1);
        System.out.println("起始IP：" + startIp + "终止IP：" + endIp);
        //判断一个IP是否属于某个网段
        boolean flag = IpAddrUtil.isIpInRange("10.2.0.0", "10.3.0.0/17");
        System.out.println(flag);
        //判断是否是一个IP
        System.out.println(IpAddrUtil.isIpVaild("192.168.1.0"));
        //把ip转换为数字(mysql中inet_aton()的实现)
        System.out.println(IpAddrUtil.ipToDouble("192.168.1.1"));
        //打印IP段所有IP（IP过多会内存溢出）
        List<String> list = IpAddrUtil.getIpRangeByIpAndMask(ip, maskBit);
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void test2() throws IkuaiRouterException {
        String ip = "192.168.1.101";
        String mask = "19";
//        int mask_int = 18;
//        IpAddrUtil.getIpRangeByIpAndMask(ip, mask).forEach(System.out::println);
//        System.out.println(IpAddrUtil.getIpCountByMask(mask));
//        System.out.println(IpAddrUtil.isIP("10.199.33.45"));
//        IpAddrUtil.getIpRangeAllIp("192.168.1.109","192.168.2.24").forEach(System.out::println);
        System.out.println(IpAddrUtil.ipToDouble(ip));
        System.out.println(BigDecimal.valueOf(IpAddrUtil.ipToDouble(ip)).toString());
//        System.out.println(IpAddrUtil.getBeginIpStr(ip, mask));
//        System.out.println(IpAddrUtil.getEndIpStr(ip, mask));
//        for (int i = 1; i <= 32; i++) {
//            System.out.println("掩码长度：" + i + " 掩码：" + IpAddrUtil.parseMaskLengthToMask(i));
//        }
//        System.out.println(IpAddrUtil.getEndIpStr("172.0.64.59","18"));
//        System.out.println(IpAddrUtil.parseMaskToMaskLength("255.255.192.0"));
    }
}
