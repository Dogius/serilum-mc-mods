/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.45.
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

package com.natamus.collective.functions;

import com.natamus.collective.config.ConfigHandler;
import com.natamus.collective.data.GlobalVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ItemFunctions {
	public static void generateEntityDropsFromLootTable(Level world) {
		MinecraftServer server = world.getServer();
		if (server == null) {
			return;
		}
		
		GlobalVariables.entitydrops = new HashMap<EntityType<?>, List<Item>>();
		
		FakePlayer fakeplayer = FakePlayerFactory.getMinecraft((ServerLevel)world);
		Vec3 vec = new Vec3(0, 0, 0);
		
		ItemStack lootingsword = new ItemStack(Items.DIAMOND_SWORD, 1);
		lootingsword.enchant(Enchantments.MOB_LOOTING, 10);
		fakeplayer.setItemSlot(EquipmentSlot.MAINHAND, lootingsword);
		
		Collection<EntityType<?>> entitytypes = ForgeRegistries.ENTITY_TYPES.getValues();
		for (EntityType<?> type : entitytypes) {
			if (type == null) {
				continue;
			}
			Entity entity = type.create(world);
			if (!(entity instanceof LivingEntity)) {
				continue;
			}
			
			LivingEntity le = (LivingEntity)entity;
			ResourceLocation lootlocation = le.getType().getDefaultLootTable();
			
			LootTable loottable = server.getLootTables().get(lootlocation);
			LootContext context = new LootContext.Builder((ServerLevel) world)
	                .withRandom(world.getRandom())
	                .withLuck(1000000F)
	                .withParameter(LootContextParams.THIS_ENTITY, entity)
	                .withParameter(LootContextParams.ORIGIN, vec)
	                .withParameter(LootContextParams.KILLER_ENTITY, fakeplayer)
	                .withParameter(LootContextParams.DAMAGE_SOURCE, DamageSource.playerAttack(fakeplayer))
	                .create(LootContextParamSets.ENTITY);
			
			List<Item> alldrops = new ArrayList<Item>();
			for (int n = 0; n < ConfigHandler.COLLECTIVE.loopsAmountUsedToGetAllEntityDrops.get(); n++) {
				List<ItemStack> newdrops = loottable.getRandomItems(context);
				for (ItemStack newdrop : newdrops) {
					Item newitem = newdrop.getItem();
					if (!alldrops.contains(newitem) && !newitem.equals(Items.AIR)) {
						alldrops.add(newitem);
					}
				}
			}
			
			GlobalVariables.entitydrops.put(type, alldrops);
		}
	}
	
	public static Boolean isTool(ItemStack itemstack) {
		return ToolFunctions.isTool(itemstack);
	}
	
	public static void shrinkGiveOrDropItemStack(Player player, InteractionHand hand, ItemStack used, ItemStack give) {
		used.shrink(1);
		
		if (used.isEmpty()) {
			Item giveitem = give.getItem();
			int maxstacksize = give.getMaxStackSize();
			List<ItemStack> inventory = player.getInventory().items;
			
			boolean increased = false;
			for (ItemStack slot : inventory) {
				if (slot.getItem().equals(giveitem)) {
					int slotcount = slot.getCount();
					if (slotcount < maxstacksize) {
						slot.setCount(slotcount + 1);
						increased = true;
						break;
					}
				}
			}
			
			if (!increased) {
				player.setItemInHand(hand, give);
			}
		}
		else if (!player.getInventory().add(give)) {
			player.drop(give, false);
		}
	}
	
	public static void giveOrDropItemStack(Player player, ItemStack give) {
		if (!player.getInventory().add(give)) {
			player.drop(give, false);
		}
	}

	public static void itemHurtBreakAndEvent(ItemStack itemStack, ServerPlayer player, InteractionHand hand, int damage) {
		if (!(player.getAbilities().instabuild)) {
			if (itemStack.isDamageableItem()) {
				damage = itemStack.getItem().damageItem(itemStack, damage, player, (player1 -> player1.broadcastBreakEvent(hand)));
				if (itemStack.hurt(damage, player.getRandom(), player)) {
					ForgeEventFactory.onPlayerDestroyItem(player, itemStack, hand);

					Item item = itemStack.getItem();
					itemStack.shrink(1);
					itemStack.setDamageValue(0);

					player.awardStat(Stats.ITEM_BROKEN.get(item));
				}
			}
		}
	}
	
	public static boolean isStoneTypeItem(Item item) {
		return GlobalVariables.stoneblockitems.contains(item);
	}
	
	public static String itemToReadableString(Item item, int amount) {
		String itemstring;
		String translationkey = item.getDescriptionId();
		if (translationkey.contains("block.")) {
			return BlockFunctions.blockToReadableString(Block.byItem(item), amount);
		}
		
		String[] itemspl = translationkey.replace("item.", "").split("\\.");
		if (itemspl.length > 1) {
			itemstring = itemspl[1];
		}
		else {
			itemstring = itemspl[0];
		}
		
		itemstring = itemstring.replace("_", " ");
		if (amount > 1) {
			itemstring = itemstring + "s";
		}
		
		return itemstring;
	}
	public static String itemToReadableString(Item item) {
		return itemToReadableString(item, 1);
	}

	public static String getNBTStringFromItemStack(ItemStack itemStack) {
		return getNBTStringFromItemStack(itemStack, true);
	}
	public static String getNBTStringFromItemStack(ItemStack itemStack, boolean shouldSanitize) {
		CompoundTag nbt = new CompoundTag();
		nbt = itemStack.save(nbt);

		String nbtstring = nbt.toString();
		if (shouldSanitize) {
			nbtstring = nbtstring.replace(" : ", ": ");
		}

		return nbtstring;
	}
}