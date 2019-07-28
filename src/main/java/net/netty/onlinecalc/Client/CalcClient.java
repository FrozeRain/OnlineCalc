package net.netty.onlinecalc.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by FrozeRain on 27.07.2019.
 */
public class CalcClient {

    private final String host;
    private final int port;

    public CalcClient(String host, int port){
        this.host = host;
        this.port = port;
    }
    public  void run() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>(){
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();

                    pipeline.addLast(new LineBasedFrameDecoder(80));
                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(new StringEncoder());

                    pipeline.addLast(new CalcClientHandler());
                }
            });

            //com.sun.nio.sctp.SctpChannel channel1 = com.sun.nio.sctp.SctpChannel.open();

            System.out.print("| Online Calculator 1.0 \n" +
                    "| by FrozeRain. Use: \n" +
                    "|  - symbol '+' for sum, \n" +
                    "|  - symbol '-' for minus, \n" +
                    "|  - symbol '*' for multiply, \n" +
                    "|  - symbol '/' for division, \n" +
                    "|  - symbol '.' for float numbers, \n" +
                    "|  - type 'stop' for exit. \n" +
                    "| Example -45.5*(-6.3) \n");
            Channel channel = bootstrap.connect(host, port).sync().channel();
            System.out.println("> Connected to " + channel.remoteAddress());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true){
                String example = reader.readLine();
                if (example.equalsIgnoreCase("stop")){
                    break;
                }
                channel.writeAndFlush(example + "\n");
            }
            channel.disconnect();
            System.out.println("> Disconnected!");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
    public static void main(String[] args)  {
        try {
            new CalcClient("localhost", 8080).run();
        } catch (Exception e){
            System.out.println("> Error: " + e.getMessage());
        }
    }
}
