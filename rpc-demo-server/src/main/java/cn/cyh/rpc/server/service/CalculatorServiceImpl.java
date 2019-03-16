package cn.cyh.rpc.server.service;

import cn.cyh.rpc.api.CalculatorServiceApi;

/**
 * @Program simple-rpc
 * @Description TODO
 * @Author cyh
 * @Date 2019/3/15 23:09
 * @Version V1.0
 **/
public class CalculatorServiceImpl implements CalculatorServiceApi {

    @Override
    public Integer add(int num1, int num2) {
        return num1+num2;
    }
}
