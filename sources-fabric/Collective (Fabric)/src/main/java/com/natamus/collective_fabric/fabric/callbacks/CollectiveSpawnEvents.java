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

package com.natamus.collective_fabric.fabric.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

public class CollectiveSpawnEvents {
	private CollectiveSpawnEvents() { }
	 
    public static final Event<CollectiveSpawnEvents.Mob_Check_Spawn> MOB_CHECK_SPAWN = EventFactory.createArrayBacked(CollectiveSpawnEvents.Mob_Check_Spawn.class, callbacks -> (entity, world, spawnerPos, spawnReason) -> {
        for (CollectiveSpawnEvents.Mob_Check_Spawn callback : callbacks) {
        	if (!callback.onMobCheckSpawn(entity, world, spawnerPos, spawnReason)) {
        		return false;
        	}
        }
        
        return true;
    });
    
	@FunctionalInterface
	public interface Mob_Check_Spawn {
		 boolean onMobCheckSpawn(Mob entity, ServerLevel world, BlockPos spawnerPos, MobSpawnType spawnReason);
	}
}
