/*
 * This is the latest source code of Hoe Tweaks.
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

package com.natamus.hoetweaks;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.hoetweaks.config.ConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
import com.natamus.hoetweaks.events.HoeEvent;
import com.natamus.hoetweaks.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return HoeEvent.onHoeRightClickBlock(player, world, hand, hitResult);
		});
		
		CollectivePlayerEvents.ON_PLAYER_DIG_SPEED_CALC.register((Level world, Player player, float digSpeed, BlockState state) -> {
			return HoeEvent.onHarvestBreakSpeed(world, player, digSpeed, state);
		});
	}
}
