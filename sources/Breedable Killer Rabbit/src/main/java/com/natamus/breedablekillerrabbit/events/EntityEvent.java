/*
 * This is the latest source code of Breedable Killer Rabbit.
 * Minecraft version: 1.19.3, mod version: 1.9.
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

package com.natamus.breedablekillerrabbit.events;

import com.natamus.breedablekillerrabbit.config.ConfigHandler;
import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.StringFunctions;

import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	@SubscribeEvent
	public void onBaby(BabyEntitySpawnEvent e) {
		AgeableMob child = e.getChild();
		Level world = child.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (!(child instanceof Rabbit)) {
			return;
		}
		Rabbit rabbit = (Rabbit)child;
		
		double num = GlobalVariables.random.nextDouble();
		if (num <= ConfigHandler.GENERAL.chanceBabyRabbitIsKiller.get()) {
			rabbit.setVariant(Rabbit.Variant.EVIL);
			if (ConfigHandler.GENERAL.removeKillerRabbitNameTag.get()) {
				rabbit.setCustomName(null);
			}
			
			Player player = e.getCausedByPlayer();
			if (player == null) {
				return;
			}
			StringFunctions.sendMessage(player, "A killer rabbit has been born! Are you far enough away or do you have a golden carrot to share?", ChatFormatting.DARK_GREEN);
		}
	}
	
	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getTarget();
		if (!(entity instanceof Rabbit)) {
			return;
		}
		
		Player player = e.getEntity();
		ItemStack itemstack = e.getItemStack();
		
		if (!itemstack.getItem().equals(Items.GOLDEN_CARROT)) {
			return;
		}
		
		Rabbit rabbit = (Rabbit)entity;
		if (!rabbit.getVariant().equals(Rabbit.Variant.EVIL)) {
			return;
		}
		if (rabbit.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.GOLDEN_CARROT)) {
			StringFunctions.sendMessage(player, "The killer rabbit has already been tamed.", ChatFormatting.DARK_GREEN);
			return;
		}
		
		rabbit.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GOLDEN_CARROT, 1));
		itemstack.shrink(1);
		StringFunctions.sendMessage(player, "The killer rabbit has been tamed!", ChatFormatting.DARK_GREEN);
	}
	
	@SubscribeEvent
	public void onTarget(LivingAttackEvent e) {
		Entity source = e.getSource().getDirectEntity();
		if (source == null) {
			return;
		}
		
		Level world = source.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (!(source instanceof Rabbit)) {
			return;
		}
		
		if (((Rabbit)source).getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.GOLDEN_CARROT)) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void mobSpawn(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (!(entity instanceof Rabbit)) {
			return;
		}
		if (!ConfigHandler.GENERAL.removeKillerRabbitNameTag.get()) {
			return;
		}
		if (!((Rabbit)entity).getVariant().equals(Rabbit.Variant.EVIL)) {
			return;
		}
		if (!entity.hasCustomName()) {
			return;
		}
		
		if (entity.getCustomName().getString().equals("The Killer Bunny")) {
			entity.setCustomName(null);
		}
	}
	
	@SubscribeEvent
	public void onPlayerDamage(LivingHurtEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (!(entity instanceof Player)) {
			return;
		}
		
		Entity source = e.getSource().getDirectEntity();
		if (source == null) {
			return;
		}
		if (!(source instanceof Rabbit)) {
			return;
		}
		
		if (((Rabbit)source).getVariant().equals(Rabbit.Variant.EVIL)) {
			StringFunctions.sendMessage((Player)e.getEntity(), "The killer rabbit wants a golden carrot!", ChatFormatting.RED);
		}
	}
}
