package cn.cyh.rpc.client.controller;

import cn.cyh.rpc.api.CalculatorServiceApi;
import cn.cyh.rpc.client.service.CalculatorRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Program simple-rpc
 * @Description TODO
 * @Author cyh
 * @Date 2019/3/15 22:57
 * @Version V1.0
 **/
public class ClientController {

    private static Logger log = LoggerFactory.getLogger(ClientController.class);

    public static void main(String[] args) {
        new ClientController().run();
    }


    private void run() {
        //创建代理对象，调用远程服务
        CalculatorServiceApi calculatorRemoteService = new CalculatorRemoteService();
        Integer result = calculatorRemoteService.add(4, 6);
        log.info("result is {}",result);
    }
}
