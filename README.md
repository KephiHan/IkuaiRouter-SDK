# IkuaiRouter-SDK
A Java SDK for Ikuai Router System API

## Usage

### Init and login

    IkuaiRouter ikuaiRouter = new IkuaiRouter(
                "192.168.1.1",  //ip
                80,             //port
                false,          //do not use https
                "admin",        //username
                "admin"         //password
        );                      //Auto login when init

### Query online host

    LanHostInfo lanHostInfo =ikuaiRouter
        .getLanHostInfoByIpAddr("192.168.1.2");
    System.out.println(lanHostInfo.toString());