/*
 * This is the latest source code of Campfire Spawn and Tweaks.
 * Minecraft version: 1.19.3, mod version: 1.8.
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

package com.natamus.campfirespawnandtweaks.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.campfirespawnandtweaks.config.ConfigHandler;
import com.natamus.campfirespawnandtweaks.events.CampfireEvent;
import com.natamus.collective.functions.DimensionFunctions;
import com.natamus.collective.functions.NumberFunctions;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Util {
	public static void loadCampfireSpawnsFromWorld(Level world) {
		String campfirespawnfolder = WorldFunctions.getWorldPath((ServerLevel)world) + File.separator + "data" + File.separator + "campfirespawn" + File.separator + DimensionFunctions.getSimpleDimensionString(world);
		File dir = new File(campfirespawnfolder);
		dir.mkdirs();
		
		File[] listOfFiles = dir.listFiles();
		if (listOfFiles == null) {
			return;
		}
		
		for (int i = 0; i < listOfFiles.length; i++) {
			String filename = listOfFiles[i].getName();
			if (filename.endsWith(".spawnpoint")) {
				String playername = filename.toLowerCase().replace(".spawnpoint", "");
				
				String filecontent;
				try {
					filecontent = new String(Files.readAllBytes(Paths.get(campfirespawnfolder + File.separator + filename, new String[0])));
				} catch (IOException e) {
					System.out.println("[Error] Campfire Spawn: Something went wrong while loading a campfire spawn location for player " + playername + ".");
					continue;
				}
				
				if (StringFunctions.sequenceCount(filecontent, "_") < 2) {
					continue;
				}
				
				String[] cs = filecontent.trim().split("_");
				if (cs.length == 3) {
					if (NumberFunctions.isNumeric(cs[0]) && NumberFunctions.isNumeric(cs[1]) && NumberFunctions.isNumeric(cs[2])) {
						int x = Integer.parseInt(cs[0]);
						int y = Integer.parseInt(cs[1]);
						int z = Integer.parseInt(cs[2]);
						
						BlockPos spawnpos = new BlockPos(x, y, z);
						CampfireEvent.playercampfires.put(playername, new Pair<Level, BlockPos>(world, spawnpos.immutable()));
					}
						
				}
			}
		}
	}
	
	public static boolean setCampfireSpawn(Level world, String playername, BlockPos campfirepos) {
		String campfirespawnfolder = WorldFunctions.getWorldPath((ServerLevel)world) + File.separator + "data" + File.separator + "campfirespawn" + File.separator + DimensionFunctions.getSimpleDimensionString(world);
		File dir = new File(campfirespawnfolder);
		dir.mkdirs();
		
		String filename = playername.toLowerCase() + ".spawnpoint";
		try {
			PrintWriter writer = new PrintWriter(campfirespawnfolder + File.separator + filename, "UTF-8");
			writer.println(campfirepos.getX() + "_" + campfirepos.getY() + "_" + campfirepos.getZ());
			writer.close();
		} catch (Exception e) {
			System.out.println("[Error] Campfire Spawn: Something went wrong while saving a campfire spawn location for player " + playername + ".");
			return false;
		}
		
		CampfireEvent.playercampfires.put(playername.toLowerCase(), new Pair<Level, BlockPos>(world, campfirepos.immutable()));
		return true;
	}
	
	public static boolean checkForCampfireSpawnRemoval(Level world, String playername, BlockPos campfirepos) {
		List<String> fromplayernames = new ArrayList<String>();
		for (String pname : CampfireEvent.playercampfires.keySet()) {
			Pair<Level, BlockPos> pair = CampfireEvent.playercampfires.get(pname);
			if (campfirepos.equals(pair.getSecond())) {
				fromplayernames.add(pname);
			}
		}
		
		boolean removed = false;
		for (String fromplayername : fromplayernames) {
			if (CampfireEvent.playercampfires.containsKey(fromplayername)) {
				for (Level loopworld : CampfireEvent.playerstorespawn.keySet()) {
					String campfirespawnfolder = WorldFunctions.getWorldPath((ServerLevel)loopworld) + File.separator + "data" + File.separator + "campfirespawn" + File.separator + DimensionFunctions.getSimpleDimensionString(loopworld);
					File dir = new File(campfirespawnfolder);
					dir.mkdirs();
					
					String filename = fromplayername.toLowerCase() + ".spawnpoint";
					try {
						PrintWriter writer = new PrintWriter(campfirespawnfolder + File.separator + filename, "UTF-8");
						writer.print("");
						writer.close();
					} catch (Exception e) {
						System.out.println("[Error] Campfire Spawn: Something went wrong while removing a campfire spawn location for player " + playername + ".");
						continue;
					}
				}
				
				CampfireEvent.playercampfires.remove(fromplayername);
				removed = true;
				
				if (ConfigHandler.GENERAL.sendMessageOnCampfireSpawnUnset.get()) {
					if (fromplayername.equalsIgnoreCase(playername)) {
						continue;
					}
					
					Player target = PlayerFunctions.matchPlayer(world, fromplayername);
					if (target == null) {
						continue;
					}
					
					StringFunctions.sendMessage(target, "Campfire spawn point removed.", ChatFormatting.DARK_GRAY);
				}
			}
		}
		
		if (removed && fromplayernames.contains(playername.toLowerCase())) {
			return true;
		}
		
		return false;
	}
}
