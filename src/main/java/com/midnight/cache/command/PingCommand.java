package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class PingCommand implements Command {
    @Override
    public String name() {
        return "PING";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String ret = "PONG";
        if (args.length >= 5) {

            ret = args[4];
        }

        return Reply.string(ret);
    }
}
