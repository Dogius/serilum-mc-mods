/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.45.
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

package com.natamus.collective_fabric.check;

import com.natamus.collective_fabric.config.CollectiveConfigHandler;
import com.natamus.collective_fabric.functions.DataFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.collective_fabric.functions.WorldFunctions;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RegisterMod {
	private static final CopyOnWriteArrayList<String> jarlist = new CopyOnWriteArrayList<String>();
	private static final HashMap<String, String> jartoname = new HashMap<String, String>();
	public static boolean shouldDoCheck = true;
	
	public static void register(String modname, String modid, String modversion, String fabricversion) {
		String jarname = modid + "_" + fabricversion.replaceAll("\\[", "").replaceAll("\\]","") + "-" + modversion + ".jar";
		if (modid.equalsIgnoreCase("collective-fabric")) {
			jarname = jarname.replaceAll("_", "-");
		}
		
		jarlist.add(jarname);
		jartoname.put(jarname, modname);
	}
	
	public static void initialProcess() {
		if (!CollectiveConfigHandler.enableAntiRepostingCheck) {
			shouldDoCheck = false;
		}
		else if (!checkAlternative()) {
			shouldDoCheck = false;
		}
	}
	
	public static void joinWorldProcess(Level world, Player player) {
		if (!(world instanceof ServerLevel)) {
			return;
		}
		
		List<String> wrongmodnames = checkIfAllJarsExist();
		if (wrongmodnames.size() > 0) {
			if (processPreJoinWorldCheck(world)) {
				String s = "";
				if (wrongmodnames.size() > 1) {
					s = "s";
				}
				
				String projecturl = "https://curseforge.com/members/serilum/projects";
				StringFunctions.sendMessage(player, "Mod" + s + " from incorrect sources:", ChatFormatting.RED, projecturl);
				for (String wrongmodname : wrongmodnames) {
					StringFunctions.sendMessage(player, " " + wrongmodname, ChatFormatting.YELLOW, projecturl);
				}
				
				StringFunctions.sendMessage(player, "You are receiving this message because you are using some of Serilum's mods, but probably haven't downloaded them from the original source. Unofficial sources can contain malicious software, supply no income for developers and host outdated versions.", ChatFormatting.RED, projecturl);
				StringFunctions.sendMessage(player, "Serilum's mod downloads are only officially available at:", ChatFormatting.DARK_GREEN, projecturl);
				StringFunctions.sendMessage(player, " https://curseforge.com/members/serilum/projects (click)", ChatFormatting.YELLOW, projecturl);
				StringFunctions.sendMessage(player, "You won't see this message again in this instance. Thank you for reading.", ChatFormatting.DARK_GREEN, projecturl);
				StringFunctions.sendMessage(player, "-Rick (Serilum)", ChatFormatting.YELLOW, projecturl);
				
				processPostJoinWorldCheck(world);
			}
		}
		
		shouldDoCheck = false;
	}
	
	private static boolean processPreJoinWorldCheck(Level world) {
		String checkfilepath = WorldFunctions.getWorldPath((ServerLevel)world) + File.separator + "config" + File.separator + "collective" + File.separator + "checked.txt";
		File checkfile = new File(checkfilepath);
		if (checkfile.exists()) {
			shouldDoCheck = false;
		}
		else if (!checkAlternative()) {
			shouldDoCheck = false;
		}
		
		return shouldDoCheck;
	}
	
	private static void processPostJoinWorldCheck(Level world) {
		shouldDoCheck = false;

		String worlddatapath = WorldFunctions.getWorldPath((ServerLevel)world) + File.separator + "config" + File.separator + "collective";
		File dir = new File(worlddatapath);
		if (!dir.mkdirs()) {
			return;
		}
		
		try {
			PrintWriter writer = new PrintWriter(worlddatapath + File.separator + "checked.txt", StandardCharsets.UTF_8);
			writer.println("# Please check out https://stopmodreposts.org/ for more information on why this feature exists.");
			writer.println("checked=true");
			writer.close();
		} catch (Exception ignored) { }
		
		String alternativecheckpath = DataFunctions.getConfigDirectory() + File.separator + "collective";
		File alternativedir = new File(alternativecheckpath);
		if (!alternativedir.mkdirs()) {
			return;
		}
		
		try {
			PrintWriter writer = new PrintWriter(alternativecheckpath + File.separator + "checked.txt", StandardCharsets.UTF_8);
			writer.println("# Please check out https://stopmodreposts.org/ for more information on why this feature exists.");
			writer.println("checked=true");
			writer.close();
		} catch (Exception ignored) { }
	}
	
	private static List<String> checkIfAllJarsExist() {
		List<String> installedmods = DataFunctions.getInstalledModJars();
		
		List<String> wrongmodnames = new ArrayList<String>();
		for (String jarname : jarlist) {
			if (!installedmods.contains(jarname) && jartoname.containsKey(jarname)) {
				wrongmodnames.add(jartoname.get(jarname));
			}
		}
		
		if (wrongmodnames.size() > 0) {
			Collections.sort(wrongmodnames);
		}
		return wrongmodnames;
	}
	
	private static boolean checkAlternative() {
		String alternativecheckfilepath = DataFunctions.getConfigDirectory() + File.separator + "collective" + File.separator + "checked.txt";
		File alternativecheckfile = new File(alternativecheckfilepath);
		return !alternativecheckfile.exists();
	}
}
