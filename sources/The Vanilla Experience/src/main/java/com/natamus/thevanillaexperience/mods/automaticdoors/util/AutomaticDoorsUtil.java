/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.automaticdoors.util;

import java.util.ArrayList;
import java.util.List;

import com.natamus.thevanillaexperience.mods.automaticdoors.config.AutomaticDoorsConfigHandler;
import com.natamus.thevanillaexperience.mods.automaticdoors.events.AutomaticDoorsDoorEvent;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.core.BlockPos;

public class AutomaticDoorsUtil {
	private static List<BlockPos> runnables = new ArrayList<BlockPos>();
	
	public static Boolean isDoor(Block block) {
		if (block instanceof DoorBlock) {
			if (!AutomaticDoorsConfigHandler.GENERAL.shouldOpenIronDoors.get()) {
				String name = block.toString().toLowerCase();
				if (name.contains("iron")) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static void delayDoorClose(BlockPos pos) {
		if (pos == null) {
			return;
		}
		
		if (runnables.contains(pos)) {
			return;
		}
		
		runnables.add(pos);
		new Thread( new Runnable() {
	    	public void run()  {
	        	try  { Thread.sleep( AutomaticDoorsConfigHandler.GENERAL.doorOpenTime.get() ); }
	            catch (InterruptedException ie)  {}
	        	
	        	if (!AutomaticDoorsDoorEvent.toclosedoors.contains(pos) && !AutomaticDoorsDoorEvent.newclosedoors.contains(pos)) {
	        		AutomaticDoorsDoorEvent.newclosedoors.add(pos);
	        	}
	        	runnables.remove(pos);
	    	}
	    } ).start();
	}
}
