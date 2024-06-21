
package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class DelCommand implements Command {


    @Override
    public String name() {
        return "DEL";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String[] keys = getParams(args);

        return Reply.integer(cache.del(keys));
    }
}
