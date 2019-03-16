package cn.cyh.rpc.api;

/**
 * @Program simple-rpc
 * @Description 远程服务Api,由客户端与服务端共享
 * @Author cyh
 * @Date 2019/3/15 22:57
 * @Version V1.0
 **/
public interface CalculatorServiceApi {
    Integer add(int num1, int num2);
}
