package cn.cyh.rpc.client.service;

import cn.cyh.rpc.api.CalculatorServiceApi;
import cn.cyh.rpc.domain.CalculatorResponse;
import cn.cyh.rpc.domain.CalculatorRequest;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Program simple-rpc
 * @Description TODO
 * @Author cyh
 * @Date 2019/3/16 0:25
 * @Version V1.0
 **/
public class CalculatorRemoteService implements CalculatorServiceApi {
    private static Logger log = LoggerFactory.getLogger(CalculatorRemoteService.class);
    public static final int PORT = 10086;

    /**
     * @Description 这个地址模拟的是远程服务的地址，理论上还要进行一次客户端的负载均衡，从多个服务实例中找一个的去调用
     * @Author cyh
     * @Date 0:55 2019/3/16
     * @Param
     * @return
     **/

    @Override
    public Integer add(int num1,int num2) {
        Socket socket;
        try {
            //获取服务列表
            Map<String, List> serviceMap = getServiceMap();
            //获取服务实例列表,其实是应该根据接口获取接口名称，再根据接口名称查询服务
            List<String> calculatorServiceList = serviceMap.get("calculator");
            //选择一个服务实例,得到实例地址
            String serviceAddress = chooseTarget(calculatorServiceList);

            //创建Socket,与服务实例通信
            socket = new Socket(serviceAddress,PORT);

            //创建请求对象，将请求对象序列化,发送给服务端
            //获取当前线程执行的方法名称
            String currentMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
            System.out.println(currentMethod);
            CalculatorRequest calculatorRequest = generateRequest(currentMethod,num1, num2);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(calculatorRequest);

            log.info("request is {}",calculatorRequest);

            //将服务端发来的响应结果对象反序列化
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object response = objectInputStream.readObject();

            //将响应结果返回结果给客户端
            if (response instanceof CalculatorResponse){
                CalculatorResponse calculatorResponse = (CalculatorResponse) response;
                return calculatorResponse.getResult();
            }
        } catch (Exception e) {
            log.error("fail", e);
            throw new InternalError();
        }

        return null;

    }

    /**
     * @Description 从服务实例列表中选择一个具体的服务实例，实际上就是客户端的负载均衡，这里不做实现
     * @Author cyh
     * @Date 10:03 2019/3/16
     * @Param [serverList]
     * @return java.lang.String
     **/
    private String chooseTarget(List<String> serviceInstanceList){
        if (serviceInstanceList != null){
            return serviceInstanceList.get(0);
        }
        return null;
    }

    /**
     * @Description 获取服务列表，模拟从注册中心获取服务列表的行为，这里不做实现
     * @Author cyh
     * @Date 10:10 2019/3/16
     * @Param []
     * @return java.util.Map<java.lang.String,java.util.List>
     **/
    public static Map<String,List> getServiceMap(){
        //创建服务列表
        HashMap<String, List> serviceMap = new HashMap<>(10);

        //创建一个calculatorService服务
        ArrayList<String> calculatorService = new ArrayList<>();
        calculatorService.add("127.0.0.1");

        //将服务注册到服务列表中
        serviceMap.put("calculator",calculatorService);
        return serviceMap;
    }


    /**
     * @Description 创建请求对象
     * @Author cyh
     * @Date 0:41 2019/3/16
     * @Param [num1, num2]
     * @return cn.cyh.rpc.domain.calculatorRequest
     **/
    private CalculatorRequest generateRequest(String methodName,int num1,int num2){
        CalculatorRequest calculatorRequest = new CalculatorRequest();
        calculatorRequest.setNum1(num1);
        calculatorRequest.setNum2(num2);
        calculatorRequest.setMethod(methodName);
        return calculatorRequest;
    }
}
