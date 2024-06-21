package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class SetCommand implements Command {


    @Override
    public String name() {
        return "SET";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        String val = getVal(args);

        cache.set(key, val);
        return Reply.string(OK);
    }
}
