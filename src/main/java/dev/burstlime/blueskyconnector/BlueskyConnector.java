package dev.burstlime.blueskyconnector;

import bsky4j.BlueskyFactory;
import bsky4j.api.entity.atproto.server.ServerCreateSessionRequest;
import bsky4j.api.entity.atproto.server.ServerCreateSessionResponse;
import bsky4j.api.entity.share.Response;
import bsky4j.domain.Service;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static dev.burstlime.blueskyconnector.BCFeedRequest.SendFeedPost;

public final class BlueskyConnector extends JavaPlugin implements Listener {

    private static Plugin plugin;
    private static String accessJwt;
    private static boolean userSuccess;

    public static Plugin getPlugin() {return plugin;}
    public static boolean getUserSuccess() {return userSuccess;}
    public static String getAccessJwt() {return accessJwt;}


    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        saveDefaultConfig();

        // Blueskyに接続する
        BskyConnection();

        // 起動情報を投稿する
        if (plugin.getConfig().getBoolean("event.server-start.enabled"))
        {
            SendFeedPost(plugin.getConfig().getString("event.server-start.message"));
        }

        // イベントを登録する
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new BCEventListener(), this);

        // コマンドを登録する
        getCommand("bluesky").setExecutor(new BCCommandExecutor());
        getCommand("bluesky").setTabCompleter(new BCTabComplete());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // 停止情報を投稿する
        if (plugin.getConfig().getBoolean("event.server-stop.enabled"))
        {
            SendFeedPost(plugin.getConfig().getString("event.server-stop.message"));
        }
    }

    // アカウントを接続する
    public static boolean BskyConnection()
    {
        // 変数を初期化する
        accessJwt = null;

        // アカウント情報を取得する
        String userID = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");

        // セッションを開始する
        try
        {
            Response<ServerCreateSessionResponse> response = BlueskyFactory
                    .getInstance(Service.BSKY_SOCIAL.getUri())
                    .server().createSession(
                            ServerCreateSessionRequest.builder()
                                    .identifier(userID)
                                    .password(password)
                                    .build()
                    );

            accessJwt = response.get().getAccessJwt();

            // アクセスに成功したとき
            plugin.getLogger().info(plugin.getConfig().getString("message.connection-success"));
            userSuccess = true;
        }
        catch (Exception exception)
        {
            // エラーが発生したとき
            plugin.getLogger().warning(plugin.getConfig().getString("message.connection-failed"));
            userSuccess = false;
        }

        return userSuccess;
    }
}
