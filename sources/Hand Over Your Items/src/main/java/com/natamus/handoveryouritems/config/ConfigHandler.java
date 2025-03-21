/*
 * This is the latest source code of Hand Over Your Items.
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

package com.natamus.handoveryouritems.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> sendItemReceivedMessage;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendItemGivenMessage;
		public final ForgeConfigSpec.ConfigValue<Boolean> mustCrouchToGiveItem;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			sendItemReceivedMessage = builder
					.comment("If enabled, players will receive a message when they have been given an item.")
					.define("sendItemReceivedMessage", true);
			sendItemGivenMessage = builder
					.comment("If enabled, players will receive a message when they give an item.")
					.define("sendItemGivenMessage", true);
			mustCrouchToGiveItem = builder
					.comment("If enabled, players will only be able to give items when they are crouching/sneaking.")
					.define("mustCrouchToGiveItem", true);
			
			builder.pop();
		}
	}
}