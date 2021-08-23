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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.configutils.ConfigUtilities;
import org.geysermc.configutils.file.codec.PathFileCodec;
import org.geysermc.configutils.file.template.ResourceTemplateReader;
import org.geysermc.configutils.updater.change.Changes;

import java.util.Objects;
import java.util.UUID;

public class ConfigLoader<T extends GeyserCommonConfiguration> {
    private final String templateFile;
    private final Class<T> mapTo;

    public ConfigLoader(@NonNull String templateFile, @NonNull Class<T> mapTo) {
        this.templateFile = Objects.requireNonNull(templateFile);
        this.mapTo = Objects.requireNonNull(mapTo);
    }

    public T load() throws Throwable {
        ConfigUtilities utilities =
                ConfigUtilities.builder()
                        .fileCodec(PathFileCodec.instance())
                        .configFile("config.yml")
                        .templateReader(ResourceTemplateReader.of(getClass()))
                        .template(templateFile)
                        .changes(Changes.builder()
                                .version(
                                        5,
                                        Changes.versionBuilder()
                                                .keyRenamed("userAuths", "user-auths"))
                                .build())
                        .copyDirectly("user-auths")
                        .definePlaceholder("metrics.uuid", UUID::randomUUID)
                        .build();

        return utilities.executeOn(mapTo);
    }
}
