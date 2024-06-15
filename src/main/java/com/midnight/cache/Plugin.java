package com.midnight.cache;

public interface Plugin {
    void init();

    void startup();

    void shutdown();
}
