package dev.burstlime.blueskyconnector;

import bsky4j.BlueskyFactory;
import bsky4j.api.entity.bsky.feed.FeedPostRequest;
import bsky4j.api.entity.bsky.feed.FeedPostResponse;
import bsky4j.api.entity.share.Response;
import bsky4j.domain.Service;
import org.bukkit.plugin.Plugin;

public class BCFeedRequest {

    // プラグインを取得
    private static final Plugin plugin = BlueskyConnector.getPlugin();

    // 投稿する
    public static boolean SendFeedPost(String msg)
    {
        // Bskyとの接続情報を取得
        boolean userSuccess = BlueskyConnector.getUserSuccess();
        String accessJwt = BlueskyConnector.getAccessJwt();

        // 接続されているかを確認する
        if (!userSuccess) return false;

        // 送信する
        try {
            Response<FeedPostResponse> sendResponse = BlueskyFactory
                    .getInstance(Service.BSKY_SOCIAL.getUri())
                    .feed().post(
                            FeedPostRequest.builder()
                                    .accessJwt(accessJwt)
                                    .text(msg)
                                    .build()
                    );
        }
        catch (Exception exception) {
            // 失敗
            plugin.getLogger().warning(plugin.getConfig().getString("message.post-failed"));
            return false;
        }

        plugin.getLogger().warning(plugin.getConfig().getString("message.post-success"));
        return true;
    }
}
