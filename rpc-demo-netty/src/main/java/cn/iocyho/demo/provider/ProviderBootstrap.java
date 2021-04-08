package cn.iocyho.demo.provider;

import cn.iocyho.demo.common.netty.server.RpcServer;

/**
 * @Description 服务提供端启动器
 * @Author chenyihao
 * @Date 2021/2/10
 * @Version 1.0
 **/
public class ProviderBootstrap {

    public final static int PORT = 9999;

    public static void main(String[] args) {
        RpcServer.startServer(PORT);
    }
}
