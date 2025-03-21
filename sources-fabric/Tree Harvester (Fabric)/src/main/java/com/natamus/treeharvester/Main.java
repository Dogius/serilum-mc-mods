/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.19.3, mod version: 6.0.
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

package com.natamus.treeharvester;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.treeharvester.config.ConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
import com.natamus.treeharvester.events.TreeEvent;
import com.natamus.treeharvester.util.Reference;
import com.natamus.treeharvester.util.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Main implements ModInitializer {
	private boolean setupBlacklistRan = false;

	@Override
	public void onInitialize() {
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			if (setupBlacklistRan) {
				return;
			}
			setupBlacklistRan = true;

			try {
				Util.setupAxeBlacklist();
			}
			catch(Exception ex) {
				System.out.println("[Tree Harvester] Something went wrong setting up the axe blacklist file.");
			}
		});

		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			TreeEvent.onWorldLoad(world);
		});

		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			TreeEvent.onWorldTick(world);
		});

		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			return TreeEvent.onTreeHarvest(world, player, pos, state, entity);
		});

		CollectivePlayerEvents.ON_PLAYER_DIG_SPEED_CALC.register((Level world, Player player, float digSpeed, BlockState state) -> {
			return TreeEvent.onHarvestBreakSpeed(world, player, digSpeed, state);
		});
	}
}
