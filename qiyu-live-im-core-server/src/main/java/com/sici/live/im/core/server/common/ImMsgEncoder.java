package com.sici.live.im.core.server.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @projectName: qiyu-live-app
 * @package: com.sici.live.im.core.server.common
 * @author: 20148
 * @description: IM消息编码器
 * @create-date: 9/16/2024 3:00 PM
 * @version: 1.0
 */

public class ImMsgEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ImMsg imMsg = (ImMsg) msg;

        out.writeShort(imMsg.getMagic());
        out.writeInt(imMsg.getCode());
        out.writeInt(imMsg.getLen());
        out.writeBytes(imMsg.getBody());

//        ctx.writeAndFlush(out);
    }
}
