package top.beliefyu.fstream.common.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * NettyClient
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-16 21:08
 */
public class NettyClient {
    private String host;

    private int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 客户端辅助启动类 对客户端配置
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //new MessageProtocolDecoder());
                            socketChannel.pipeline().addLast("decoder", new ByteArrayDecoder());
                            // 处理网络IO
                            socketChannel.pipeline().addLast("channelHandler", new ClientHandler());
                            //new MessageProtocolEncoder());
                            socketChannel.pipeline().addLast("encoder", new ByteArrayEncoder());
                        }
                    });
            // 异步链接服务器 同步等待链接成功
            ChannelFuture f = b.connect(host, port).sync();

            // 等待链接关闭
            f.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
            System.out.println("client release resource...");
        }

    }

    public static class ClientHandler extends ChannelInboundHandlerAdapter {

    }

}
