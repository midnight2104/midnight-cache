package com.midnight.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MidnightApplicationListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    List<Plugin> plugins;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            for (Plugin plugin : plugins) {
                plugin.init();
                plugin.startup();
            }
        } else if (event instanceof ContextClosedEvent) {
            for (Plugin plugin : plugins) {
                plugin.shutdown();
            }
        }
    }
}
