package cn.iocyho.demo.common.netty.server;

import cn.iocyho.demo.common.constant.CustomHeader;
import cn.iocyho.demo.provider.service.DemoServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description RPC服务-服务端
 * @Author chenyihao
 * @Date 2021/2/10
 * @Version 1.0
 **/
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = msg.toString();
        System.out.println("raw message from consumer: " + message);
        // 自定义一个协议，规定所有请求必须以固定字符串开头,如"HelloService#hello#你好" TODO 可以通过封装自定义的协议类来实现
        if (message.startsWith(CustomHeader.DEMO_SERVICE)) {
            String resp = new DemoServiceImpl().sayHello(message.substring(message.lastIndexOf('#') + 1));
            ctx.writeAndFlush(resp);
        } else {
            ctx.writeAndFlush("无效请求");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println(cause.getMessage());
        ctx.close();
    }
}
