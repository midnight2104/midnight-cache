
package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class HexistsCommand implements Command {


    @Override
    public String name() {
        return "HEXISTS";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        String hval = getVal(args);

        return Reply.integer(cache.hexists(key, hval));
    }
}
