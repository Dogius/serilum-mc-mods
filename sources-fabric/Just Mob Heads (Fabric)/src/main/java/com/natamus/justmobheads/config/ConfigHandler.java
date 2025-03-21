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

package com.natamus.justmobheads.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_mobSpecificDropChances;
	@Entry public static boolean mobSpecificDropChances = true;

	@Comment public static Comment DESC_enableStandardHeads;
	@Entry public static boolean enableStandardHeads = false;

	@Comment public static Comment DESC_enableLootingEnchant;
	@Entry public static boolean enableLootingEnchant = true;

	@Comment public static Comment DESC_onlyAdultMobsDropTheirHead;
	@Entry public static boolean onlyAdultMobsDropTheirHead = true;

	@Comment public static Comment DESC_overallDropChance;
	@Entry public static double overallDropChance = 0.1;
	@Comment public static Comment RANGE_overallDropChance;

	@Comment public static Comment DESC_creeperSkeletonZombieDropChance;
	@Entry public static double creeperSkeletonZombieDropChance = 0.1;
	@Comment public static Comment RANGE_creeperSkeletonZombieDropChance;

	@Comment public static Comment DESC_onlyDropHeadsByChargedCreeper;
	@Entry public static boolean onlyDropHeadsByChargedCreeper = false;

	@Comment public static Comment DESC_onlyDropHeadsByPlayerKill;
	@Entry public static boolean onlyDropHeadsByPlayerKill = false;
}