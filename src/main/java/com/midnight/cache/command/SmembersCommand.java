package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class SmembersCommand implements Command {
    @Override
    public String name() {
        return "SMEMBERS";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        return Reply.array(cache.smembers(key));
    }
}
