/*
 * This is the latest source code of Mineral Chance.
 * Minecraft version: 1.19.3, mod version: 2.3.
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

package com.natamus.mineralchance.events;

import com.natamus.collective_fabric.data.GlobalVariables;
import com.natamus.collective_fabric.fakeplayer.FakePlayer;
import com.natamus.collective_fabric.functions.CompareBlockFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.collective_fabric.functions.WorldFunctions;
import com.natamus.mineralchance.config.ConfigHandler;
import com.natamus.mineralchance.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MiningEvent {
	public static void onBlockBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (world.isClientSide) {
			return;
		}
		
		if (ConfigHandler.ignoreFakePlayers) {
			if (player instanceof FakePlayer) {
				return;
			}
		}
		
		if (player.isCreative()) {
			return;
		}
		
		Block block = state.getBlock();
		if (!CompareBlockFunctions.isStoneBlock(block) && !CompareBlockFunctions.isNetherStoneBlock(block)) {
			return;
		}
		
		Item randommineral;
		if (WorldFunctions.isOverworld(world)) {
			if (!ConfigHandler.enableOverworldMinerals) {
				return;
			}
			if (CompareBlockFunctions.isNetherStoneBlock(block)) {
				return;
			}
			if (GlobalVariables.random.nextDouble() > ConfigHandler.extraMineralChanceOnOverworldStoneBreak) {
				return;
			}
			randommineral = Util.getRandomOverworldMineral();
		}
		else if (WorldFunctions.isNether(world)) {
			if (!ConfigHandler.enableNetherMinerals) {
				return;
			}
			if (!CompareBlockFunctions.isNetherStoneBlock(block)) {
				return;
			}
			if (GlobalVariables.random.nextDouble() > ConfigHandler.extraMineralChanceOnNetherStoneBreak) {
				return;
			}
			randommineral = Util.getRandomNetherMineral();
		}
		else {
			return;
		}
		
		ItemEntity mineralentity = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, new ItemStack(randommineral, 1));
		world.addFreshEntity(mineralentity);
		
		if (ConfigHandler.sendMessageOnMineralFind) {
			StringFunctions.sendMessage(player, ConfigHandler.foundMineralMessage, ChatFormatting.DARK_GREEN);
		}
	}
}
