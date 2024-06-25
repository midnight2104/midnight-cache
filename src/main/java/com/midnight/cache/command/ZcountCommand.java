
package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class ZcountCommand implements Command {
    @Override
    public String name() {
        return "ZCOUNT";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        double min = Double.parseDouble(getKey(args));
        double max = Double.parseDouble(args[8]);

        return Reply.integer(cache.zcount(key, min, max));
    }

}
