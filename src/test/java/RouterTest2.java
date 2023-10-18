import net.dabaiyun.ikuairouter.Entity.LanHostInfo;
import net.dabaiyun.ikuairouter.Entity.NetMapping;
import net.dabaiyun.ikuairouter.IkuaiRouter;
import org.junit.Test;

import java.util.Scanner;

public class RouterTest2 {

    private final String address = "192.168.1.1";
    private final int port = 80;
    private final boolean https = false;
    private final String username = "admin";
    private final String pwd = "Admin@123";

    private final IkuaiRouter ikuaiRouter = new IkuaiRouter(
            address, port, https, username, pwd
    );

    public RouterTest2() throws Exception {
    }

    public static void main(String[] args) throws Exception {
        String address = "192.168.1.1";
        int port = 80;
        boolean https = false;
        String username = "admin";
        String pwd = "Admin@123";
        IkuaiRouter ikuaiRouter = new IkuaiRouter(
                address, port, https, username, pwd
        );
        Scanner scanner = new Scanner(System.in);
        String input = "";
        do {
            ikuaiRouter = new IkuaiRouter(
                    address, port, https, username, pwd, false
            );
            try {
                System.out.println(ikuaiRouter.getPlugins());
//            } catch (IkuaiRouterNoAuthException noAuthException) {
//                System.out.println("IkuaiRouterNoAuthException : " + noAuthException.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("输入0结束，输入其他再来一次");
            input = scanner.nextLine();
        } while (!input.equals("0"));

        LanHostInfo lanHostInfo = ikuaiRouter.getLanHostInfoByIpAddr("192.168.1.2");
        System.out.println(lanHostInfo.toString());
    }

    @Test
    public void cookieTest() {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        do {
            try {
                System.out.println(ikuaiRouter.getPlugins());
//            } catch (IkuaiRouterNoAuthException noAuthException) {
//                System.out.println("IkuaiRouterNoAuthException : " + noAuthException.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            input = scanner.nextLine();
            System.out.println("输入0结束，输入其他再来一次");
        } while (!input.equals("0"));
    }

    @Test
    public void findAvailableIp() throws Exception {
        System.out.println("Find IP : " + ikuaiRouter.findAvailableIpAddr("10.1.0.1", 24, "10.1.0.32", "10.1.0.240"));
    }

    @Test
    public void idTest() throws Exception {
        int port_base = 40000;
        String ip_lan = "192.168.1.101";

        for (int i = 1; i <= 10; i++) {
            log("生成第 " + i + " 个端口映射记录");
            NetMapping netMapping = new NetMapping();
            netMapping.setWan_port(String.valueOf(port_base + i));
            netMapping.setProtocol("tcp+udp");
            netMapping.setLan_addr(ip_lan);
            netMapping.setLan_port(String.valueOf(port_base + i));
            netMapping.setInter_face("wan1");
            netMapping.setEnabled("yes");
            netMapping.setComment("Test-AutoCreate-" + i);

            Integer newRowId = ikuaiRouter.addNetMapping(netMapping);
            log("已插入，返回新ID为 " + newRowId);
        }
    }

    private void log(String t) {
        System.out.println(t);
    }

}
