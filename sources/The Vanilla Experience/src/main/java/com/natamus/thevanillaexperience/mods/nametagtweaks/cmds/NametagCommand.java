/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.nametagtweaks.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.nametagtweaks.config.NameTagTweaksConfigHandler;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;

public class NametagCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("nametag")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof ServerPlayer)
			.executes((command) -> {
				StringFunctions.sendMessage(command.getSource(), "Usage: '/nametag <name>' while holding a name tag.", ChatFormatting.DARK_GREEN);
				return 1;
			})
			.then(Commands.argument("name", StringArgumentType.word())
			.executes((command) -> {
				Player player = command.getSource().getPlayerOrException();
				
				ItemStack nametagstack;
				if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.NAME_TAG)) {
					nametagstack = player.getItemInHand(InteractionHand.MAIN_HAND);
				}
				else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem().equals(Items.NAME_TAG)) {
					nametagstack = player.getItemInHand(InteractionHand.OFF_HAND);
				}
				else {
					StringFunctions.sendMessage(player, "Usage: '/nametag <name>' while holding a name tag.", ChatFormatting.RED);
					return 1;
				}
				
				String name = StringArgumentType.getString(command, "name");
				if (NameTagTweaksConfigHandler.GENERAL.nameTagCommandReplaceUnderscoresWithSpaces.get()) {
					name = name.replace("_", " ");
				}
				
				nametagstack.setHoverName(new TextComponent(name));
				nametagstack.setRepairCost(0);
				StringFunctions.sendMessage(player, "Set name value to '" + name + "'.", ChatFormatting.DARK_GREEN);
				return 1;
			}))
		);
    }
}
