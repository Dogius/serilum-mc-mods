/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.inventorytotem.events;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class InventoryTotemTotemEvent {
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)entity;
		if (player.getMainHandItem().getItem().equals(Items.TOTEM_OF_UNDYING) || player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)) {
			return;
		}
		
		PlayerInventory inv = player.inventory;
		if (inv == null) {
			return;
		}
		
		ItemStack totemstack = null;
		for(int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.getItem().equals(Items.TOTEM_OF_UNDYING)) {
				totemstack = stack;
				break;
			}
		}
		
		if (totemstack == null) {
			return;
		}
		
		e.setCanceled(true);
		if (player instanceof ServerPlayerEntity) {
			ServerPlayerEntity entityplayermp = (ServerPlayerEntity)player;
            entityplayermp.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
            CriteriaTriggers.USED_TOTEM.trigger(entityplayermp, totemstack);
        }

        player.setHealth(1.0F);
        player.removeAllEffects();
        player.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
        player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
        world.broadcastEntityEvent(player, (byte)35);
        totemstack.shrink(1);
	}
}
