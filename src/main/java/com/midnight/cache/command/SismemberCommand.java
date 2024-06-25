package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class SismemberCommand implements Command {
    @Override
    public String name() {
        return "SISMEMBER";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        String val = getVal(args);
        return Reply.integer(cache.sismember(key, val));
    }
}
