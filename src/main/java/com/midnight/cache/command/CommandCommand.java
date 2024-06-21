package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Commands;
import com.midnight.cache.core.Reply;

public class CommandCommand implements Command {
    @Override
    public String name() {
        return "COMMAND";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        return Reply.array(Commands.getCommandNames());
    }
}
