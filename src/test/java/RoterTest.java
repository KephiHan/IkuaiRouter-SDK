import com.fasterxml.jackson.databind.ObjectMapper;
import net.dabaiyun.ikuairouter.Exception.IkuaiRouterException;
import net.dabaiyun.ikuairouter.IkuaiRouter;
import net.dabaiyun.ikuairouter.Util.IpAddrUtil;
import okhttp3.Cookie;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoterTest {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    private final String address = "192.168.77.1";
    private final int port = 8839;
    private final boolean https = true;
    private final String username = "admin";
    private final String pwd = "admin";

    @Test
    public void cookieStoreTest() throws Exception {
        IkuaiRouter ikuaiRouter = new IkuaiRouter(
                "192.168.77.1",
                80,
                false,
                "admin",
                "admin"
        );

        String sess_key = ikuaiRouter.getSessKey();

        System.out.println(sess_key);

        IkuaiRouter ikuaiRouter1 = new IkuaiRouter(
                "192.168.77.1",
                80,
                false,
                "admin",
                "admin",
                sess_key
        );

        System.out.println(
                ikuaiRouter1.findAvailableIpAddr(
                        "192.168.77.1",
                        24,
                        "192.168.77.1",
                        "192.168.77.255"
                )
        );

    }

    @Test
    public void delNetmapTest() throws Exception {
        IkuaiRouter ikuaiRouter = new IkuaiRouter(
                address,
                port,
                https,
                username,
                pwd
        );
        System.out.println(
                ikuaiRouter.delNetMappingById(95)
        );
    }

    @Test
    public void findWanPortTest() throws Exception {
        IkuaiRouter ikuaiRouter = new IkuaiRouter(
                address,
                port,
                https,
                username,
                pwd
        );
        long time_begin = System.currentTimeMillis();
        System.out.println(ikuaiRouter.findAvailableNetMappingWanPort("adsl2",30000,31000));
        long time_end = System.currentTimeMillis();
        System.out.println(" Time : " + (time_end - time_begin) + "ms");
//        System.out.println(Arrays.toString("8000,8005".split("-|,")));
    }

    @Test
    public void getNextNIp() throws IkuaiRouterException {
        System.out.println(IpAddrUtil.getNextNIp("255.255.240.0", 16384));
    }

    @Test
    public void getPreviousNIp() throws IkuaiRouterException {
        System.out.println(IpAddrUtil.getPreviousNIp("0.0.48.213", 16384));
    }

    @Test
    public void findAvailableIp() throws Exception {
        long timestrap_start = System.currentTimeMillis();
        IkuaiRouter ikuaiRouter = new IkuaiRouter(
                address,
                port,
                https,
                username,
                pwd
        );
        long timestrap_logined = System.currentTimeMillis();
        System.out.println("Find IP : " + ikuaiRouter.findAvailableIpAddr("10.1.0.1", 24, "10.1.0.32", "10.1.0.240"));
        long timestrap_finish = System.currentTimeMillis();

        System.out.println("登入耗时: " + (timestrap_logined - timestrap_start) + " ms");
        System.out.println("查找耗时: " + (timestrap_finish - timestrap_logined) + " ms");
        System.out.println("总耗时: " + (timestrap_finish - timestrap_start) + " ms");
    }

    @Test
    public void logintest() throws Exception {
        IkuaiRouter ikuaiRouter = new IkuaiRouter(
                address,
                port,
                https,
                username,
                pwd
        );
        try {
            System.out.println(ikuaiRouter.findAvailableIpAddr("192.168.1.1", 24, "10.1.0.32", "10.1.0.240"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDHCPStaticTest() throws Exception {
        IkuaiRouter ikuaiRouter = new IkuaiRouter(
                address,
                port,
                https,
                username,
                pwd
        );
        try {
//            String str = ikuaiRouter.getDHCPStaticByIpAddr(
//                    "192.168.77.4"
//            ).toString();
            String str = ikuaiRouter.getDHCPStaticByMAC("fe:4e:9d:70:55:23").toString();
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void duwiafg() {
        String mac = "FE:4E:9D:70:55:23";
        System.out.println(mac.toLowerCase());
    }

    @Test
    public void getLanHostInfo() throws Exception {
        IkuaiRouter ikuaiRouter = new IkuaiRouter(
                address,
                port,
                https,
                username,
                pwd
        );
        System.out.println(ikuaiRouter.getLanHostInfoByMAC("C8:09:A8:57:08:5A"));
    }

    @Test
    public void getSystemStatu() throws Exception {
        IkuaiRouter ikuaiRouter = new IkuaiRouter(
                address,
                port,
                https,
                username,
                pwd
        );
        System.out.println(ikuaiRouter.getSystemStatus());

    }
}
