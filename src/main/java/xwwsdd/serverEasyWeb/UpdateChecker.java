package xwwsdd.serverEasyWeb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class UpdateChecker {
    private final ServerEasyWeb plugin;
    private final ConfigManager configManager;
    private static final String GITHUB_API_URL = "https://api.github.com/repos/mcraftbbs/ServerEasyWeb/releases/latest";

    public UpdateChecker(ServerEasyWeb plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void checkForUpdates() {
        if (!configManager.isCheckUpdates()) {
            return;
        }

        try {
            URL url = new URL(GITHUB_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String latestVersion = parseVersion(response.toString());
                String currentVersion = plugin.getDescription().getVersion();
                if (!currentVersion.equals(latestVersion)) {
                    plugin.logFancy("A new version (" + latestVersion + ") is available! Download it from GitHub.");
                } else {
                    plugin.logFancy("ServerEasyWeb is up to date (version " + currentVersion + ").");
                }
            } else {
                plugin.getLogger().warning("Failed to check for updates. HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error checking for updates: " + e.getMessage());
        }
    }

    private String parseVersion(String jsonResponse) {
        // Simplified parsing: extract tag_name from JSON
        String tagPrefix = "\"tag_name\":\"";
        int startIndex = jsonResponse.indexOf(tagPrefix) + tagPrefix.length();
        int endIndex = jsonResponse.indexOf("\"", startIndex);
        return jsonResponse.substring(startIndex, endIndex);
    }
}

