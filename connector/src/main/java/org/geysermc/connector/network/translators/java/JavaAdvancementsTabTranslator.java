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

package org.geysermc.connector.network.translators.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerAdvancementTabPacket;
import org.geysermc.geyser.connector.network.session.GeyserSession;
import org.geysermc.geyser.connector.network.session.cache.AdvancementsCache;
import org.geysermc.geyser.connector.network.translators.PacketTranslator;
import org.geysermc.geyser.connector.network.translators.Translator;

/**
 * Indicates that the client should open a particular advancement tab
 */
@Translator(packet = ServerAdvancementTabPacket.class)
public class JavaAdvancementsTabTranslator extends PacketTranslator<ServerAdvancementTabPacket> {

    @Override
    public void translate(ServerAdvancementTabPacket packet, GeyserSession session) {
        session.getAdvancementsCache().setCurrentAdvancementCategoryId(packet.getTabId());
        session.sendForm(session.getAdvancementsCache().buildListForm(), AdvancementsCache.ADVANCEMENTS_LIST_FORM_ID);
    }
}
