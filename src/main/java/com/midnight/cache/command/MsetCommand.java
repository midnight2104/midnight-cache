package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class MsetCommand implements Command {


    @Override
    public String name() {
        return "MSET";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String[] keys = getKeys(args);
        String[] vals = getVals(args);

        cache.mset(keys, vals);
        return Reply.string(OK);
    }
}
