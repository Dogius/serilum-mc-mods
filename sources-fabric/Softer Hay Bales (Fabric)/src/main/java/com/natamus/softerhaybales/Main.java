/*
 * This is the latest source code of Softer Hay Bales.
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

package com.natamus.softerhaybales;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.softerhaybales.events.FallEvent;
import com.natamus.softerhaybales.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		/*     	MinecraftForge.EVENT_BUS.register(new FallEvent());
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			SheepEvent.onSheepSpawn(world, entity);
		});
		*/
		CollectiveEntityEvents.ON_FALL_DAMAGE_CALC.register((Level world, Entity entity, float f, float g) -> {
			return FallEvent.onFall(world, entity, f, g);
		});
	}
}
