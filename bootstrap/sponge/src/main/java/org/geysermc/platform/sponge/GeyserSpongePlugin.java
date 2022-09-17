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

package org.geysermc.platform.sponge;

import com.google.inject.Inject;
import org.geysermc.common.PlatformType;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.bootstrap.GeyserBootstrap;
import org.geysermc.connector.command.CommandManager;
import org.geysermc.connector.configuration.ConfigLoader;
import org.geysermc.connector.dump.BootstrapDumpInfo;
import org.geysermc.connector.ping.GeyserLegacyPingPassthrough;
import org.geysermc.connector.ping.IGeyserPingPassthrough;
import org.geysermc.connector.utils.LanguageUtils;
import org.geysermc.platform.sponge.command.GeyserSpongeCommandExecutor;
import org.geysermc.platform.sponge.command.GeyserSpongeCommandManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.nio.file.Path;

@Plugin(id = "geyser", name = GeyserConnector.NAME + "-Sponge", version = GeyserConnector.VERSION, url = "https://geysermc.org", authors = "GeyserMC")
public class GeyserSpongePlugin implements GeyserBootstrap {

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    private GeyserSpongeCommandManager geyserCommandManager;
    private GeyserSpongeConfiguration geyserConfig;
    private GeyserSpongeLogger geyserLogger;
    private IGeyserPingPassthrough geyserSpongePingPassthrough;

    private GeyserConnector connector;

    @Override
    public void onEnable() {
        try {
            geyserConfig = new ConfigLoader<>("config-sponge.yml", GeyserSpongeConfiguration.class).load();
        } catch (Throwable throwable) {
            logger.warn(LanguageUtils.getLocaleStringLog("geyser.config.failed"), throwable);
            return;
        }

        this.geyserLogger = new GeyserSpongeLogger(logger, geyserConfig.isDebugMode());
        this.connector = GeyserConnector.start(PlatformType.SPONGE, this);

        if (geyserConfig.isLegacyPingPassthrough()) {
            this.geyserSpongePingPassthrough = GeyserLegacyPingPassthrough.init(connector);
        } else {
            this.geyserSpongePingPassthrough = new GeyserSpongePingPassthrough();
        }

        this.geyserCommandManager = new GeyserSpongeCommandManager(Sponge.getCommandManager(), connector);
        Sponge.getCommandManager().register(this, new GeyserSpongeCommandExecutor(connector), "geyser");
    }

    @Override
    public void onDisable() {
        connector.shutdown();
    }

    @Override
    public GeyserSpongeConfiguration getGeyserConfig() {
        return geyserConfig;
    }

    @Override
    public GeyserSpongeLogger getGeyserLogger() {
        return geyserLogger;
    }

    @Override
    public CommandManager getGeyserCommandManager() {
        return this.geyserCommandManager;
    }

    @Override
    public IGeyserPingPassthrough getGeyserPingPassthrough() {
        return geyserSpongePingPassthrough;
    }

    @Override
    public Path getConfigFolder() {
        return configDir.toPath();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        onEnable();
    }

    @Listener
    public void onServerStop(GameStoppedEvent event) {
        onDisable();
    }

    @Override
    public BootstrapDumpInfo getDumpInfo() {
        return new GeyserSpongeDumpInfo();
    }

    @Override
    public String getMinecraftServerVersion() {
        return Sponge.getPlatform().getMinecraftVersion().getName();
    }
}
