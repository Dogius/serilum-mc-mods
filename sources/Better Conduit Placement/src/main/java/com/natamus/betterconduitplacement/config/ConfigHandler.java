/*
 * This is the latest source code of Better Conduit Placement.
 * Minecraft version: 1.19.3, mod version: 2.0.
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

package com.natamus.betterconduitplacement.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> breakConduitBlocks;
		public final ForgeConfigSpec.ConfigValue<Boolean> dropReplacedBlockTopConduit;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			breakConduitBlocks = builder
					.comment("If enabled, drops all conduit blocks when the conduit itself is broken.")
					.define("breakConduitBlocks", true);
			dropReplacedBlockTopConduit = builder
					.comment("If enabled, when prismarine replaces a normal block that block is dropped on top of the conduit.")
					.define("dropReplacedBlockTopConduit", true);
			
			builder.pop();
		}
	}
}