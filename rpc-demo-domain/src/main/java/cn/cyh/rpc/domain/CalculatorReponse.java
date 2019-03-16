package cn.cyh.rpc.domain;

import java.io.Serializable;

/**
 * @Program simple-rpc
 * @Description 响应对象
 * @Author cyh
 * @Date 2019/3/15 23:00
 * @Version V1.0
 **/
public class CalculatorReponse implements Serializable {

    private static final long serialVersionUID = 2704215288915096509L;

    int result;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "calculatorReponse{" +
                "result=" + result +
                '}';
    }
}
