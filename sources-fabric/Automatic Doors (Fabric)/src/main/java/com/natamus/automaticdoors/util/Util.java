/*
 * This is the latest source code of Automatic Doors.
 * Minecraft version: 1.19.3, mod version: 3.0.
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

package com.natamus.automaticdoors.util;

import java.util.ArrayList;
import java.util.List;

import com.natamus.automaticdoors.config.ConfigHandler;
import com.natamus.automaticdoors.events.DoorEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.core.BlockPos;

public class Util {
	private static List<BlockPos> runnables = new ArrayList<BlockPos>();
	
	public static Boolean isDoor(Block block) {
		if (block instanceof DoorBlock) {
			if (!ConfigHandler.shouldOpenIronDoors) {
				String name = block.toString().toLowerCase();
				return !name.contains("iron");
			}
			return true;
		}
		return false;
	}
	
	public static void delayDoorClose(Level world, BlockPos pos) {
		if (pos == null) {
			return;
		}
		
		if (runnables.contains(pos)) {
			return;
		}
		
		runnables.add(pos);
		new Thread(() -> {
			try  { Thread.sleep( ConfigHandler.doorOpenTime ); }
			catch (InterruptedException ignored)  {}

			if (!DoorEvent.toclosedoors.get(world).contains(pos) && !DoorEvent.newclosedoors.get(world).contains(pos)) {
				DoorEvent.newclosedoors.get(world).add(pos);
			}
			runnables.remove(pos);
		}).start();
	}
}
