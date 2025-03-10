/*
 * This is the latest source code of Superflat World No Slimes.
 * Minecraft version: 1.19.3, mod version: 2.3.
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

package com.natamus.superflatworldnoslimes.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class SlimeEvent {
	public static boolean onWorldJoin(Level world, Entity entity) {
		if (world.isClientSide) {
			return true;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		
		if (entity instanceof Slime) {
			if (serverworld.getServer().getWorldData().isFlatWorld()) {
				return false;
			}
		}
		
		return true;
	}
}
