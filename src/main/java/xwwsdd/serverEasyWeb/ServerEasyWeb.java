package xwwsdd.serverEasyWeb;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerEasyWeb extends JavaPlugin {
    private WebServerManager webServerManager;
    private ConfigManager configManager;
    private CommandHandler commandHandler;
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.loadConfigs();
        webServerManager = new WebServerManager(this, configManager);
        commandHandler = new CommandHandler(this, configManager, webServerManager);
        updateChecker = new UpdateChecker(this, configManager);

        logFancy("Starting ServerEasyWeb v" + getDescription().getVersion());
        getCommand("servereasyweb").setExecutor(commandHandler);
        getCommand("servereasyweb").setTabCompleter(commandHandler);

        // Start web server asynchronously with a delay to ensure file extraction
        getServer().getScheduler().runTaskLater(this, () -> {
            webServerManager.startWebServer();
        }, 20L); // 1-second delay (20 ticks)

        if (configManager.isCheckUpdates()) {
            logFancy("Checking for updates on GitHub...");
            updateChecker.checkForUpdates();
        } else {
            logFancy("Update checking is disabled in config.");
        }
    }

    @Override
    public void onDisable() {
        logFancy("Shutting down ServerEasyWeb...");
        webServerManager.stopWebServer();
        logFancy("ServerEasyWeb has been disabled.");
    }

    public void logFancy(String message) {
        String ansiPrefix = "\u001B[36m\u001B[0m \u001B[32m";
        String ansiSuffix = "\u001B[0m";
        getLogger().info(ansiPrefix + message + ansiSuffix);
    }
}
