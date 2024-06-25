
package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class HdelCommand implements Command {


    @Override
    public String name() {
        return "HDEL";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getParamsNoKey(args);

        return Reply.integer(cache.hdel(key, hkeys));
    }
}
