package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class DecrCommand implements Command {


    @Override
    public String name() {
        return "DECR";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);

        return Reply.integer(cache.decr(key));
    }
}
