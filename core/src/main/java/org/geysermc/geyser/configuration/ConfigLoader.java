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

package org.geysermc.geyser.configuration;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.configutils.ConfigUtilities;
import org.geysermc.configutils.file.codec.PathFileCodec;
import org.geysermc.configutils.file.template.ResourceTemplateReader;
import org.geysermc.configutils.loader.validate.Validations;
import org.geysermc.configutils.updater.change.Changes;
import org.geysermc.geyser.GeyserBootstrap;

import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

public class ConfigLoader<T extends GeyserCommonConfiguration<B>, B extends GeyserBootstrap> {
    private final String templateFile;
    private final Class<T> mapTo;
    private final Path dataDirectory;
    private final B geyserBootstrap;

    public ConfigLoader(
            @NonNull String templateFile,
            @NonNull Class<T> mapTo,
            @NonNull Path dataDirectory,
            @Nullable B geyserBootstrap
    ) {
        this.templateFile = Objects.requireNonNull(templateFile);
        this.mapTo = Objects.requireNonNull(mapTo);
        this.dataDirectory = Objects.requireNonNull(dataDirectory);
        this.geyserBootstrap = geyserBootstrap;
    }

    public T load() throws Throwable {
        ConfigUtilities utilities =
                ConfigUtilities.builder()
                        .fileCodec(PathFileCodec.of(dataDirectory))
                        .configFile("config.yml")
                        .templateReader(ResourceTemplateReader.of(getClass()))
                        .template(templateFile)
                        .changes(Changes.builder()
                                .version(
                                        5,
                                        Changes.versionBuilder()
                                                .keyRenamed("userAuths", "user-auths")
                                                .valueChanged("metrics.uuid", "generateduuid", UUID.randomUUID()))
                                .build())
                        .copyDirectly("user-auths")
                        .definePlaceholder("metrics.uuid", UUID::randomUUID)
                        .validations(Validations.builder()
                                .validation("remote.port", new GeyserCommonConfiguration.PortValidator())
                                .build())
                        .postInitializeCallbackArgument(geyserBootstrap)
                        .build();

        return utilities.executeOn(mapTo);
    }
}
