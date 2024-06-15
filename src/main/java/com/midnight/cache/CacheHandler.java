package com.midnight.cache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheHandler extends SimpleChannelInboundHandler<String> {

    private static final String CRLF = "\r\n";
    private static final String STR_PREFIX = "+";
    private static final String OK = "OK";
    private static final String INFO = "MidnightCache Server[v1.0.0], created by Midnight." + CRLF
            + "Mock Redis Server, at 2024-06-12 in Beijing." + CRLF;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        String[] args = message.split(CRLF);
        log.info("MidnightCacheHandler => " + String.join(",", args));
        String cmd = args[2].toUpperCase();

        if ("COMMAND".equals(cmd)) {
            writeByteBuf(ctx, "*2"
                    + CRLF + "$7"
                    + CRLF + "COMMAND"
                    + CRLF + "$4"
                    + CRLF + "PING"
                    + CRLF);
        } else if ("PING".equals(cmd)) {
            String ret = "PONG";
            if (args.length >= 5) {
                ret = args[4];
            }
            simpleString(ctx, ret);
        } else if ("INFO".equals(cmd)) {
            bulkString(ctx, INFO);
        } else {
            simpleString(ctx, OK);
        }

    }

    private void bulkString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, "$" + content.getBytes().length + CRLF + content + CRLF);
    }

    private void simpleString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, STR_PREFIX + content + CRLF);
    }

    private void writeByteBuf(ChannelHandlerContext ctx, String content) {
        log.info("wrap byte buffer and reply: " + content);
        ByteBuf buffer = Unpooled.buffer(128);
        buffer.writeBytes(content.getBytes());
        ctx.writeAndFlush(buffer);
    }
}
