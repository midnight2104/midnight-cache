package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class StrLenCommand implements Command {


    @Override
    public String name() {
        return "STRLEN";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);

        return Reply.integer(cache.strlen(key));
    }
}
