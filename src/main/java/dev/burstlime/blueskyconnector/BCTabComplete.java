package dev.burstlime.blueskyconnector;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BCTabComplete implements TabCompleter {

    private static final Plugin plugin = BlueskyConnector.getPlugin();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        String[] commandList = {"help", "post", "reload"};

        if (command.getName().equalsIgnoreCase("bluesky")) {
            if (args.length == 1) {
                if (args[0].isEmpty()){
                    // 引数未入力
                    return Arrays.asList(commandList);
                }else{
                    // 検索
                    List<String> searchlist = new ArrayList<>();
                    for (String s : commandList) {if (s.startsWith(args[0])) {searchlist.add(s);}}
                    return Arrays.asList(searchlist.toArray(new String[searchlist.size()]));
                }
            }
        }

        // 何も見つからなかったとき
        return null;
    }

}
