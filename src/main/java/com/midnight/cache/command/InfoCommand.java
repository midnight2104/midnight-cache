package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

public class InfoCommand implements Command {
    private static final String INFO = "KKCache Server[v1.0.1], created by midnight." + CRLF
            + "Mock Redis Server, at 2024-06-21 in Beijing." + CRLF;

    @Override
    public String name() {
        return "INFO";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        return Reply.bulkString(INFO);
    }
}
