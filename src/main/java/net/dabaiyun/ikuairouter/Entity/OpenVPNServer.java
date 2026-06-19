package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenVPNServer {

    private String auth;
    private String comp_lzo;
    private String dev_type;
    private String topology;
    private int method;
    private String enabled;
    private String port;
    private String subnet;
    private String mask;
    private String push_gateway;
    private String push_route;
    private String push_route_comment;
    private String proto;
    private String push_dns;
    private String extra_config;
    private String key;
    private int status;

    public OpenVPNServer() {
    }

    public String getAuth() { return auth; }
    public void setAuth(String auth) { this.auth = auth; }

    public String getComp_lzo() { return comp_lzo; }
    public void setComp_lzo(String comp_lzo) { this.comp_lzo = comp_lzo; }

    public String getDev_type() { return dev_type; }
    public void setDev_type(String dev_type) { this.dev_type = dev_type; }

    public String getTopology() { return topology; }
    public void setTopology(String topology) { this.topology = topology; }

    public int getMethod() { return method; }
    public void setMethod(int method) { this.method = method; }

    public String getEnabled() { return enabled; }
    public void setEnabled(String enabled) { this.enabled = enabled; }

    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    public String getSubnet() { return subnet; }
    public void setSubnet(String subnet) { this.subnet = subnet; }

    public String getMask() { return mask; }
    public void setMask(String mask) { this.mask = mask; }

    public String getPush_gateway() { return push_gateway; }
    public void setPush_gateway(String push_gateway) { this.push_gateway = push_gateway; }

    public String getPush_route() { return push_route; }
    public void setPush_route(String push_route) { this.push_route = push_route; }

    public String getPush_route_comment() { return push_route_comment; }
    public void setPush_route_comment(String push_route_comment) { this.push_route_comment = push_route_comment; }

    public String getProto() { return proto; }
    public void setProto(String proto) { this.proto = proto; }

    public String getPush_dns() { return push_dns; }
    public void setPush_dns(String push_dns) { this.push_dns = push_dns; }

    public String getExtra_config() { return extra_config; }
    public void setExtra_config(String extra_config) { this.extra_config = extra_config; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    @Override
    public String toString() {
        return "OpenVPNServer{" +
                "auth='" + auth + "'" +
                ", comp_lzo='" + comp_lzo + "'" +
                ", dev_type='" + dev_type + "'" +
                ", topology='" + topology + "'" +
                ", method=" + method +
                ", enabled='" + enabled + "'" +
                ", port='" + port + "'" +
                ", subnet='" + subnet + "'" +
                ", mask='" + mask + "'" +
                ", push_gateway='" + push_gateway + "'" +
                ", push_route='" + push_route + "'" +
                ", push_route_comment='" + push_route_comment + "'" +
                ", proto='" + proto + "'" +
                ", push_dns='" + push_dns + "'" +
                ", extra_config='" + extra_config + "'" +
                ", status=" + status +
                '}';
    }
}
