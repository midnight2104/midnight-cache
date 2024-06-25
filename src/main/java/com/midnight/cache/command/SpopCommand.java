package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class SpopCommand implements Command {
    @Override
    public String name() {
        return "SPOP";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        int count = 1;
        if (args.length > 6) {
            String val = getVal(args);
            count = Integer.parseInt(val);
            return Reply.array(cache.spop(key, count));
        }

        String[] spop = cache.spop(key, count);
        return Reply.bulkString(spop == null ? null : spop[0]);
    }
}
