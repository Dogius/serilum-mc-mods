/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.19.3, mod version: 3.3.
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

package com.natamus.respawndelay.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.respawndelay.config.ConfigHandler;
import com.natamus.respawndelay.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RespawningEvent {
	public static HashMap<Player, Date> death_times = new HashMap<Player, Date>();
	
	public static void onPlayerTick(ServerLevel world, ServerPlayer player) {
		if (player.tickCount % 20 != 0) {
			return;
		}
		
		if (!player.isSpectator()) {
			return;
		}
		
		if (!death_times.containsKey(player)) {
			return;
		}
		
		int respawndelay = ConfigHandler.respawnDelayInSeconds;
		if (respawndelay < 0) {
			return;
		}
		
		Date now = new Date();
		Date timedied = death_times.get(player); 
		
		long ms = (now.getTime()-timedied.getTime());
		if (ms > respawndelay*1000) {
			Util.respawnPlayer(world, (ServerPlayer)player);
			return;
		}
		
		int seconds = respawndelay - (int)(ms/1000);
		String waitingmessage = ConfigHandler.waitingForRespawnMessage;
		waitingmessage = waitingmessage.replaceAll("<seconds_left>", seconds + "");
		
		StringFunctions.sendMessage(player, waitingmessage, ChatFormatting.GRAY);
	}
	
	public static boolean onPlayerDeath(ServerPlayer player, DamageSource damageSource, float damageAmount) {
		if (player instanceof ServerPlayer == false) {
			return true;
		}
		
		if (ConfigHandler.ignoreAdministratorPlayers) {
			if (player.hasPermissions(2)) {
				return true;
			}
		}
		
		if (ConfigHandler.ignoreCreativePlayers) {
			if (player.isCreative()) {
				return true;
			}
		}
		
		ServerPlayer serverplayer = (ServerPlayer)player;
		ServerLevel world = (ServerLevel)serverplayer.level;
		
		serverplayer.setGameMode(GameType.SPECTATOR);
		player.setHealth(20);
		player.awardStat(Stats.DEATHS);
		player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
		player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
		player.clearFire();
		
		Vec3 pvec = player.position();
		if (pvec.y() < ConfigHandler.lowestPossibleYCoordinate) {
			pvec = new Vec3(pvec.x(), ConfigHandler.lowestPossibleYCoordinate, pvec.z());
			player.setDeltaMovement(0, 0, 0);
			player.teleportTo(pvec.x(), pvec.y(), pvec.z());
		}
		
		if (!ConfigHandler.keepItemsOnDeath) {
			Collection<ItemEntity> playerdrops = new ArrayList<ItemEntity>();
			
			Inventory inv = player.getInventory();
			for (int i=0; i < 36; i++) {
				ItemStack slot = inv.getItem(i);
				if (!slot.isEmpty()) {
					playerdrops.add(new ItemEntity(world, pvec.x, pvec.y+1, pvec.z, slot.copy()));
					slot.setCount(0);
				}
			}
			
			Iterator<ItemStack> it = player.getAllSlots().iterator();
			while (it.hasNext()) {
				ItemStack next = it.next();
				if (!next.isEmpty()) {
					playerdrops.add(new ItemEntity(world, pvec.x, pvec.y+1, pvec.z, next.copy()));
					next.setCount(0);
				}
			}
			
			//ForgeHooks.onLivingDrops((LivingEntity)player, e.getSource(), playerdrops, 0, true);
			
			if (ConfigHandler.dropItemsOnDeath) {
				for (ItemEntity drop : playerdrops) {
					world.addFreshEntity(drop);
				}
			}
		}
		
		Date now = new Date();
		death_times.put(player, now);
		
		StringFunctions.sendMessage(player, ConfigHandler.onDeathMessage, ChatFormatting.DARK_RED);
		return false;
	}
	
	public static void onPlayerLogout(Level world, Player player) {
		if (world.isClientSide) {
			return;
		}
		
		if (player instanceof ServerPlayer == false) {
			return;
		}
		
		if (death_times.containsKey(player)) {
			Util.respawnPlayer(world, (ServerPlayer)player);
		}
	}
	
	public static void onPlayerLogin(Level world, Player player) {
		if (world.isClientSide) {
			return;
		}
		
		if (player instanceof ServerPlayer == false) {
			return;
		}
		
		if (player.isSpectator() && !death_times.containsKey(player)) {
			Util.respawnPlayer(world, (ServerPlayer)player);
		}
	}
}
