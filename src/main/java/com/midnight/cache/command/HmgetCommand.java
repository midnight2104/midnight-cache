
package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class HmgetCommand implements Command {


    @Override
    public String name() {
        return "HMGET";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getParamsNoKey(args);

        return Reply.array(cache.hmget(key, hkeys));
    }
}
