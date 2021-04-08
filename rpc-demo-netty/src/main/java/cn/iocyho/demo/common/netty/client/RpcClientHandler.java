package cn.iocyho.demo.common.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @Description RPC服务-客户端
 *              注意观察调用顺序
 * @Author chenyihao
 * @Date 2021/2/10
 * @Version 1.0
 **/
public class RpcClientHandler extends ChannelInboundHandlerAdapter implements Callable<String> {

    private ChannelHandlerContext context;
    private String response;
    private String request;

    /**
     *
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("RpcClientHandler.channelActive 被调用");
        context = ctx;
    }

    /**
     * 这里要进行异步转同步的操作，进行线程间通信
     * 注意 synchronized 关键字
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("RpcClientHandler.channelRead 被调用");
        response = msg.toString();
        // 唤醒被等待中的线程(异步转同步)
        notify();
    }

    /**
     *
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println(cause.getMessage());
        ctx.close();
    }

    /**
     * 这里要进行异步转同步的操作，进行线程间通信
     * 注意 synchronized 关键字
     *
     * 被代理对象调用, 发送数据给服务器，-> wait -> 等待被唤醒(channelRead方法中唤醒)
     */
    @Override
    public synchronized String call() throws Exception {
        System.out.println("RpcClientHandler.call 第一次被调用，向服务端发送请求");
        context.writeAndFlush(request);
        // 发送完消息后等待，直至在channelRead方法中被唤醒
        wait();
        System.out.println("RpcClientHandler.call 第二次被调用，接收服务端的返回结果");
        return response;
    }

    public void setRequest(String request) {
        System.out.println("RpcClientHandler.setRequest 被调用");
        this.request = request;
    }
}
