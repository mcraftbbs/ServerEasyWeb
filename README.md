![GitHub Views](https://komarev.com/ghpvc/?username=mcraftbbs&repo=ServerEasyWeb&label=Views)

# ServerEasyWeb

A lightweight Minecraft plugin that serves customizable web pages from a local folder, allowing server owners to create a custom web interface for their Minecraft server. Built for compatibility with Minecraft 1.8–1.21 and Java 8–21, it uses Jetty to serve static files (HTML, CSS, JS) from `plugins/ServerEasyWeb/web/`.

## Features

- **Customizable Web Interface**: Serve static web files (HTML, CSS, JS) from the `plugins/ServerEasyWeb/web/` folder.
- **Modern Default Design**: Includes a responsive default page with Tailwind CSS and basic JavaScript interactivity.
- **Reload Command**: Use `/servereasyweb reload` to reload the configuration and restart the web server.
- **Tab Completion**: Supports tab completion for the `reload` command, restricted to users with `servereasyweb.admin` permission.
- **Lightweight and Compatible**: Works with Minecraft 1.8–1.21 (Spigot/Paper) and avoids legacy material warnings in newer versions.
- **No Log Spam**: File-serving logs are suppressed to keep the server console clean.

## Requirements

- **Minecraft Server**: Spigot or Paper, versions 1.8–1.21.
- **Java**: Version 8 or higher.
- **Port Access**: The configured `web-port` (default: 8080) must be open and not used by other applications.

## Installation

1. **Download**:
    - Grab the latest `ServerEasyWeb-1.0.0.jar` from the [Releases](https://github.com/mcraftbbs/ServerEasyWeb/releases) page.

2. **Deploy**:
    - Place the JAR in the `plugins/` folder of your Minecraft server.
    - Delete any existing `plugins/ServerEasyWeb/` folder to ensure fresh file extraction.

3. **Start the Server**:
    - Run your server to generate the `plugins/ServerEasyWeb/` folder, including `config.yml` and the `web/` folder with default files (`index.html`, `styles.css`, `script.js`).

4. **Verify**:
    - Check server logs for:
      ```
      [ServerEasyWeb] Loaded configuration: web-port=8080, check-updates=true
      [ServerEasyWeb] Extracted web file: .../plugins/ServerEasyWeb/web/index.html
      [ServerEasyWeb] Web server started on port 8080
      ```
    - Access `http://<server-ip>:8080/` in a browser to see the default web page.

## Configuration

The plugin uses `plugins/ServerEasyWeb/config.yml` for settings:

```yaml
web-port: 8080
check-updates: true
```

- **web-port**: The port for the web server (default: 8080). Ensure it’s open and not in use.
- **check-updates**: Enable (`true`) or disable (`false`) checking for plugin updates on GitHub.

To apply changes, edit `config.yml` and run `/servereasyweb reload`.

## Customization

Customize the web interface by editing files in `plugins/ServerEasyWeb/web/`:
- **index.html**: The default page, styled with Tailwind CSS (via CDN) and linked to `styles.css` and `script.js`.
- **styles.css**: Additional CSS to complement Tailwind.
- **script.js**: JavaScript for interactivity (e.g., button click events).
- **Additional Files**: Add images, extra HTML pages, or other assets to the `web/` folder. They will be served automatically (e.g., `http://<server-ip>:8080/image.png`).

After editing files, refresh the browser to see changes. Use `/servereasyweb reload` to re-extract default files if needed.

## Commands

- **/servereasyweb reload**:
    - Reloads `config.yml`, re-extracts web files, and restarts the web server.
    - Permission: `servereasyweb.admin` (default: operators only).
    - Tab completion: Type `/servereasyweb ` and press Tab to see `reload`.

Example:
```
/servereasyweb reload
```

## Permissions

- **servereasyweb.admin**:
    - Allows use of `/servereasyweb reload`.
    - Default: Operators (`op`).

## Debugging

If issues occur:
- **Web Page Not Loading**:
    - Check logs for errors like “Failed to start web server.” or “File not found.”
    - Verify `plugins/ServerEasyWeb/web/index.html` exists.
    - Ensure the `web-port` (default: 8080) is open (`netstat -tuln | grep 8080` on Linux).
    - Check file permissions (`chmod -R u+rw plugins/ServerEasyWeb` on Linux).

## Notes

- **Security**: The plugin uses HTTP. For production, consider configuring HTTPS in `WebServerManager.java` with Jetty’s SSL support.
- **Compatibility**: Tested with Minecraft 1.8.8, 1.12.2, 1.16.5, 1.19.4, and 1.21 (Spigot/Paper).
- **GitHub Repository**: Replace `mcraftbbs/ServerEasyWeb` with your actual repository URL in `UpdateChecker.java` for accurate update checks.

## Building from Source

1. Clone the repository:
   ```
   git clone https://github.com/xwwsdd/ServerEasyWeb.git
   ```
2. Build with Gradle:
   ```
   cd ServerEasyWeb
   gradle clean build
   ```
3. Find the JAR in `build/libs/ServerEasyWeb-1.0.0.jar`.

## License


MIT License. See [LICENSE](https://github.com/mcraftbbs/ServerEasyWeb?tab=MIT-1-ov-file) for details.
