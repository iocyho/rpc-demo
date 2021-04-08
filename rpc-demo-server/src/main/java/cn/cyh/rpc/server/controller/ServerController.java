package cn.cyh.rpc.server.controller;

import cn.cyh.rpc.api.CalculatorServiceApi;
import cn.cyh.rpc.domain.CalculatorResponse;
import cn.cyh.rpc.domain.CalculatorRequest;
import cn.cyh.rpc.server.service.CalculatorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Program simple-rpc
 * @Description TODO
 * @Author cyh
 * @Date 2019/3/15 23:42
 * @Version V1.0
 **/
public class ServerController {

    private static Logger log = LoggerFactory.getLogger(ServerController.class);
    public static final int PORT = 10086;

    /**
     * @Description 监听端口
     * @Author cyh
     * @Date 23:50 2019/3/15
     * @Param
     * @return
     **/
    public static void main(String[] args) throws IOException {
        new ServerController().run();
    }

    private void run() throws IOException {
        ServerSocket listener = null;
        Socket socket = null;
        try {
            //创建服务方的Socket，监听服务端口
            listener = new ServerSocket(PORT);
            //通过死循环保持监听
            while (true){
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
                Integer result = caculate(calculatorRequest);


                //将计算结果封装到Response对象,Response对象序列化，向客户端发送
                CalculatorResponse calculatorReponse = new CalculatorResponse();
                calculatorReponse.setResult(result);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(calculatorReponse);
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
    private Integer caculate(CalculatorRequest calculatorRequest){
        //这里应该通过反射去调用，研究一下怎么实现
        if ("add".equals(calculatorRequest.getMethod())){
            CalculatorServiceApi calculatorService = new CalculatorServiceImpl();
            int num1 = calculatorRequest.getNum1();
            int num2 = calculatorRequest.getNum2();
            return calculatorService.add(num1,num2);
        }

        return null;
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
       } catch (IOException e) {
           e.printStackTrace();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       }

       log.info("request is {}",readObject);

        return readObject;
    }
}
