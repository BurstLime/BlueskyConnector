package dev.burstlime.blueskyconnector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import static dev.burstlime.blueskyconnector.BlueskyConnector.BskyConnection;
import static dev.burstlime.blueskyconnector.BCFeedRequest.SendFeedPost;
import static dev.burstlime.blueskyconnector.BlueskyConnector.getPrefix;

public class BCCommandExecutor implements CommandExecutor {
    private static final Plugin plugin = BlueskyConnector.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equalsIgnoreCase("bluesky"))
        { // メインコマンド

            if (args[0].equalsIgnoreCase("help"))
            { // ヘルプ

                // 権限確認
                if (!CheckPermission(sender, "blueskyconnector.commands.help")) return true;

                // ヘルプメッセージを送信
                sender.sendMessage(getHelpMessage());

                return true;
            }
            else if (args[0].equalsIgnoreCase("post"))
            {
                // 権限確認
                if (!CheckPermission(sender, "blueskyconnector.commands.post")) return true;

                if (args.length == 2)
                {
                    SendFeedPost(sender, args[1]);
                }
                else
                {
                    sender.sendMessage(getPrefix() + "Usage: /bsky post <message>");
                }

                return true;
            }
            else if (args[0].equalsIgnoreCase("reload"))
            { // リロード

                // 権限確認
                if (!CheckPermission(sender, "blueskyconnector.commands.reload")) return true;

                // コンフィグを再読み込み
                plugin.reloadConfig();

                // Blueskyに接続を試みる
                BskyConnection(sender);

                return true;
            }


            // コマンドが見つからなかったとき
            sender.sendMessage(getPrefix() + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("message.command-notfound")));
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

    private boolean CheckPermission(CommandSender sender, String permission)
    {
        if (sender.hasPermission(permission)) {
            // 権限を持っている
            return true;
        }

        // 権限を持っていない
        sender.sendMessage(
                getPrefix() + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("message.command-nopermission"))
        );
        return false;
    }
}
