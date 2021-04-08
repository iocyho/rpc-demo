package cn.iocyho.demo.consumer;


import cn.iocyho.demo.common.api.DemoService;
import cn.iocyho.demo.common.constant.CustomHeader;
import cn.iocyho.demo.common.netty.client.RpcClient;

/**
 * @Description 消费者(客户端)启动器
 * @Author chenyihao
 * @Date 2021/2/10
 * @Version 1.0
 **/
public class ConsumerBootstrap {
    public final static String REMOTE_HOST = "127.0.0.1";
    public final static int REMOTE_PORT = 9999;

    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient(REMOTE_HOST, REMOTE_PORT);
        DemoService demoService = (DemoService) rpcClient.getBean(DemoService.class, CustomHeader.DEMO_SERVICE);
        String resp = demoService.sayHello("你好，服务端");
        System.out.println(resp);
    }
}
