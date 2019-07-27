package net.netty.onlinecalc.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by FrozeRain on 27.07.2019.
 */
public class CalcServer {

    private final int port;

    public CalcServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                                      @Override
                                      protected void initChannel(SocketChannel socketChannel) throws Exception {
                                          ChannelPipeline pipeline = socketChannel.pipeline();

                                          pipeline.addLast(new LineBasedFrameDecoder(80));
                                          pipeline.addLast(new StringDecoder());
                                          pipeline.addLast(new StringEncoder());

                                          pipeline.addLast(new CalcServerHandler());
                                      }
                                  }
                    );
            System.out.println("> Server loaded!");
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
            System.out.println("> Server shutdown...");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new CalcServer(8080).run();
    }
}
