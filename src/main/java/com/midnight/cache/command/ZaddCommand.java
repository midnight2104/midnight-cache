
package com.midnight.cache.command;

import com.midnight.cache.MidnightCache;
import com.midnight.cache.core.Command;
import com.midnight.cache.core.Reply;

import java.util.Arrays;

public class ZaddCommand implements Command {
    @Override
    public String name() {
        return "ZADD";
    }

    @Override
    public Reply<?> exec(MidnightCache cache, String[] args) {
        String key = getKey(args);
        String[] score = getHkeys(args);
        String[] vals = getHvals(args);
        return Reply.integer(cache.zadd(key, vals, toDouble(score)));
    }

    double[] toDouble(String[] scores) {
        return Arrays.stream(scores).mapToDouble(Double::parseDouble).toArray();
    }

}
