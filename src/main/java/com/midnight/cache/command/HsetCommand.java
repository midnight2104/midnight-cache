
package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class HsetCommand implements Command {


    @Override
    public String name() {
        return "HSET";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getHkeys(args);
        String[] hvals = getHvals(args);

        return Reply.integer(cache.hset(key, hkeys, hvals));
    }
}
