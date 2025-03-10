/*
 * This is the latest source code of Nutritious Milk.
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

package com.natamus.nutritiousmilk.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Integer> hungerLevelIncrease;
		public final ForgeConfigSpec.ConfigValue<Double> saturationLevelIncrease;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			hungerLevelIncrease = builder
					.comment("The hunger level decreased. Example values: cookie = 2, bread = 5, salmon = 6, steak = 8.")
					.defineInRange("hungerLevelIncrease", 10, 0, 20);
			saturationLevelIncrease = builder
					.comment("The saturation increase. Example values: melon = 1.2, carrot = 3.6, chicken = 7.2, steak 12.8.")
					.defineInRange("saturationLevelIncrease", 10.0, 0, 20.0);
			
			builder.pop();
		}
	}
}