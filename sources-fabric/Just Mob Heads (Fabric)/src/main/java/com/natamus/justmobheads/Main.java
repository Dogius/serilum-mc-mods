/*
 * This is the latest source code of Just Mob Heads.
 * Minecraft version: 1.19.3, mod version: 6.3.
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

package com.natamus.justmobheads;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.justmobheads.cmds.CommandJmh;
import com.natamus.justmobheads.config.ConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
import com.natamus.justmobheads.events.HeadDropEvent;
import com.natamus.justmobheads.util.HeadData;
import com.natamus.justmobheads.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
 		HeadData.init();
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveEntityEvents.ON_ENTITY_IS_DROPPING_LOOT.register((Level world, Entity entity, DamageSource damageSource) -> {
			HeadDropEvent.mobItemDrop(world, entity, damageSource);
		});
		
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			return HeadDropEvent.onPlayerHeadBreak(world, player, pos, state, entity);
		});
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CommandJmh.register(dispatcher);
		});
	}
}
