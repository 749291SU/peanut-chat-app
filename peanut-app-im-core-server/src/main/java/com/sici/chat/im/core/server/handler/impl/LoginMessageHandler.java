package com.sici.chat.im.core.server.handler.impl;

import com.alibaba.fastjson.JSON;
import com.sici.common.result.ResponseResult;
import com.sici.chat.im.core.server.common.ChannelHandlerContextCache;
import com.sici.chat.model.im.bo.ImMsg;
import com.sici.chat.im.core.server.common.ImMsgBuilder;
import com.sici.chat.im.core.server.common.util.ImCacheUtil;
import com.sici.chat.im.core.server.common.util.ImContextUtil;
import com.sici.chat.im.core.server.handler.AbstractMessageHandler;
import com.sici.chat.interfaces.im.rpc.ImTokenRpc;
import com.sici.chat.model.im.dto.ImMsgBody;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @projectName: qiyu-live-app
 * @package: com.sici.live.im.core.server.handler.impl
 * @author: 20148
 * @description: 登录消息处理器
 * @create-date: 9/16/2024 3:24 PM
 * @version: 1.0
 */


@Component
@Slf4j
public class LoginMessageHandler implements AbstractMessageHandler {
    @DubboReference
    private ImTokenRpc imTokenRpc;
    @Resource
    private ImCacheUtil imCacheUtil;
    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        // 用户已经登陆过
        if (ImContextUtil.getUserId(ctx) != null) {
            return ;
        }
        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            ctx.close();
            log.error("[loginMessageHandler]==>message body is null: {}", imMsg);
            throw new IllegalArgumentException("[loginMessageHandler]==>message body is null");
        }
        ImMsgBody imMsgBody = JSON.parseObject(new String(body), ImMsgBody.class);
        String token = imMsgBody.getToken();
        if (StringUtils.isEmpty(token)) {
            ctx.close();
            log.error("[loginMessageHandler]==>token is empty");
            throw new IllegalArgumentException("token is empty");
        }

        ResponseResult<Long> userIdByToken = imTokenRpc.getUserIdByToken(token);
        Integer userId = userIdByToken != null ? userIdByToken.getData() : null;

        if (userId == null || !userId.equals(imMsgBody.getUserId())) {
            ctx.close();
            log.error("[loginMessageHandler]==>userId error");
            throw new IllegalArgumentException("[loginMessageHandler]==>userId error");
        }

        log.info("[loginMessageHandler]==>[server received message]: " + imMsg);
        ChannelHandlerContextCache.put(userId, ctx);
        ImContextUtil.setUserId(ctx, userId);

        // 设置当前用户连接的IM服务器地址，方便后面router RPC调用im服务(通过mq)
        imCacheUtil.recordImBindAddressOfUserId(userId, imMsgBody.getAppId());

        ImMsg imMsgResponse = ImMsgBuilder.buildLogin(JSON.toJSONString(
                ImMsgBody.builder()
                        .appId(AppIdEnums.QIYU_LIVE_APP.getAppId())
                        .userId(userId)
                        .token(token)
                        .data("true")
                        .build()
        ));
        ctx.writeAndFlush(imMsgResponse);
    }
}
