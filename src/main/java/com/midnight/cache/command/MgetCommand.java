package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class MgetCommand implements Command {


    @Override
    public String name() {
        return "MGET";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String[] keys = getParams(args);

        return Reply.array(cache.mget(keys));
    }
}
