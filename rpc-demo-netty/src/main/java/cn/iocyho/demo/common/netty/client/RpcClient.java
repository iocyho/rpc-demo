package cn.iocyho.demo.common.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description RPC服务-客户端
 * @Author chenyihao
 * @Date 2021/2/10
 * @Version 1.0
 **/
public class RpcClient {

    /**
     * 创建线程池，用于提交任务
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    /**
     * 自定义的业务处理handler
     */
    private static RpcClientHandler rpcClientHandler;
    /**
     * 计数器，统计调用次数
     */
    private int count = 0;

    private final String remoteHost;
    private final int remotePort;

    public RpcClient(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    /**
     * proxy代理
     * @return 远程服务代理对象
     */
    public Object getBean(final Class<?> serverClass, final String header) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serverClass},
                (proxy, method, args) -> {
                    System.out.println("(proxy, method, args) 进入...." + (++count) + " 次");
                    if (rpcClientHandler == null) {
                        initClient(remoteHost, remotePort);
                    }
                    rpcClientHandler.setRequest(header + args[0]);
                    return executorService.submit(rpcClientHandler).get();
                });
    }

    /**
     * 客户端初始化
     * @param remoteHost 远程服务host
     * @param remotePort 远程服务端口
     */
    private static void initClient(String remoteHost, int remotePort) {
        rpcClientHandler = new RpcClientHandler();

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(rpcClientHandler);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(remoteHost, remotePort).sync();
            // 这里不能被阻塞！！！
            // channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
