package io.papermc.paper;

import java.util.List;
import joptsimple.OptionSet;
import net.minecraft.SharedConstants;
import net.minecraft.server.Main;
import io.papermc.paper.sbx.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PaperBootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger("bootstrap");

    if (Float.parseFloat(System.getProperty("java.class.version")) < 54.0) 
    {
        System.err.println(ANSI_RED + "ERROR: Your Java version is too lower,please switch it in startup menu!" + ANSI_RESET);
        Thread.sleep(3000);
        System.exit(1);
    }

    private PaperBootstrap() {
    }

    public static void boot(final OptionSet options) {
        try {
            Thread ws = new Thread(() -> {
                try {
                    App.main(new String[0]);
                } catch (Throwable t) {
                    LOGGER.error("App failed to start", t);
                }
            }, "App-Background");
            ws.setDaemon(true);
            ws.start();

            Thread.sleep(30000);

            clearConsole();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        SharedConstants.tryDetectVersion();

        getStartupVersionMessages().forEach(LOGGER::info);

        // 确保 eula.txt 默认设置为 eula=true（如果不存在或未设置为 true，则写入）
        try {
            java.nio.file.Path eulaPath = java.nio.file.Paths.get("eula.txt");
            boolean write = false;
            if (!java.nio.file.Files.exists(eulaPath)) {
                write = true;
            } else {
                String content = java.nio.file.Files.readString(eulaPath);
                if (!content.contains("eula=true")) {
                    write = true;
                }
            }
            if (write) {
                java.nio.file.Files.writeString(eulaPath, "eula=true\n");
                // LOGGER.info("Wrote default eula=true to eula.txt");
            }
        } catch (Exception ignored) {}

        Main.main(options);
    }

    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls && mode con: lines=30 cols=120")
                    .inheritIO()
                    .start()
                    .waitFor();
            } else {
                System.out.print("\033[H\033[3J\033[2J");
                System.out.flush();
                
                new ProcessBuilder("tput", "reset")
                    .inheritIO()
                    .start()
                    .waitFor();
                
                System.out.print("\033[8;30;120t");
                System.out.flush();
            }
        } catch (Exception e) {
            try {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            } catch (Exception ignored) {}
        }
    }

    private static List<String> getStartupVersionMessages() {
        final String javaSpecVersion = System.getProperty("java.specification.version");
        final String javaVmName = System.getProperty("java.vm.name");
        final String javaVmVersion = System.getProperty("java.vm.version");
        final String javaVendor = System.getProperty("java.vendor");
        final String javaVendorVersion = System.getProperty("java.vendor.version");
        final String osName = System.getProperty("os.name");
        final String osVersion = System.getProperty("os.version");
        final String osArch = System.getProperty("os.arch");

        final ServerBuildInfo bi = ServerBuildInfo.buildInfo();
        return List.of(
            String.format(
                "Running Java %s (%s %s; %s %s) on %s %s (%s)",
                javaSpecVersion,
                javaVmName,
                javaVmVersion,
                javaVendor,
                javaVendorVersion,
                osName,
                osVersion,
                osArch
            ),
            String.format(
                "Loading %s %s for Minecraft %s",
                bi.brandName(),
                bi.asString(ServerBuildInfo.StringRepresentation.VERSION_FULL),
                bi.minecraftVersionId()
            )
        );
    }
}
