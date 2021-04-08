package cn.iocyho.demo.common.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description RPC服务-服务端
 * @Author chenyihao
 * @Date 2021/2/10
 * @Version 1.0
 **/
public class RpcServer {

    public final static String LOCAL_HOST = "127.0.0.1";

    private RpcServer(int port) {
    }

    public static void startServer(int port) {
        startServer0(LOCAL_HOST, port);
    }

    public static void startServer(String host, int port) {
        startServer0(host, port);
    }

    private static void startServer0(String host, int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerInitializer());
            try {
                ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();
                System.out.println("RPC服务启动 " + host + ":" + port);
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
