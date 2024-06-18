package com.midnight.cache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/***
 * REDIS 协议规范  https://redis.com.cn/topics/protocol.html
 *
 * 在 RESP 中，数据的类型依赖于首字节：
 *
 * 单行字符串（Simple Strings）： 响应的首字节是 "+"
 * 错误（Errors）： 响应的首字节是 "-"
 * 整型（Integers）： 响应的首字节是 ":"
 * 多行字符串（Bulk Strings）： 响应的首字节是"\$"
 * 数组（Arrays）： 响应的首字节是 "*"
 * RESP协议的不同部分总是以 "\r\n" (CRLF) 结束。
 */
@Slf4j
public class CacheHandler extends SimpleChannelInboundHandler<String> {

    private static final String CRLF = "\r\n";
    private static final String STR_PREFIX = "+";
    private static final String OK = "OK";
    private static final String BULK_PREFIX = "$";

    private static final String INFO = "MidnightCache Server[v1.0.0], created by Midnight." + CRLF
            + "Mock Redis Server, at 2024-06-12 in Beijing." + CRLF;

    public static final MidnightCache cache = new MidnightCache();

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
        } else if ("GET".equals(cmd)) {
            String value = cache.get(args[4]);
            bulkString(ctx, value);
        } else if ("SET".equals(cmd)) {
            cache.set(args[4], args[6]);
            simpleString(ctx, OK);
        } else if ("STRLEN".equals(cmd)) {
            String value = cache.get(args[4]);
            integer(ctx, value == null ? 0 : value.length());
        } else if ("DEL".equals(cmd)) {
            int len = (args.length - 3) / 2;
            String[] keys = new String[len];
            for (int i = 0; i < len; i++) {
                keys[i] = args[4 + i * 2];
            }
            integer(ctx, cache.del(keys));
        } else if ("EXISTS".equals(cmd)) {
            int len = (args.length - 3) / 2;
            String[] keys = new String[len];
            for (int i = 0; i < len; i++) {
                keys[i] = args[4 + i * 2];
            }
            integer(ctx, cache.exists(keys));
        } else if ("MGET".equals(cmd)) {
            int len = (args.length - 3) / 2;
            String[] keys = new String[len];
            for (int i = 0; i < len; i++) {
                keys[i] = args[4 + i * 2];
            }
            array(ctx, cache.mget(keys));
        } else if ("MSET".equals(cmd)) {
            int len = (args.length - 3) / 4;
            String[] keys = new String[len];
            String[] vals = new String[len];
            for (int i = 0; i < len; i++) {
                keys[i] = args[4 + i * 4];
                vals[i] = args[6 + i * 4];
            }
            cache.mset(keys, vals);
            simpleString(ctx, OK);
        } else if ("INCR".equals(cmd)) {
            String key = args[4];
            try {
                integer(ctx, cache.incr(key));
            } catch (NumberFormatException nfe) {
                error(ctx, "NFE" + key + " value[" + cache.get(key) + "] is not an integer");
            }
        } else if ("DECR".equals(cmd)) {
            String key = args[4];
            try {
                integer(ctx, cache.decr(key));
            } catch (NumberFormatException nfe) {
                error(ctx, "NFE" + key + " value is not an integer");
            }
        } else {
            simpleString(ctx, OK);
        }

    }

    private void integer(ChannelHandlerContext ctx, int i) {
        writeByteBuf(ctx, integerEncode(i));
    }

    private String integerEncode(int i) {
        return ":" + i + CRLF;
    }

    private void error(ChannelHandlerContext ctx, String msg) {
        writeByteBuf(ctx, errorEncode(msg));
    }

    private String errorEncode(String msg) {
        return "-" + msg + CRLF;
    }

    public void array(ChannelHandlerContext ctx, String[] array) {
        writeByteBuf(ctx, arrayEncode(array));
    }

    private String arrayEncode(Object[] array) {
        StringBuilder sb = new StringBuilder();
        if (array == null) {
            sb.append("*-1" + CRLF);
        } else if (array.length == 0) {
            sb.append("*0" + CRLF);
        } else {
            sb.append("*" + array.length + CRLF);
            for (int i = 0; i < array.length; i++) {
                Object obj = array[i];
                if (obj == null) {
                    sb.append("$-1" + CRLF);
                } else {
                    if (obj instanceof Integer) {
                        sb.append(integerEncode((Integer) obj));
                    } else if (obj instanceof String) {
                        sb.append(bulkStringEncode((String) obj));
                    } else if (obj instanceof Object[] objs) {
                        sb.append(arrayEncode(objs));
                    }
                }
            }
        }

        return sb.toString();
    }

    private String bulkStringEncode(String content) {
        String ret;
        if (content == null) {
            ret = "$-1";
        } else if (content.isEmpty()) {
            ret = "$0";
        } else {
            ret = BULK_PREFIX + content.getBytes().length + CRLF + content;
        }
        return ret + CRLF;
    }

    private void bulkString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, bulkStringEncode(content));
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
