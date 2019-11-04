package io.github.foxitdog.alltoserver.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * 消息编解码器
 */
@Log4j2
public class MessageDecodeAndEncode extends MessageToMessageCodec<ByteBuf, JSONObject> {
    @Override
    protected void encode(ChannelHandlerContext ctx, JSONObject msg, List<Object> out) {
        ByteBuf bf = ctx.alloc().buffer();
        bf.writeBytes((msg.toString() + "|||").getBytes());
        out.add(bf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);

        String s = new String(bytes);
        log.info(s);
        String[] sa = s.split("\\|\\|\\|");
        for (String sm : sa) {
            try {
                JSONObject json = (JSONObject) JSON.parse(sm);
                out.add(json);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
