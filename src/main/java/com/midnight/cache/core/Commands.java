package com.midnight.cache.core;

import com.midnight.cache.command.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class Commands {
    private static Map<String, Command> ALL = new LinkedHashMap<>();

    static {
        initCommands();
    }

    private static void initCommands() {
        // common commands
        register(new PingCommand());
        register(new InfoCommand());
        register(new CommandCommand());

        // string
        register(new SetCommand());
        register(new GetCommand());
        register(new StrLenCommand());
        register(new DelCommand());
        register(new ExistsCommand());
        register(new IncrCommand());
        register(new DecrCommand());
        register(new MsetCommand());
        register(new MgetCommand());

        // list
        // Lpush, Rpush, Lpop, Rpop, Llen, Lindex, Lrange
        register(new LpushCommand());
        register(new LpopCommand());
        register(new RpopCommand());
        register(new RpushCommand());
        register(new LlenCommand());
        register(new LindexCommand());
        register(new LrangeCommand());


        // set
        register(new SaddCommand());
        register(new SmembersCommand());
        register(new SremCommand());
        register(new ScardCommand());
        register(new SpopCommand());
        register(new SismemberCommand());

        // hash: hset hget hlen hgetall hdel hexists hmget
        register(new HsetCommand());
        register(new HgetCommand());
        register(new HgetallCommand());
        register(new HlenCommand());
        register(new HdelCommand());
        register(new HexistsCommand());
        register(new HmgetCommand());

        // zset
        register(new ZaddCommand());
        register(new ZcardCommand());
        register(new ZscoreCommand());
        register(new ZremCommand());
        register(new ZrankCommand());
        register(new ZcountCommand());
    }

    public static void register(Command command) {
        ALL.put(command.name(), command);
    }

    public static Command get(String name) {
        return ALL.get(name);
    }

    public static String[] getCommandNames() {
        return ALL.keySet().toArray(new String[0]);
    }

}
