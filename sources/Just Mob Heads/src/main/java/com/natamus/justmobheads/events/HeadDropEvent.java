/*
 * This is the latest source code of Just Mob Heads.
 * Minecraft version: 1.17.1, mod version: 4.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Just Mob Heads ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.justmobheads.events;

import com.natamus.justmobheads.config.ConfigHandler;
import com.natamus.justmobheads.util.HeadData;
import com.natamus.justmobheads.util.MobHeads;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class HeadDropEvent {	
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (ConfigHandler.GENERAL.onlyAdultMobsDropTheirHead.get()) {
			if (entity instanceof TamableAnimal) {
				TamableAnimal te = (TamableAnimal)entity;
				if (te.isBaby()) {
					return;
				}
			}
		}
		
		String mobname = MobHeads.getName(entity);
		if (mobname == "") {
			return;
		}
		
		double extrachance = 0;
		if (ConfigHandler.GENERAL.enableLootingEnchant.get()) {
			Integer looting = e.getLootingLevel();
			if (looting > 0) {
				extrachance = 0.025 + (looting/100);
			}
		}
		
		String headname = "";
		if (mobname.equals("creeper") || mobname.equals("zombie") || mobname.equals("skeleton")) {
			if (ConfigHandler.GENERAL.enableStandardHeads.get()) {
				headname = mobname.substring(0, 1).toUpperCase() + mobname.substring(1) + " Head";
			}
			else {
				return;
			}
		}
		
		double num = Math.random();
		if (ConfigHandler.GENERAL.mobSpecificDropChances.get()) {
			double chance = -1;
        	if (headname.equals("")) {
        		if (HeadData.headchances.containsKey(mobname)) {
        			chance = HeadData.headchances.get(mobname);
        		}
        	}
        	else {
        		chance = ConfigHandler.GENERAL.creeperSkeletonZombieDropChance.get();
        	}
	        
	        if (chance == -1) {
	        	if (num > ConfigHandler.GENERAL.overallDropChance.get() + extrachance) {
	        		return;
	        	}
	        }
	        else if (num > chance + extrachance) {
	        	return;
	        }
		}
		else if (num > ConfigHandler.GENERAL.overallDropChance.get() + extrachance) {
			return;
		}
		
		BlockPos pos = entity.blockPosition();
		
		ItemEntity mobhead;
		if (headname.equals("")) {
			ItemStack headstack = MobHeads.getMobHead(mobname, 1);
			if (headstack == null) {
				return;
			}
			
			mobhead = new ItemEntity(world, pos.getX(), pos.getY()+1, pos.getZ(), headstack);
		}
		else {
			mobhead = new ItemEntity(world,pos.getX(), pos.getY()+1, pos.getZ(), MobHeads.getStandardHead(headname));
		}
		
		world.addFreshEntity(mobhead);
	}
}
