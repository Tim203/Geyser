/*
 * Copyright (c) 2019-2021 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.geyser.platform.bungeecord;

import net.md_5.bungee.api.plugin.Plugin;
import org.geysermc.common.PlatformType;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.GeyserBootstrap;
import org.geysermc.geyser.command.CommandManager;
import org.geysermc.geyser.session.auth.AuthType;
import org.geysermc.geyser.configuration.ConfigLoader;
import org.geysermc.geyser.dump.BootstrapDumpInfo;
import org.geysermc.geyser.ping.GeyserLegacyPingPassthrough;
import org.geysermc.geyser.ping.IGeyserPingPassthrough;
import org.geysermc.geyser.text.GeyserLocale;
import org.geysermc.geyser.platform.bungeecord.command.GeyserBungeeCommandExecutor;
import org.geysermc.geyser.platform.bungeecord.command.GeyserBungeeCommandManager;
import org.jetbrains.annotations.Nullable;

import java.net.SocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class GeyserBungeePlugin extends Plugin implements GeyserBootstrap {

    private GeyserBungeeCommandManager geyserCommandManager;
    private GeyserBungeeConfiguration geyserConfig;
    private GeyserBungeeInjector geyserInjector;
    private GeyserBungeeLogger geyserLogger;
    private IGeyserPingPassthrough geyserBungeePingPassthrough;

    private GeyserImpl geyser;

    @Override
    public void onEnable() {
        try {
            geyserConfig = new ConfigLoader<>("config-plugin.yml", GeyserBungeeConfiguration.class, this).load();
        } catch (Throwable throwable) {
            getLogger().log(Level.WARNING, GeyserLocale.getLocaleStringLog("geyser.config.failed"), throwable);
        }

        this.geyserLogger = new GeyserBungeeLogger(getLogger(), geyserConfig.isDebugMode());

        this.geyser = GeyserImpl.start(PlatformType.BUNGEECORD, this);

        this.geyserInjector = new GeyserBungeeInjector(this);
        this.geyserInjector.initializeLocalChannel(this);

        this.geyserCommandManager = new GeyserBungeeCommandManager(geyser);

        if (geyserConfig.isLegacyPingPassthrough()) {
            this.geyserBungeePingPassthrough = GeyserLegacyPingPassthrough.init(geyser);
        } else {
            this.geyserBungeePingPassthrough = new GeyserBungeePingPassthrough(getProxy());
        }

        this.getProxy().getPluginManager().registerCommand(this, new GeyserBungeeCommandExecutor(geyser));
    }

    @Override
    public void onDisable() {
        if (geyser != null) {
            geyser.shutdown();
        }
        if (geyserInjector != null) {
            geyserInjector.shutdown();
        }
    }

    @Override
    public GeyserBungeeConfiguration getGeyserConfig() {
        return geyserConfig;
    }

    @Override
    public GeyserBungeeLogger getGeyserLogger() {
        return geyserLogger;
    }

    @Override
    public CommandManager getGeyserCommandManager() {
        return this.geyserCommandManager;
    }

    @Override
    public IGeyserPingPassthrough getGeyserPingPassthrough() {
        return geyserBungeePingPassthrough;
    }

    @Override
    public Path getConfigFolder() {
        return getDataFolder().toPath();
    }

    @Override
    public BootstrapDumpInfo getDumpInfo() {
        return new GeyserBungeeDumpInfo(getProxy());
    }

    @Override
    public Path getLogsPath() {
        return Paths.get(getProxy().getName().equals("BungeeCord") ? "proxy.log.0" : "logs/latest.log");
    }

    @Nullable
    @Override
    public SocketAddress getSocketAddress() {
        return this.geyserInjector.getServerSocketAddress();
    }
}
