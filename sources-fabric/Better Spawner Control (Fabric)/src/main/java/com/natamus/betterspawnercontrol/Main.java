/*
 * This is the latest source code of Better Spawner Control.
 * Minecraft version: 1.19.3, mod version: 3.2.
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

package com.natamus.betterspawnercontrol;

import com.natamus.betterspawnercontrol.events.MobSpawnEvent;
import com.natamus.betterspawnercontrol.util.Reference;
import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveSpawnEvents;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveSpawnEvents.MOB_CHECK_SPAWN.register((Mob entity, ServerLevel world, BlockPos spawnerPos, MobSpawnType spawnReason) -> {
			return MobSpawnEvent.onMobSpawn(entity, world, spawnerPos, spawnReason);
		});
	}
}
