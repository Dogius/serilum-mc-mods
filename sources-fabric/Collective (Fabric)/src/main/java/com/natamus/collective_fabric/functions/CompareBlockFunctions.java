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

package com.natamus.collective_fabric.functions;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.*;

public class CompareBlockFunctions {
	public static boolean blockIsInRegistryHolder(Block block, TagKey<Block> tagKey) {
		return block.builtInRegistryHolder().is(tagKey);
	}

	public static boolean isStoneBlock(Block block) {
		return blockIsInRegistryHolder(block, BlockTags.BASE_STONE_OVERWORLD);
	}

	public static boolean isNetherStoneBlock(Block block) {
		return blockIsInRegistryHolder(block, BlockTags.BASE_STONE_NETHER);
	}

	public static boolean isTreeLeaf(Block block, boolean withNetherVariants) {
		if (blockIsInRegistryHolder(block, BlockTags.LEAVES) || block instanceof LeavesBlock) {
			return true;
		}
		if (withNetherVariants) {
			if (block.equals(Blocks.NETHER_WART_BLOCK) || block.equals(Blocks.WARPED_WART_BLOCK) || block.equals(Blocks.SHROOMLIGHT)) {
				return true;
			}
		}
		if (block instanceof BushBlock) {
			return !(block instanceof CropBlock) && !(block instanceof DeadBushBlock) && !(block instanceof DoublePlantBlock) && !(block instanceof FlowerBlock) && !(block instanceof SaplingBlock) && !(block instanceof StemBlock) && !(block instanceof AttachedStemBlock) && !(block instanceof SweetBerryBushBlock) && !(block instanceof TallGrassBlock);
		}
		return false;
	}
	public static boolean isTreeLeaf(Block block) {
		return isTreeLeaf(block, true);
	}

	public static boolean isTreeLog(Block block) {
		return blockIsInRegistryHolder(block, BlockTags.LOGS) || block instanceof RotatedPillarBlock;
	}

	public static boolean isSapling(Block block) {
		return blockIsInRegistryHolder(block, BlockTags.SAPLINGS) || block instanceof SaplingBlock;
	}

	public static boolean isDirtBlock(Block block) {
		return block.equals(Blocks.GRASS_BLOCK) || block.equals(Blocks.DIRT) || block.equals(Blocks.COARSE_DIRT) || block.equals(Blocks.PODZOL);
	}

	public static boolean isPortalBlock(Block block) {
		return block instanceof NetherPortalBlock || BlockFunctions.blockToReadableString(block).equals("portal placeholder");
	}

	public static boolean isAirOrOverwritableBlock(Block block) {
		return block.equals(Blocks.AIR) || (block instanceof BushBlock) || (block instanceof SnowLayerBlock);
	}
}
