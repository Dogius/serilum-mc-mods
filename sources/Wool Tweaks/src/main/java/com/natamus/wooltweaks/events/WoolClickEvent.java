/*
 * This is the latest source code of Wool Tweaks.
 * Minecraft version: 1.19.3, mod version: 2.2.
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

package com.natamus.wooltweaks.events;

import com.natamus.wooltweaks.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class WoolClickEvent {
	@SuppressWarnings( "deprecation" )
	@SubscribeEvent
	public void onWoolClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack handstack = e.getItemStack();
		Item handitem = handstack.getItem();
		if (!(handitem instanceof DyeItem)) {
			return;
		}
		
		BlockPos target = e.getPos();
		BlockState state = world.getBlockState(target);
		Block block = state.getBlock();
		
		Block newblock;
		if (block.builtInRegistryHolder().is(BlockTags.WOOL)) {
			newblock = Util.woolblocks.get(handitem);
		}
		else if (block instanceof BedBlock) {
			newblock = Util.bedblocks.get(handitem);
		}
		else if (block instanceof WoolCarpetBlock) {
			newblock = Util.carpetblocks.get(handitem);
		}
		else {
			return;
		}
		
		if (newblock == null) {
			return;
		}
		
		if (block.equals(newblock)) {
			return;
		}

		Player player = e.getEntity();
		
		e.setCanceled(true);
		
		BlockState newstate = newblock.defaultBlockState();
		if (block instanceof BedBlock) {
			Direction direction = state.getValue(BedBlock.FACING);
			newstate = newstate.setValue(BedBlock.FACING, direction);
			newstate = newstate.setValue(BedBlock.OCCUPIED, state.getValue(BedBlock.OCCUPIED));
			
			BedPart bedpart = state.getValue(BedBlock.PART);
			newstate = newstate.setValue(BedBlock.PART, bedpart);
			
			BlockPos othertarget;
			BedPart otherpart;
			if (bedpart.equals(BedPart.HEAD)) {
				otherpart = BedPart.FOOT;
				othertarget = target.relative(direction.getOpposite());
				
				world.setBlockAndUpdate(target, Blocks.AIR.defaultBlockState());
				world.setBlockAndUpdate(othertarget, Blocks.AIR.defaultBlockState());
			}
			else {
				otherpart = BedPart.HEAD;
				othertarget = target.relative(direction);
				
				world.setBlockAndUpdate(othertarget, Blocks.AIR.defaultBlockState());
				world.setBlockAndUpdate(target, Blocks.AIR.defaultBlockState());
			}
			
			world.setBlockAndUpdate(othertarget, newstate.setValue(BedBlock.PART, otherpart));
		}
		
		world.setBlockAndUpdate(target, newstate);
		player.swing(e.getHand());

		if (!player.isCreative()) {
			handstack.shrink(1);
		}
	}
}