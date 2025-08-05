package xwwsdd.serverEasyWeb;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private final ServerEasyWeb plugin;
    private final ConfigManager configManager;
    private final WebServerManager webServerManager;

    public CommandHandler(ServerEasyWeb plugin, ConfigManager configManager, WebServerManager webServerManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.webServerManager = webServerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("servereasyweb.admin")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§6Usage: /" + label + " reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            webServerManager.stopWebServer();
            configManager.loadConfigs();
            webServerManager.startWebServer();
            sender.sendMessage("§aServerEasyWeb reloaded successfully.");
            return true;
        }

        sender.sendMessage("§cUnknown subcommand. Usage: /" + label + " reload");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("servereasyweb.admin")) {
            return Arrays.asList("reload");
        }
        return Collections.emptyList();
    }
}
