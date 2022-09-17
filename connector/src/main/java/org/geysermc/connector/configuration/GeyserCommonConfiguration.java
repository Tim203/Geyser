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

package org.geysermc.connector.configuration;

import lombok.Getter;
import lombok.Setter;
import org.geysermc.configutils.loader.callback.CallbackResult;
import org.geysermc.configutils.loader.callback.GenericPostInitializeCallback;
import org.geysermc.configutils.loader.validate.ValidationResult;
import org.geysermc.configutils.loader.validate.Validator;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.common.AuthType;
import org.geysermc.connector.common.serializer.AsteriskSerializer;
import org.geysermc.connector.network.CIDRMatcher;
import org.geysermc.connector.utils.Constants;
import org.geysermc.connector.utils.LanguageUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@SuppressWarnings("FieldMayBeFinal")
public abstract class GeyserCommonConfiguration<T>
        implements GeyserConfiguration, GenericPostInitializeCallback<T> {

    private BedrockConfiguration bedrock = new BedrockConfiguration();
    private RemoteConfiguration remote = new RemoteConfiguration();

    private String floodgateKeyFile = "key.pem";

    private Path floodgateKeyPath;

    private Map<String, UserAuthenticationInfo> userAuths;

    private boolean commandSuggestions = true;

    private boolean passthroughMotd = false;

    private boolean passthroughPlayerCounts = false;

    private boolean passthroughProtocolName = false;

    private boolean legacyPingPassthrough = false;

    private int pingPassthroughInterval = 3;

    private boolean forwardPlayerPing = false;

    private int maxPlayers = 100;

    private boolean debugMode = false;

    private boolean allowThirdPartyCapes = true;

    private String showCooldown = "title";

    private boolean showCoordinates = true;

    private EmoteOffhandWorkaroundOption emoteOffhandWorkaround = EmoteOffhandWorkaroundOption.DISABLED;

    private boolean allowThirdPartyEars = false;

    private String defaultLocale = null; // is null by default so system language takes priority

    private int cacheImages = 0;

    private boolean allowCustomSkulls = true;

    private boolean addNonBedrockItems = true;

    private boolean aboveBedrockNetherBuilding = false;

    private boolean forceResourcePacks = true;

    private boolean xboxAchievementsEnabled = false;

    private MetricsInfo metrics = new MetricsInfo();

    private int scoreboardPacketThreshold = 10;

    private boolean enableProxyConnections = false;

    private int mtu = 1400;

    private boolean useDirectConnection = true;

    private int configVersion = 0;

    @Override
    public CallbackResult postInitialize(T callbackArgument) {
        return postInitialize().ifSucceeded(() -> {
            // other platforms had their chance to change stuff by overriding postInitialize/0
            if (getBedrock().isCloneRemotePort()) {
                getBedrock().setPort(getRemote().getPort());
            }

            floodgateKeyPath = retrieveFloodgateKeyPath(callbackArgument);
            return CallbackResult.ok();
        });
    }

    protected CallbackResult postInitialize() {
        return CallbackResult.ok();
    }

    protected CallbackResult checkForFloodgate(boolean hasFloodgate) {
        // Remove this in like a year
        try {
            // Should only exist on 1.0
            Class.forName("org.geysermc.floodgate.FloodgateAPI");

            return CallbackResult.failed(LanguageUtils.getLocaleStringLog(
                    "geyser.bootstrap.floodgate.outdated",
                    Constants.FLOODGATE_DOWNLOAD_LOCATION
            ));
        } catch (ClassNotFoundException ignored) {}

        if (getRemote().getAuthType() == AuthType.FLOODGATE && !hasFloodgate) {
            return CallbackResult.failed(
                    LanguageUtils.getLocaleStringLog("geyser.bootstrap.floodgate.not_installed") + " " +
                            LanguageUtils.getLocaleStringLog("geyser.bootstrap.floodgate.disabling")
            );
        } else if (hasFloodgate) {
            // Auto-setting to Floodgate auth when Floodgate is installed
            getRemote().setAuthType(AuthType.FLOODGATE);
        }

        return CallbackResult.ok();
    }

    abstract protected Path retrieveFloodgateKeyPath(T callbackArgument);

    @Getter
    public static class BedrockConfiguration implements IBedrockConfiguration {
        @AsteriskSerializer.Asterisk(isIp = true)
        private String address = "0.0.0.0";

        @Setter
        private int port = 19132;

        private boolean cloneRemotePort = false;

        private String motd1 = "GeyserMC";
        private String motd2 = "Geyser";

        private String serverName = GeyserConnector.NAME;

        private int compressionLevel = 6;

        public int getCompressionLevel() {
            return Math.max(-1, Math.min(compressionLevel, 9));
        }

        private boolean enableProxyProtocol = false;

        private List<String> proxyProtocolWhitelistedIps = Collections.emptyList(); //todo support

        private List<CIDRMatcher> whitelistedIpsMatchers = null;

        public List<CIDRMatcher> getWhitelistedIpsMatchers() {
            // Effective Java, Third Edition; Item 83: Use lazy initialization judiciously
            List<CIDRMatcher> matchers = this.whitelistedIpsMatchers;
            if (matchers == null) {
                synchronized (this) {
                    this.whitelistedIpsMatchers = matchers = proxyProtocolWhitelistedIps.stream()
                            .map(CIDRMatcher::new)
                            .collect(Collectors.toList());
                }
            }
            return Collections.unmodifiableList(matchers);
        }
    }

    @Getter
    public static class RemoteConfiguration implements IRemoteConfiguration {
        @Setter
        @AsteriskSerializer.Asterisk(isIp = true)
        private String address = "127.0.0.1";

        @Setter
        private int port = 25565;

        @Setter
        private AuthType authType = AuthType.ONLINE;

        private boolean allowPasswordAuthentication = true;

        private boolean useProxyProtocol = false;

        private boolean forwardHostname = true; // only true by default for plugin versions
    }

    @Getter
    public static class UserAuthenticationInfo implements IUserAuthenticationInfo {
        @AsteriskSerializer.Asterisk()
        private String email;

        @AsteriskSerializer.Asterisk()
        private String password;

        private boolean microsoftAccount = false;
    }

    @Getter
    public static class MetricsInfo implements IMetricsInfo {
        private boolean enabled = true;

        private String uuid = UUID.randomUUID().toString();
    }

    /**
     * Ensure that the port deserializes in the config as a number no matter what.
     */
    protected static class PortValidator implements Validator {
        @Override
        public ValidationResult validate(String ignored, Object value) {
            if (!(value instanceof Integer)) {
                return ValidationResult.failed("Port number should be an integer");
            }
            int port = (int) value;
            if (port <= 0) {
                return ValidationResult.failed("Port number should be greater than " + 0);
            } else if (port > 65535) {
                return ValidationResult.failed("Port number cannot be greater than " + 65535);
            }
            return ValidationResult.ok(port);
        }
    }
}
