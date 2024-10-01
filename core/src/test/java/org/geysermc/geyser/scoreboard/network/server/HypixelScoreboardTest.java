/*
 * Copyright (c) 2024 GeyserMC. http://geysermc.org
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

package org.geysermc.geyser.scoreboard.network.server;

import static org.geysermc.geyser.scoreboard.network.util.GeyserMockContextScoreboard.mockAndAddPlayerEntity;
import static org.geysermc.geyser.scoreboard.network.util.GeyserMockContextScoreboard.mockContextScoreboard;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.geysermc.geyser.translator.protocol.java.scoreboard.JavaSetDisplayObjectiveTranslator;
import org.geysermc.geyser.translator.protocol.java.scoreboard.JavaSetObjectiveTranslator;
import org.geysermc.geyser.translator.protocol.java.scoreboard.JavaSetPlayerTeamTranslator;
import org.geysermc.geyser.translator.protocol.java.scoreboard.JavaSetScoreTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.chat.numbers.BlankFormat;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.CollisionRule;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.NameTagVisibility;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.ObjectiveAction;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.ScoreType;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.ScoreboardPosition;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.TeamAction;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.TeamColor;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard.ClientboundSetDisplayObjectivePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard.ClientboundSetObjectivePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard.ClientboundSetPlayerTeamPacket;
import org.junit.jupiter.api.Test;

public class HypixelScoreboardTest {
    @Test
    void test() {
        mockContextScoreboard(context -> {
            var setTeamTranslator = new JavaSetPlayerTeamTranslator();
            var setObjectiveTranslator = new JavaSetObjectiveTranslator();
            var setDisplayObjectiveTranslator = new JavaSetDisplayObjectiveTranslator();
            var setScoreTranslator = new JavaSetScoreTranslator();


            // First Hypixel creates teams for the different ranks that are online, after that two for NPCs


            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a559-16ff5980",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a559-16ff5980")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).children(List.of(
                        Component.text("[MVP", NamedTextColor.AQUA),
                        Component.text("+", NamedTextColor.BLACK),
                        Component.text("]", NamedTextColor.AQUA))),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).children(List.of(
                        Component.text(" "),
                        Component.text("[OMEN]", NamedTextColor.YELLOW))),
                    true,
                    true,
                    NameTagVisibility.ALWAYS,
                    CollisionRule.NEVER,
                    TeamColor.AQUA,
                    new String[] { "A_Player" }));

            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a828-238b2c3a",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a828-238b2c3a")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("[VIP]", NamedTextColor.GREEN)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.ALWAYS,
                    CollisionRule.NEVER,
                    TeamColor.GREEN,
                    new String[] { "B_Player" }));

            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "NPCListShow",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("NPCListShow")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("[NPC]", NamedTextColor.YELLOW)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.ALWAYS,
                    CollisionRule.NEVER,
                    TeamColor.YELLOW,
                    new String[0]));

            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "NPCListHide",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("NPCListHide")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("[NPC]", NamedTextColor.DARK_GRAY)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.NEVER,
                    CollisionRule.NEVER,
                    TeamColor.DARK_GRAY,
                    new String[0]));


            // Now it creates, shows, and almost immediately removes an objective?


            context.translate(
                setObjectiveTranslator,
                new ClientboundSetObjectivePacket(
                    "MMLobby",
                    ObjectiveAction.ADD,
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("MURDER MYSTERY", NamedTextColor.YELLOW, TextDecoration.BOLD)),
                    ScoreType.INTEGER,
                    BlankFormat.INSTANCE));

            context.translate(
                setDisplayObjectiveTranslator, new ClientboundSetDisplayObjectivePacket(ScoreboardPosition.SIDEBAR, "MMLobby"));

            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "|||||||||||||||",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("|||||||||||||||")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("", NamedTextColor.WHITE)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.ALWAYS,
                    CollisionRule.NEVER,
                    TeamColor.WHITE,
                    new String[0]));

            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket("|||||||||||||||", TeamAction.ADD_PLAYER, new String[] { "Tim203" }));

            context.translate(
                setObjectiveTranslator,
                new ClientboundSetObjectivePacket("MMLobby", ObjectiveAction.REMOVE, null, ScoreType.INTEGER, null));

            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("a559-16ff5980"));
            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("a828-238b2c3a"));
            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("NPCListShow"));
            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("NPCListHide"));


            // Now the final sidebar objective is added


            context.translate(
                setObjectiveTranslator,
                new ClientboundSetObjectivePacket(
                    "MMLobby",
                    ObjectiveAction.ADD,
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("MURDER MYSTERY", NamedTextColor.YELLOW, TextDecoration.BOLD)),
                    ScoreType.INTEGER,
                    BlankFormat.INSTANCE));

            context.translate(
                setDisplayObjectiveTranslator, new ClientboundSetDisplayObjectivePacket(ScoreboardPosition.SIDEBAR, "MMLobby"));


            // ClientboundPlayerInfoUpdatePacket which sets the gamemode to adventure, and after that some more
            // teams for NPCs are created and the NPCs are added to those teams


            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a999-76d80d5f",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a999-76d80d5f")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("", NamedTextColor.WHITE)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.ALWAYS,
                    CollisionRule.NEVER,
                    TeamColor.WHITE,
                    new String[0]));
            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a999-76d80d5f",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a999-76d80d5f")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("", NamedTextColor.WHITE)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.NEVER,
                    CollisionRule.NEVER,
                    TeamColor.WHITE));
            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a999-76d80d5f",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a999-76d80d5f")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("[NPC] ", NamedTextColor.DARK_GRAY)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.NEVER,
                    CollisionRule.NEVER,
                    TeamColor.DARK_GRAY));
            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a999-76d80d5f",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a999-76d80d5f")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("[NPC] ", NamedTextColor.DARK_GRAY)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.NEVER,
                    CollisionRule.NEVER,
                    TeamColor.DARK_GRAY));

            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("a999-76d80d5f", TeamAction.ADD_PLAYER, new String[] { "g8e29094tn" }));
            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("a999-76d80d5f", TeamAction.ADD_PLAYER, new String[] { "9nblpy3ml0" }));

            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a9992076abcd",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a9992076abcd")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("", NamedTextColor.WHITE)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.ALWAYS,
                    CollisionRule.NEVER,
                    TeamColor.WHITE,
                    new String[0]));
            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a9992076abcd",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a9992076abcd")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("", NamedTextColor.WHITE)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.NEVER,
                    CollisionRule.NEVER,
                    TeamColor.WHITE));
            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a9992076abcd",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a9992076abcd")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("[NPC] ", NamedTextColor.DARK_GRAY)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.NEVER,
                    CollisionRule.NEVER,
                    TeamColor.LIGHT_PURPLE));
            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a9992076abcd",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a9992076abcd")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("[NPC] ", NamedTextColor.DARK_GRAY)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.NEVER,
                    CollisionRule.NEVER,
                    TeamColor.LIGHT_PURPLE));

            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("a9992076abcd", TeamAction.ADD_PLAYER, new String[] { "xtl79x640i" }));
            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("a999-76d80d5f", TeamAction.ADD_PLAYER, new String[] { "q75t5u20yq" }));
            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("a999-76d80d5f", TeamAction.ADD_PLAYER, new String[] { "1elb7pgppv" }));
            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("a999-76d80d5f", TeamAction.ADD_PLAYER, new String[] { "515dcn714k" }));
            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("a999-76d80d5f", TeamAction.ADD_PLAYER, new String[] { "x6g9294a6r" }));
            context.translate(setTeamTranslator, new ClientboundSetPlayerTeamPacket("a999-76d80d5f", TeamAction.ADD_PLAYER, new String[] { "6mcrbi3l8k" }));

            // for each NPC the ClientboundPlayerInfoUpdatePacket is sent,  packet for each of the following actions:
            // ADD_PLAYER, INITIALIZE_CHAT, UPDATE_GAME_MODE, UPDATE_LISTED (true), UPDATE_DISPLAY_NAME.
            // The NPCs have UUIDv2's
            mockAndAddPlayerEntity(context, "g8e29094tn", 2);
            mockAndAddPlayerEntity(context, "x6g9294a6r", 3);
            mockAndAddPlayerEntity(context, "1elb7pgppv", 4);
            // ClientboundPlayerInfoUpdatePacket is sent for self, twice
            mockAndAddPlayerEntity(context, "C_Player", 5);
            mockAndAddPlayerEntity(context, "D_Player", 6);


            // The default rank is created, and the player is added


            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a908203421f7",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a908203421f7")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("", NamedTextColor.WHITE)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.ALWAYS,
                    CollisionRule.NEVER,
                    TeamColor.WHITE));
            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a908203421f7",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a908203421f7")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("", NamedTextColor.GRAY)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.ALWAYS,
                    CollisionRule.NEVER,
                    TeamColor.GRAY));
            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket(
                    "a908203421f7",
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("a908203421f7")),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))).append(Component.text("", NamedTextColor.GRAY)),
                    Component.text("", Style.style(TextDecoration.ITALIC.withState(false))),
                    true,
                    true,
                    NameTagVisibility.ALWAYS,
                    CollisionRule.NEVER,
                    TeamColor.GRAY));
            context.translate(
                setTeamTranslator,
                new ClientboundSetPlayerTeamPacket("a908203421f7", TeamAction.ADD_PLAYER, new String[] { "Tim203" }));
        });
    }
}
