/*
 * Copyright (c) 2019-2020 GeyserMC. http://geysermc.org
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

package org.geysermc.platform.spigot.world.manager;

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.world.block.BlockTranslator;

/**
 * Should only be used when we know {@link GeyserSpigotWorldManager#getBlockAt(GeyserSession, int, int, int)}
 * cannot be accurate. Typically, this is when ViaVersion is not installed but a client still manages to connect.
 */
public class GeyserSpigotFallbackWorldManager extends GeyserSpigotWorldManager {
    public GeyserSpigotFallbackWorldManager() {
        super(false);
    }

    @Override
    public int getBlockAt(GeyserSession session, int x, int y, int z) {
        return BlockTranslator.AIR;
    }

    @Override
    public void getBlocksInSection(GeyserSession session, int x, int y, int z, Chunk chunk) {
        // Do nothing, since we can't do anything with the chunk
    }

    @Override
    public boolean hasMoreBlockDataThanChunkCache() {
        return false;
    }

    @Override
    public boolean isLegacy() {
        return true;
    }
}