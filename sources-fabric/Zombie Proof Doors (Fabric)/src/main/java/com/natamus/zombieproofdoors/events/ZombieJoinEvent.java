/*
 * This is the latest source code of Zombie Proof Doors.
 * Minecraft version: 1.19.3, mod version: 2.1.
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

package com.natamus.zombieproofdoors.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class ZombieJoinEvent {
	public static void onEntityJoin(Level world, Entity entity) {
		if (!(entity instanceof Zombie)) {
			return;
		}
		
		Zombie zombie = (Zombie)entity;
		zombie.setCanBreakDoors(false);
	}
}
