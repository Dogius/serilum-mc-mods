/*
 * This is the latest source code of Stack Refill.
 * Minecraft version: 1.19.3, mod version: 3.3.
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

package com.natamus.stackrefill;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;
import com.natamus.stackrefill.events.RefillEvent;
import com.natamus.stackrefill.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			RefillEvent.onWorldTick(world);
		});

		CollectiveItemEvents.ON_ITEM_USE_FINISHED.register((Player player, ItemStack usedItem, ItemStack newItem, InteractionHand hand) -> {
			return RefillEvent.onItemUse(player, usedItem, newItem, hand);
		});

		CollectiveItemEvents.ON_ITEM_DESTROYED.register((Player player, ItemStack stack, InteractionHand hand) -> {
			RefillEvent.onItemBreak(player, stack, hand);
		});

		CollectiveItemEvents.ON_ITEM_TOSSED.register((Player player, ItemStack itemStack) -> {
			RefillEvent.onItemToss(player, itemStack);
		});

		UseItemCallback.EVENT.register((Player player, Level world, InteractionHand hand) -> {
			return RefillEvent.onItemRightClick(player, world, hand);
		});

		CollectiveBlockEvents.BLOCK_RIGHT_CLICK.register((Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) -> {
			RefillEvent.onBlockRightClick(world, player, hand, pos, hitVec);
			return true;
		});
	}
}
