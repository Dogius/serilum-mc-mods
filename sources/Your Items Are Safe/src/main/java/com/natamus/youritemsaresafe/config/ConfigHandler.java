/*
 * This is the latest source code of Your Items Are Safe.
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

package com.natamus.youritemsaresafe.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHaveItemsInInventoryForCreation;
		public final ForgeConfigSpec.ConfigValue<Boolean> addPlayerHeadToArmorStand;
		public final ForgeConfigSpec.ConfigValue<Boolean> createArmorStand;
		public final ForgeConfigSpec.ConfigValue<Boolean> createSignWithPlayerName;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> needChestMaterials;
		public final ForgeConfigSpec.ConfigValue<Boolean> needArmorStandMaterials;
		public final ForgeConfigSpec.ConfigValue<Boolean> needSignMaterials;
		public final ForgeConfigSpec.ConfigValue<Boolean> ignoreStoneMaterialNeed;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnCreationFailure;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnCreationSuccess;
		
		public final ForgeConfigSpec.ConfigValue<String> creationFailureMessage;
		public final ForgeConfigSpec.ConfigValue<String> creationSuccessMessage;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustHaveItemsInInventoryForCreation = builder
					.comment("When enabled and a player dies without any items in their inventory, no chest or armor stand is generated.")
					.define("mustHaveItemsInInventoryForCreation", true);
			addPlayerHeadToArmorStand = builder
					.comment("If a player head should be added to the armor stand. If a helmet is worn, this will be placed into the chest.")
					.define("addPlayerHeadToArmorStand", true);
			createArmorStand = builder
					.comment("Whether an armor stand should be created on death. If disabled, the player's gear will be placed inside the chest.")
					.define("createArmorStand", true);
			createSignWithPlayerName = builder
					.comment("Whether a sign should be placed on the chest with the name of the player who died there.")
					.define("createSignWithPlayerName", true);
			
			needChestMaterials = builder
					.comment("Whether materials are needed for the chest which spawns on death. This can be the actual chest or the costs in raw materials.")
					.define("needChestMaterials", true);
			needArmorStandMaterials = builder
					.comment("Whether materials are needed for the armor stand to spawn on death. This can be the actual armor stand or the costs in raw materials.")
					.define("needArmorStandMaterials", true);
			needSignMaterials = builder
					.comment("Whether materials are needed for the creation of the sign when 'createSignWithPlayerName' is enabled.")
					.define("needSignMaterials", false);
			ignoreStoneMaterialNeed = builder
					.comment("Only relevant if 'needChestAndArmorStandMaterials' is enabled. An armor stand needs 1 stone slab to be created, but I think it's alright to ignore that requirement. If enabled, no stone is needed in the inventory on death.")
					.define("ignoreStoneMaterialNeed", true);
			
			sendMessageOnCreationFailure = builder
					.comment("If a message should be sent if the chest or armor stand can't be created due to missing materials.")
					.define("sendMessageOnCreationFailure", true);
			sendMessageOnCreationSuccess = builder
					.comment("If a message should be sent on successful creation of the chest(s) and armor stand.")
					.define("sendMessageOnCreationSuccess", true);
			
			creationFailureMessage = builder
					.comment("The message sent on creation failure with 'sendMessageOnCreationFailure' enabled. Possible replacement values: %plankamount%, %stoneamount%.")
					.define("creationFailureMessage", "Your items are not safe due to having insufficient materials. Missing: %plankamount% planks.");
			creationSuccessMessage = builder
					.comment("The message sent on creation success with 'sendMessageOnCreationSuccess' enabled.")
					.define("creationSuccessMessage", "Your items are safe at your death location.");
			
			builder.pop();
		}
	}
}