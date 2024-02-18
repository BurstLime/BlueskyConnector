package dev.burstlime.blueskyconnector;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

import static dev.burstlime.blueskyconnector.FeedRequest.SendFeedPost;

public class EventListener implements Listener {

    private static final Plugin plugin = BlueskyConnector.getPlugin();
    private static UUID kickUUID = null;

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event)
    {
        if (plugin.getConfig().getBoolean("event.player-join.enabled"))
        {
            SendFeedPost(plugin.getConfig().getString("event.player-join.message")
                    .replace("{name}", event.getPlayer().getName())
                    .replace("{display-name}", event.getPlayer().getDisplayName())
            );
        }
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event)
    {
        // キックが原因か調べる
        if (kickUUID == event.getPlayer().getUniqueId())
        {
            kickUUID = null;
            return;
        }

        if (plugin.getConfig().getBoolean("event.player-quit.enabled"))
        {
            SendFeedPost(plugin.getConfig().getString("event.player-quit.message")
                    .replace("{name}", event.getPlayer().getName())
                    .replace("{display-name}", event.getPlayer().getDisplayName())
            );
        }
    }

    @EventHandler
    void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (plugin.getConfig().getBoolean("event.player-chat.enabled"))
        {
            SendFeedPost(plugin.getConfig().getString("event.player-chat.message")
                    .replace("{name}", event.getPlayer().getName())
                    .replace("{display-name}", event.getPlayer().getDisplayName())
                    .replace("{chat}", event.getMessage())
            );
        }
    }

    @EventHandler
    void onPlayerKick(PlayerKickEvent event)
    {
        if (plugin.getConfig().getBoolean("event.player-kick.enabled"))
        {

            SendFeedPost(plugin.getConfig().getString("event.player-kick.message")
                    .replace("{name}", event.getPlayer().getName())
                    .replace("{display-name}", event.getPlayer().getDisplayName())
                    .replace("{reason}", event.getReason())
            );

            kickUUID = event.getPlayer().getUniqueId();
        }
    }

    @EventHandler
    void onPlayerAdvancement(PlayerAdvancementDoneEvent event)
    {
        if (plugin.getConfig().getBoolean("event.player-advancement.enabled"))
        {
            SendFeedPost(plugin.getConfig().getString("event.player-advancement.message")
                    .replace("{name}", event.getPlayer().getName())
                    .replace("{display-name}", event.getPlayer().getDisplayName())
                    .replace("{advancement}", event.getAdvancement().toString())
            );
        }
    }
}
