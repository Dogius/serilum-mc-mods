/*
 * This is the latest source code of Configurable Furnace Burn Time.
 * Minecraft version: 1.19.3, mod version: 1.3.
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

package com.natamus.configurablefurnaceburntime.events;

import com.natamus.configurablefurnaceburntime.config.ConfigHandler;

import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FurnaceBurnEvent {
	@SubscribeEvent
	public void furnaceBurnTimeEvent(FurnaceFuelBurnTimeEvent e) {
		int burntime = e.getBurnTime();
		int newburntime = (int)Math.ceil(burntime * ConfigHandler.GENERAL.burnTimeModifier.get());
		
		e.setBurnTime(newburntime);
	}
}
