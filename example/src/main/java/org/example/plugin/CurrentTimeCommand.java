package org.example.plugin;

import okhttp3.HttpUrl;
import okhttp3.Request;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CurrentTimeCommand implements CommandExecutor {
    private ExamplePlugin plugin;
    private final static HttpUrl TIME_ENDPOINT = HttpUrl.get("https://showcase.api.linx.twenty57.net/UnixTime/fromunix");

    public CurrentTimeCommand(ExamplePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var request = new Request.Builder()
                .url(TIME_ENDPOINT.newBuilder()
                        .addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()))
                        .build()
                )
                .get()
                .build();

        try (var response = plugin.getHttpClient().newCall(request).execute();) {
            sender.sendMessage("Time: %s".formatted(response.body().string()));
        } catch (IOException e) {
            sender.sendMessage("An error occurred!");
            e.printStackTrace();
        }
        return false;
    }
}
