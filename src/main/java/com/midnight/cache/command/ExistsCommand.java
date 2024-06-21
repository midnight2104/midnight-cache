package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class ExistsCommand implements Command {


    @Override
    public String name() {
        return "EXISTS";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String[] keys = getParams(args);

        return Reply.integer(cache.exists(keys));
    }
}
