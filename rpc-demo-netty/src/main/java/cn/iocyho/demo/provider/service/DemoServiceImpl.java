package cn.iocyho.demo.provider.service;

import cn.iocyho.demo.common.api.DemoService;

/**
 * @Description Demo服务实现类
 * @Author chenyihao
 * @Date 2021/2/10
 * @Version 1.0
 **/
public class DemoServiceImpl implements DemoService {

    private static int requestCount;

    @Override
    public String sayHello() {
        System.out.println("收到客户端请求");
        return "Hello, I had received your request for " + (++requestCount) + "times.";
    }

    @Override
    public String sayHello(String message) {
        System.out.println("收到客户端消息: " + message);
        return "Hello, I had received your request for " + (++requestCount) + " times, and your message is: [" + message + "]";
    }
}
