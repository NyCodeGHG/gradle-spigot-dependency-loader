package org.example.plugin;

import okhttp3.HttpUrl;
import okhttp3.Request;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;

public record CurrentTimeCommand(ExamplePlugin plugin) implements CommandExecutor {
    private final static HttpUrl TIME_ENDPOINT = HttpUrl.get("https://showcase.api.linx.twenty57.net/UnixTime/fromunix");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var request = new Request.Builder()
                .url(TIME_ENDPOINT.newBuilder()
                        .addQueryParameter("timestamp", String.valueOf(Instant.now().getEpochSecond()))
                        .build()
                )
                .get()
                .build();

        try (var response = plugin.getHttpClient().newCall(request).execute()) {
            var body = response.body();
            if (body == null) {
                sender.sendMessage("An error occurred");
                return false;
            }

            sender.sendMessage("Time: %s".formatted(body.string()));
        } catch (IOException e) {
            sender.sendMessage("An error occurred!");
            e.printStackTrace();
        }
        return false;
    }
}
