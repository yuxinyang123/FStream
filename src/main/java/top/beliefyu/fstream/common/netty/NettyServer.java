package top.beliefyu.fstream.common.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.TimeUnit;

/**
 * NettyServer
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-16 17:56
 */
public class NettyServer {
    private int port;
    private boolean useEpoll;

    /**
     * 用来接收进来的连接
     */
    private EventLoopGroup bossGroup;
    /**
     * 用来处理已经被接收的连接
     */
    private EventLoopGroup workerGroup;

    public NettyServer(int port) {
        this.port = port;
    }

    public NettyServer(int port, boolean useEpoll) {
        this.port = port;
        this.useEpoll = useEpoll;
    }

    private ServerBootstrap initBootStrap(boolean useEpoll) {
        Class<? extends ServerChannel> channelClass;
        if (useEpoll) {
            bossGroup = new EpollEventLoopGroup();
            workerGroup = new EpollEventLoopGroup();
            channelClass = EpollServerSocketChannel.class;
        } else {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            channelClass = NioServerSocketChannel.class;
        }
        return new ServerBootstrap()
                .group(bossGroup, workerGroup)
                //服务端
                .channel(channelClass);
    }

    public void run() throws Exception {


        try {
            //启动NIO服务的辅助启动类
            ServerBootstrap bootstrap = initBootStrap(useEpoll)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            //心跳机制 参数:1.读空闲超时时间 2.写空闲超时时间 3.所有类型的空闲超时时间(读、写) 4.时间单位
                            //在Handler需要实现userEventTriggered方法，在出现超时事件时会被触发
//                            socketChannel.pipeline().addLast("idleStateHandler", new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
                            //设置解码器
                            socketChannel.pipeline().addLast("decoder", new ByteArrayDecoder());
                            socketChannel.pipeline().addLast("channelHandler", new ServerHandler());
                            //设置编码器
                            socketChannel.pipeline().addLast("encoder", new ByteArrayEncoder());

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //绑定端口，开始接收进来的连接
            ChannelFuture cf = bootstrap.bind(port).sync();
            //等待服务器socket关闭
            cf.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static class ServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            try {
                System.out.println(msg);
            } finally {
                // 抛弃收到的数据
                ReferenceCountUtil.release(msg);
            }
        }
    }
}