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

package com.natamus.thevanillaexperience.mods.treeharvester.events;

import java.util.Date;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class TreeHarvesterSoundHarvestEvent {
	public static Date lastplayedlog = null;
	public static Date lastplayedleaf = null;
	
	@SubscribeEvent
	public void onSoundEvent(PlaySoundEvent e) {
		String name = e.getName().trim();
		
		if (name.equals("block.grass.break") || name.equals("block.wood.break")) {
			Date now = new Date();
			Date then = null;
			
			if (name.equals("block.grass.break")) {
				then = lastplayedleaf;
				lastplayedleaf = now;
			}
			else {
				then = lastplayedlog;
				lastplayedlog = now;
			}
			
			if (then != null) {
				long ms = (now.getTime()-then.getTime());
				if (ms < 10) {
					e.setResultSound(null);
				}
			}
		}
	}
}
