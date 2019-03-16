package cn.cyh.rpc.domain;

import java.io.Serializable;

/**
 * @Program simple-rpc
 * @Description 请求对象
 * @Author cyh
 * @Date 2019/3/15 23:00
 * @Version V1.0
 **/
public class CalculatorRequest implements Serializable {

    private static final long serialVersionUID = 8573914845896958528L;

    int num1;
    int num2;
    String method;

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "calculatorRequest{" +
                "num1=" + num1 +
                ", num2=" + num2 +
                ", method='" + method + '\'' +
                '}';
    }
}
