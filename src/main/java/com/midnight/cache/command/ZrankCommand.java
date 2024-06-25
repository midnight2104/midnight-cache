
package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class ZrankCommand implements Command {
    @Override
    public String name() {
        return "ZRANK";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        String val = getVal(args);
        return Reply.integer(cache.zrank(key, val));
    }

}
