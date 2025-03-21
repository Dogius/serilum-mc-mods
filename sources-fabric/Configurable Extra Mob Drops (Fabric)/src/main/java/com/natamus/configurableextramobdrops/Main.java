/*
 * This is the latest source code of Configurable Extra Mob Drops.
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

package com.natamus.configurableextramobdrops;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.configurableextramobdrops.cmd.CommandCemd;
import com.natamus.configurableextramobdrops.events.MobDropEvent;
import com.natamus.configurableextramobdrops.util.Reference;
import com.natamus.configurableextramobdrops.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
    	try {
			Util.loadMobConfigFile();
		} catch (Exception ex) {
			System.out.println("[Error] Configurable Extra Mob Drops error on loading the entity config file. The mod has been disabled.");
			ex.printStackTrace();
			return;
		}
    	
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CommandCemd.register(dispatcher);
		});
		
		CollectiveEntityEvents.ON_ENTITY_IS_DROPPING_LOOT.register((Level world, Entity entity, DamageSource damageSource) -> {
			MobDropEvent.mobItemDrop(world, entity, damageSource);
		});
	}
}
