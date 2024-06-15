package com.midnight.cache;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class CacheDecoder extends ByteToMessageDecoder {

    AtomicLong counter = new AtomicLong();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf in, List<Object> out) throws Exception {
        log.info("decode count: " + counter.incrementAndGet());

        if (in.readableBytes() <= 0) return;

        int count = in.readableBytes();
        int index = in.readerIndex();

        log.info("count:" + count + ",index:" + index);

        byte[] bytes = new byte[count];
        in.readBytes(bytes);

        String ret = new String(bytes);
        log.info("ret: " + ret);

        out.add(ret);

    }
}
