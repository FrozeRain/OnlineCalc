package net.netty.onlinecalc.Server;

import io.netty.channel.*;

/**
 * Created by FrozeRain on 27.07.2019.
 */
public class CalcServerHandler extends ChannelInboundMessageHandlerAdapter<String> {
    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("> " + s);
        Channel channel = channelHandlerContext.channel();
        channel.flush();
        channel.write("> Answer: " + CalcParser.parseAndSolve(s) + "\n");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("> USER " + ctx.channel().remoteAddress() + " connected.");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("> USER " + ctx.channel().remoteAddress() + " disconnected.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        channel.flush();
        channel.write("> Error: " + cause.getMessage() + "\n");
    }
}
