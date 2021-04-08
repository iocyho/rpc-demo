package cn.cyh.rpc.server.controller;

import cn.cyh.rpc.domain.CalculatorResponse;
import cn.cyh.rpc.domain.CalculatorRequest;
import cn.cyh.rpc.server.service.CalculatorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Program simple-rpc
 * @Description RPC服务端
 * @Author cyh
 * @Date 2019/3/15 23:42
 * @Version V1.0
 **/
public class ServerController {

    public static void main(String[] args) throws IOException {
        new ServerController(10086).run();
    }

    private static final Logger log = LoggerFactory.getLogger(ServerController.class);
    public final int port;

    public ServerController(int port) {
        this.port = port;
    }

    private void run() throws IOException {
        log.info("RPC服务启动，监听端口:{}", port);
        ServerSocket listener = null;
        Socket socket = null;
        try {
            // 创建服务方的Socket，监听服务端口
            listener = new ServerSocket(port);
            // 自旋，保持监听状态
            while (true) {
                 socket = listener.accept();

                //用户请求数据反序列化
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Object object = objectInputStream.readObject();
                CalculatorRequest calculatorRequest = null;
                if (object instanceof CalculatorRequest){
                    calculatorRequest = (CalculatorRequest)object;
                }
                log.info("request is {}",calculatorRequest);

                //处理用户请求
                Integer result = calculate(calculatorRequest);

                //将计算结果封装到Response对象,Response对象序列化，向客户端发送
                CalculatorResponse calculatorResponse = new CalculatorResponse();
                calculatorResponse.setResult(result);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(calculatorResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (socket != null){
                socket.close();
            }
            if (listener != null){
                listener.close();
            }
        }
    }

    /**
     * @Description 解析请求对象，处理请求，调用计算服务
     * @Author cyh
     * @Date 0:03 2019/3/16
     * @Param [num1, num2]
     * @return java.lang.Integer
     **/
    private Integer calculate(CalculatorRequest calculatorRequest) {
        // TODO 应当是从请求报文中获取全限定类名，获取class对象，再反射调用对应的方法
        Class<CalculatorServiceImpl> calculatorServiceClazz = CalculatorServiceImpl.class;
        Method[] methods = calculatorServiceClazz.getMethods();
        Integer result = null;
        for (Method method : methods) {
            if (method.getName().equals(calculatorRequest.getMethod())) {
                try {
                    result = (Integer) method.invoke(calculatorServiceClazz.newInstance(), calculatorRequest.getNum1(), calculatorRequest.getNum2());
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * @Description 接收用户请求，反序列化
     * @Author cyh
     * @Date 23:26 2019/3/15
     * @Param [clientRequest]
     * @return cn.cyh.rpc.domain.calculatorRequest
     **/
   private Object getRequest(ObjectInputStream clientRequest) {

        //客户端请求数据反序列化
       Object readObject = null;
       try {
           readObject = clientRequest.readObject();
       } catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
       }

       log.info("request is {}",readObject);

        return readObject;
    }
}
