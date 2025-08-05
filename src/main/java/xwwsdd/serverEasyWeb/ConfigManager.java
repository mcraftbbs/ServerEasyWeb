package xwwsdd.serverEasyWeb;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {
    private final ServerEasyWeb plugin;
    private int webPort;
    private boolean checkUpdates;

    public ConfigManager(ServerEasyWeb plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        plugin.saveDefaultConfig();
        extractWebFiles();
        webPort = plugin.getConfig().getInt("web-port", 8080);
        checkUpdates = plugin.getConfig().getBoolean("check-updates", true);
        plugin.logFancy("Loaded configuration: web-port=" + webPort + ", check-updates=" + checkUpdates);
    }

    private void extractWebFiles() {
        File webDir = new File(plugin.getDataFolder(), "web");
        if (!webDir.exists()) {
            webDir.mkdirs();
        }
        String[] webFiles = {"index.html", "styles.css", "tailwind.css", "script.js"};
        for (String file : webFiles) {
            File targetFile = new File(webDir, file);
            if (!targetFile.exists()) {
                try {
                    plugin.saveResource("web/" + file, false);
                    plugin.logFancy("Extracted web file: " + targetFile.getAbsolutePath());
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to extract web file: " + file + " - " + e.getMessage());
                }
            } else {
                plugin.logFancy("Web file already exists: " + targetFile.getAbsolutePath());
            }
        }
        // Verify extraction
        for (String file : webFiles) {
            File targetFile = new File(webDir, file);
            if (!targetFile.exists()) {
                plugin.getLogger().severe("Web file missing after extraction: " + targetFile.getAbsolutePath());
            }
        }
    }

    public int getWebPort() {
        return webPort;
    }

    public boolean isCheckUpdates() {
        return checkUpdates;
    }
}
