package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class RpopCommand implements Command {


    @Override
    public String name() {
        return "RPOP";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        int count = 1;
        if (args.length > 6) {
            String val = getVal(args);
            count = Integer.parseInt(val);
            return Reply.array(cache.rpop(key, count));
        }
        String[] rpop = cache.rpop(key, count);
        return Reply.bulkString(rpop == null ? null : rpop[0]);
    }
}
