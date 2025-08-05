package xwwsdd.serverEasyWeb;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class WebServerManager {
    private final ServerEasyWeb plugin;
    private final ConfigManager configManager;
    private Server webServer;

    public WebServerManager(ServerEasyWeb plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void startWebServer() {
        webServer = new Server(configManager.getWebPort());
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        context.addServlet(new ServletHolder(new StaticServlet()), "/*");
        webServer.setHandler(context);

        try {
            webServer.start();
            plugin.logFancy("Web server started on port " + configManager.getWebPort());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to start web server", e);
        }
    }

    public void stopWebServer() {
        if (webServer != null) {
            try {
                webServer.stop();
                plugin.logFancy("Web server stopped successfully");
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to stop web server", e);
            }
        }
    }

    private class StaticServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String pathInfo = request.getPathInfo();
            String path = pathInfo == null || pathInfo.equals("/") ? "index.html" : pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
            Path filePath = Paths.get(plugin.getDataFolder().getAbsolutePath(), "web", path);
            plugin.getLogger().fine("Attempting to serve file: " + filePath + " for request URI: " + request.getRequestURI());

            if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
                plugin.getLogger().warning("File not found or is directory: " + filePath);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("text/html");
                response.getWriter().write("<h1>404 Not Found</h1><p>File " + path + " not found in web directory.</p>");
                return;
            }

            String contentType = path.endsWith(".html") ? "text/html" :
                    path.endsWith(".css") ? "text/css" :
                            path.endsWith(".js") ? "application/javascript" : "application/octet-stream";
            response.setContentType(contentType);
            try (InputStream is = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, bytesRead);
                }
                plugin.getLogger().fine("Successfully served file: " + filePath);
            } catch (IOException e) {
                plugin.getLogger().warning("Error serving file " + filePath + ": " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("text/html");
                response.getWriter().write("<h1>500 Internal Server Error</h1><p>Error reading file " + path + ".</p>");
            }
        }
    }
}