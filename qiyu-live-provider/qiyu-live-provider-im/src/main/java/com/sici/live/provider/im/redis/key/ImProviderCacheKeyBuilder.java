package com.sici.live.provider.im.redis.key;

import com.sici.framework.redis.key.RedisKeyBuilder;
import com.sici.framework.redis.key.RedisKeyLoadMatch;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Configurable
@Component
@Conditional(RedisKeyLoadMatch.class)
public class ImProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static String IM_LOGIN_TOKEN_KEY = "imLoginToken";
    private static String IM_MESSAGE_ACK_KEY = "imAckMap";

    public String buildImLoginTokenKey(String token) {
        return super.getPrefix() + IM_LOGIN_TOKEN_KEY +
                super.getSplitItem() + token;
    }

    public List<String> buildImLoginTokenKey(List<String> token) {
        List<String> keys = new ArrayList<>();
        token.forEach(id -> keys.add(buildImLoginTokenKey(id)));
        return keys;
    }

    public String buildImMsgAckKey(Long userId, Integer appId) {
        return super.getPrefix() + IM_MESSAGE_ACK_KEY + super.getSplitItem() +
                appId + super.getSplitItem() +
                (userId % 100);
    }
}