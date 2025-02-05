package com.assured.config;

import org.aeonbits.owner.ConfigCache;

public class ConfigFactory {

    private ConfigFactory() {
    }

    public static Configuration getConfigs() {
        return ConfigCache.getOrCreate(Configuration.class);

    }

}

