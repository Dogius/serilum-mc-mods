/*
 * This is the latest source code of Eroding Stone Entities.
 * Minecraft version: 1.19.3, mod version: 3.1.
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

package com.natamus.erodingstoneentities.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.natamus.erodingstoneentities.config.ConfigHandler;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class Util {
	private static List<Block> iceblocks = new ArrayList<Block>(Arrays.asList(Blocks.ICE, Blocks.BLUE_ICE, Blocks.FROSTED_ICE, Blocks.PACKED_ICE));
	
	public static HashMap<Item, Item> erodeinto = new HashMap<Item, Item>();
	
	public static boolean populateArrays() {
		String tosandstring = ConfigHandler.itemsWhichErodeIntoSand;
		String toredsandstring = ConfigHandler.itemsWhichErodeIntoRedSand;
		String toclaystring = ConfigHandler.itemsWhichErodeIntoClay;
		
		for (String itemstring : tosandstring.split(",")) {
			ResourceLocation rl = new ResourceLocation(itemstring.trim());
			if (!BuiltInRegistries.ITEM.keySet().contains(rl)) {
				System.out.println("[Eroding Stone Entities] Unable to find item for input '" + itemstring.trim() + "' to erode into sand in the Forge item registry. Ignoring it.");
				continue;
			}
			
			Item item = BuiltInRegistries.ITEM.get(rl);
			erodeinto.put(item, Items.SAND);
		}
		
		for (String itemstring : toredsandstring.split(",")) {
			ResourceLocation rl = new ResourceLocation(itemstring.trim());
			if (!BuiltInRegistries.ITEM.keySet().contains(rl)) {
				System.out.println("[Eroding Stone Entities] Unable to find item for input '" + itemstring.trim() + "' to erode into red sand in the Forge item registry. Ignoring it.");
				continue;
			}
			
			Item item = BuiltInRegistries.ITEM.get(rl);
			erodeinto.put(item, Items.RED_SAND);
		}
		
		for (String itemstring : toclaystring.split(",")) {
			ResourceLocation rl = new ResourceLocation(itemstring.trim());
			if (!BuiltInRegistries.ITEM.keySet().contains(rl)) {
				System.out.println("[Eroding Stone Entities] Unable to find item for input '" + itemstring.trim() + "' to erode into clay in the Forge item registry. Ignoring it.");
				continue;
			}
			
			Item item = BuiltInRegistries.ITEM.get(rl);
			if (ConfigHandler.erodeIntoClayBlockInsteadOfClayBall) {
				erodeinto.put(item, Items.CLAY);
			}
			else {
				erodeinto.put(item, Items.CLAY_BALL);
			}
		}
		
		if (erodeinto.size() == 0) {
			System.out.println("[Eroding Stone Entities] The erode into hashmap is empty. Something went wrong while parsing your config file. Mod disabled.");
			return false;
		}
		return true;
	}
	
	public static void transformItemEntity(Level world, ItemEntity ie) {
		ItemStack stack = ie.getItem();
		if (stack == null) {
			return;
		}
		
		Item item = stack.getItem();
		if (item == null) {
			return;
		}
		
		if (!erodeinto.containsKey(item)) {
			return;
		}
		
		Item toitem = erodeinto.get(item);
		
		ItemEntity newie = new ItemEntity(world, ie.getX(), ie.getY(), ie.getZ(), new ItemStack(toitem, stack.getCount()));
		ie.remove(RemovalReason.DISCARDED);
		world.addFreshEntity(newie);
	}
	
	public static boolean isIceBlock(Block block) {
		if (iceblocks.contains(block)) {
			return true;
		}
		return false;
	}
}
