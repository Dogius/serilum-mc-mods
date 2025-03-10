/*
 * This is the latest source code of Just Player Heads.
 * Minecraft version: 1.19.3, mod version: 2.6.
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

package com.natamus.justplayerheads.cmds;

import java.util.HashMap;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective_fabric.functions.HeadFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.justplayerheads.config.ConfigHandler;
import com.natamus.justplayerheads.util.Variables;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;

public class CommandJph {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("jph")
    			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player && iCommandSender.hasPermission(2))
    			.then(Commands.argument("name", StringArgumentType.word())
    			.then(Commands.argument("amount", IntegerArgumentType.integer(1, 64))
    			.executes((command) -> {
		    		Integer amount = 1;
	    			Integer specifiedamount = IntegerArgumentType.getInteger(command, "amount");
	    			if (specifiedamount > 1 && specifiedamount <= 64) {
	    				amount = specifiedamount;		    			
	    			}
		    		
		    		processJph(command, amount);
		    		return 1;
		        })))
    			.then(Commands.argument("name", StringArgumentType.word())
    			.executes((command) -> {
    				processJph(command, 1);
    				return 1;
    			}))
    			.then(Commands.literal("cache")
    			.executes((command) -> {
    				CommandSourceStack source = command.getSource();
    				
    				Variables.headcache = new HashMap<String, ItemStack>();
    				
    				StringFunctions.sendMessage(source, "The player head texture cache has been emptied.", ChatFormatting.DARK_GREEN);
    				return 1;
    			}))
    			.executes((command) -> {
    				CommandSourceStack source = command.getSource();

    				StringFunctions.sendMessage(source, "Allows you to get the head of a player.", ChatFormatting.DARK_GREEN);
    				StringFunctions.sendMessage(source, " Usage: /jph playerName (amount)", ChatFormatting.DARK_GREEN);
    				return 1;
    			})
    		);
    }
    
    public static void processJph(CommandContext<CommandSourceStack> command, Integer amount) throws CommandSyntaxException {
    	CommandSourceStack source = command.getSource();
		
		String target = StringArgumentType.getString(command, "name");
		
		ItemStack head = null;
		if (ConfigHandler.enablePlayerHeadCaching) {
			if (Variables.headcache.containsKey(target.toLowerCase())) {
				head = Variables.headcache.get(target.toLowerCase());
				head.setCount(amount);
			}
		}
		
		if (head == null) {
			head = HeadFunctions.getPlayerHead(target, amount);
			
			if (head != null && ConfigHandler.enablePlayerHeadCaching) {
				ItemStack cachehead = head.copy();
				cachehead.setCount(1);
				
				Variables.headcache.put(target.toLowerCase(), cachehead);
			}
		}
		
		String message = "";
		if (head == null) {
			message = "Unable to generate the player head. Either the player '" + target + "' does not exist or the Mojang API server has had too many requests in a short period of time.";
			StringFunctions.sendMessage(source, message, ChatFormatting.DARK_RED);
		}
		else {
			message = "Succesfully generated the head of the player '" + target + "'.";
			StringFunctions.sendMessage(source, message, ChatFormatting.DARK_GREEN);
			
			Player player = source.getPlayerOrException();
			player.spawnAtLocation(head, 1);
		}
    }
}