/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.19.3, mod version: 2.8.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.respawndelay.cmds;

import java.util.Iterator;
import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.respawndelay.events.RespawningEvent;
import com.natamus.respawndelay.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class CommandRespawnall {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("respawnall").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				int amountrespawned = 0;
				Set<Player> spectating_players = RespawningEvent.death_times.keySet();
				Iterator<Player> it = spectating_players.iterator();
				while (it.hasNext()) {
					Player nextplayer = it.next();
					Util.respawnPlayer(nextplayer.getCommandSenderWorld(), (ServerPlayer)nextplayer);
					amountrespawned += 1;
				}
				
				String s = "";
				if (amountrespawned > 1) {
					s = "s";
				}
				
				StringFunctions.sendMessage(source, "Successfully made " + amountrespawned + " player" + s + " respawn.", ChatFormatting.DARK_GREEN);
				return 1;
			})
		);
    }
}
