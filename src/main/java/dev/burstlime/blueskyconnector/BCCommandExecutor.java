package dev.burstlime.blueskyconnector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import static dev.burstlime.blueskyconnector.BlueskyConnector.BskyConnection;
import static dev.burstlime.blueskyconnector.FeedRequest.SendFeedPost;

public class BCCommandExecutor implements CommandExecutor {
    private static final Plugin plugin = BlueskyConnector.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // プレフィックス取得
        String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("message.prefix"));

        if (command.getName().equalsIgnoreCase("bluesky"))
        { // メインコマンド

            if (args[0].equalsIgnoreCase("help"))
            { // ヘルプ

                // ヘルプメッセージを送信
                sender.sendMessage(getHelpMessage());

                return true;
            }
            else if (args[0].equalsIgnoreCase("post"))
            {
                if (args.length == 2)
                {
                    if (SendFeedPost(args[1])) {
                        // 成功
                        sender.sendMessage(prefix + plugin.getConfig().getString("message.post-success"));
                    } else {
                        // 失敗
                        sender.sendMessage(prefix + plugin.getConfig().getString("message.post-failed"));
                    }
                }
                else
                {
                    sender.sendMessage(prefix + "Usage: /bsky post <message>");
                }

                return true;
            }
            else if (args[0].equalsIgnoreCase("reload"))
            { // リロード

                // コンフィグを再読み込み
                plugin.reloadConfig();

                // Blueskyに接続を試みる
                if (BskyConnection()) {
                    // 成功
                    sender.sendMessage(prefix + plugin.getConfig().getString("message.connection-success"));
                }
                else {
                    // 失敗
                    sender.sendMessage(prefix + plugin.getConfig().getString("message.connection-failed"));
                }

                return true;
            }


            // コマンドが見つからなかったとき
            sender.sendMessage(prefix + plugin.getConfig().getString("message.command-notfound"));
        }

        return true;
    }


    private String getHelpMessage()
    {
        return ChatColor.translateAlternateColorCodes('&',
            "/bsky help        - " + plugin.getConfig().getString("command-description.help") + "\n" +
            "/bsky post <msg>  - " + plugin.getConfig().getString("command-description.post") + "\n" +
            "/bsky reload      - " + plugin.getConfig().getString("command-description.reload") + "\n"
        );
    }
}
