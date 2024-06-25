
package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class HgetCommand implements Command {


    @Override
    public String name() {
        return "HGET";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        String hkey = getVal(args);

        return Reply.bulkString(cache.hget(key, hkey));
    }
}
