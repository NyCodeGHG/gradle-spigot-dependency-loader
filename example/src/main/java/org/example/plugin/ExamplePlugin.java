package org.example.plugin;

import okhttp3.OkHttpClient;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {
    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    public void onEnable() {
        getCommand("time").setExecutor(new CurrentTimeCommand(this));
    }

    @Override
    public void onDisable() {
        httpClient.connectionPool().evictAll();
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }
}
