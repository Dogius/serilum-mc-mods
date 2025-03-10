/*
 * This is the latest source code of GUI Followers.
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

package com.natamus.guifollowers.events;

import java.util.ArrayList;
import java.util.List;

import com.natamus.guifollowers.config.ConfigHandler;
import com.natamus.guifollowers.util.Variables;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class FollowerEvent {
	private final Minecraft mc = Minecraft.getInstance();

	@SubscribeEvent
	public void onPlayerTick(ClientTickEvent e) {
		if (!e.phase.equals(Phase.START)) {
			return;
		}
		
		Player player = mc.player;
		if (player == null) {
			return;
		}
		
		if (player.tickCount % (20* ConfigHandler.GENERAL.timeBetweenChecksInSeconds.get()) != 0) {
			return;
		}
		
		Level world = player.getCommandSenderWorld();
		if (world == null) {
			return;
		}

		int dc = ConfigHandler.GENERAL.distanceToCheckForFollowersAround.get(); // distancecheck
		if (dc <= 0) {
			return;
		}
		
		Vec3 pvec = player.position();
		List<Entity> entitiesaround = world.getEntities(player, new AABB(pvec.x-dc, pvec.y-dc, pvec.z-dc, pvec.x+dc, pvec.y+dc, pvec.z+dc));
		for (Entity ea : entitiesaround) {
			if (!(ea instanceof TamableAnimal)) {
				continue;
			}
			
			TamableAnimal te = (TamableAnimal)ea;
			if (!te.isTame()) {
				continue;
			}
			
			if (!te.isOwnedBy(player)) {
				continue;
			}
			
			if (te.isInSittingPose()) {
				continue;
			}

			boolean exists = false;
			for (Entity entity : Variables.activefollowers) {
				if (entity.getUUID().equals(ea.getUUID())) {
					exists = true;
					break;
				}
			}

			if (!exists) {
				Variables.activefollowers.add(ea);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent e) {
		Variables.activefollowers = new ArrayList<Entity>();
	}

	@SubscribeEvent
	public void onKey(InputEvent.Key e) {
		if (e.getAction() != 1) {
			return;
		}

		if (Variables.clearlist_hotkey != null && e.getKey() == Variables.clearlist_hotkey.getKey().getValue()) {
			Variables.activefollowers = new ArrayList<Entity>();
		}
	}
}
