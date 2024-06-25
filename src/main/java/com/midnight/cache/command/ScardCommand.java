package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class ScardCommand implements Command {
    @Override
    public String name() {
        return "SCARD";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.scard(key));
    }
}
