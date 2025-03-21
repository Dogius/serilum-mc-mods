/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.treeharvester.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mojang.datafixers.util.Pair;
import com.natamus.thevanillaexperience.mods.treeharvester.config.TreeHarvesterConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TreeHarvesterUtil {
	public static HashMap<BlockPos, Integer> highestleaf = new HashMap<BlockPos, Integer>();
	public static CopyOnWriteArrayList<Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>> lowerlogs = new CopyOnWriteArrayList<Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>>();
	
	public static int isTreeAndReturnLogAmount(Level world, BlockPos pos) {
		highestleaf.put(pos, 0);
		
		int leafcount = 20;
		int logcount = 0;
		int prevleafcount = -1;
		int prevlogcount = -1;
	
		int highesty = 0;
		for (int y = 1; y<=30; y+=1) {
			if (prevleafcount == leafcount && prevlogcount == logcount) {
				break;
			}
			prevleafcount = leafcount;
			prevlogcount = logcount;
			
			Iterator<BlockPos> it = BlockPos.betweenClosedStream(pos.getX()-2, pos.getY()+(y-1), pos.getZ()-2, pos.getX()+2, pos.getY()+(y-1), pos.getZ()+2).iterator();
			while (it.hasNext()) {
				BlockPos npos = it.next();
				Block nblock = world.getBlockState(npos).getBlock();
				if (isTreeLeaf(nblock)) {
					leafcount-=1;
					if (npos.getY() > highesty) {
						highesty = npos.getY();
					}
				}
				else if (isTreeLog(nblock)) {
					logcount+=1;
				}
			}
		}
		
		highestleaf.put(pos.immutable(), highesty);
		
		if (leafcount < 0) {
			return logcount;
		}
		return -1;
	}
	
	public static boolean isTreeLeaf(Block block) {
		if (BlockTags.LEAVES.contains(block) || block instanceof LeavesBlock) {
			return true;
		}
		if (block instanceof BushBlock) {
			if (block instanceof CropBlock == false && block instanceof DeadBushBlock == false && block instanceof DoublePlantBlock == false && block instanceof FlowerBlock == false && block instanceof SaplingBlock == false && block instanceof StemBlock == false && block instanceof AttachedStemBlock == false && block instanceof SweetBerryBushBlock == false && block instanceof TallGrassBlock == false) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isTreeLog(Block block) {
		if (BlockTags.LOGS.contains(block) || block instanceof RotatedPillarBlock) {
			return true;
		}
		return false;
	}
	
	public static boolean isSapling(ItemStack itemstack) {
		Item item = itemstack.getItem();
		if (Block.byItem(item) instanceof SaplingBlock) {
			return true;
		}
		return false;
	}
	
	public static List<BlockPos> getAllLogsToBreak(Level world, BlockPos pos, int logcount) {
		CopyOnWriteArrayList<BlockPos> bottomlogs = new CopyOnWriteArrayList<BlockPos>();
		if (TreeHarvesterConfigHandler.GENERAL.replaceSaplingIfBottomLogIsBroken.get()) {
			if (world.getBlockState(pos.below()).getBlock().equals(Blocks.DIRT)) {
				Iterator<BlockPos> it = BlockPos.betweenClosedStream(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY(), pos.getZ()+1).iterator();
				while (it.hasNext()) {
					BlockPos npos = it.next();
					Block block = world.getBlockState(npos).getBlock();
					if (isTreeLog(block)) {
						bottomlogs.add(npos.immutable());
					}
				}
			}
		}
		
		if (TreeHarvesterConfigHandler.GENERAL.replaceSaplingIfBottomLogIsBroken.get()) {
			if (TreeHarvesterConfigHandler.GENERAL.instantBreakLeavesAround.get()) {
				replaceSapling(world, pos, bottomlogs, 1);
			}
			else if (TreeHarvesterConfigHandler.GENERAL.enableFastLeafDecay.get()){
				lowerlogs.add(new Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>(pos.immutable(), bottomlogs));
			}
		}
		
		return getLogsToBreak(world, pos, new ArrayList<BlockPos>(), logcount);
	}
	
	public static void replaceSapling(Level world, BlockPos pos, CopyOnWriteArrayList<BlockPos> bottomlogs, int radius) {
    	int reducecount = bottomlogs.size();
    	int rc = reducecount;
		ItemStack sapling = null;
		
		Iterator<Entity> entitiesaround = world.getEntities(null, new AABB(pos.getX()-radius, pos.getY()-2, pos.getZ()-radius, pos.getX()+radius, pos.getY()+30, pos.getZ()+radius)).iterator();
		while (entitiesaround.hasNext()) {
			Entity ea = entitiesaround.next();
			if (ea instanceof ItemEntity) {
				ItemEntity eia = (ItemEntity)ea;
				ItemStack eisa = eia.getItem();
				if (isSapling(eisa)) {
					if (sapling == null) {
						sapling = eisa.copy();
					}
					
					int count = eisa.getCount();
					if (count > 1) {
						for (int n = 0; n < count; n++) {
							eisa.shrink(1);
							rc-=1;
							
							if (rc == 0) {
								break;
							}
						}
						eia.setItem(eisa);
					}
					else {
						rc-=1;
						eia.remove(RemovalReason.DISCARDED);
					}
					
					if (rc == 0) {
						break;
					}
				}
			}
		}
    	
		int setsaplings = bottomlogs.size()-rc;
		for (BlockPos bottompos : bottomlogs) {
			if (setsaplings == 0) {
				break;
			}
			
			world.setBlockAndUpdate(bottompos, Block.byItem(sapling.getItem()).defaultBlockState());
			setsaplings-=1;
			bottomlogs.remove(bottompos);
		}
		
		if (bottomlogs.size() > 0) {
			if (radius >= 5) {
				return;
			}
			replaceSapling(world, pos, bottomlogs, radius+2);
		}
	}
	
	private static List<BlockPos> getLogsToBreak(Level world, BlockPos pos, List<BlockPos> logstobreak, int logcount) {
		List<BlockPos> checkaround = new ArrayList<BlockPos>();
		
		Iterator<BlockPos> aroundlogs = BlockPos.betweenClosedStream(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY()+1, pos.getZ()+1).iterator();
		while (aroundlogs.hasNext()) {
			BlockPos nalogpos = aroundlogs.next().immutable();
			if (logstobreak.contains(nalogpos)) {
				continue;
			}
			BlockState logstate = world.getBlockState(nalogpos);
			Block logblock = logstate.getBlock();
			if (isTreeLog(logblock)) {
				checkaround.add(nalogpos);
				logstobreak.add(nalogpos);
				
				Pair<Integer, Integer> hv = getHorizontalAndVerticalValue(logcount);
				int h = hv.getFirst();
				int v = hv.getSecond();

				Iterator<BlockPos> aroundleaves = BlockPos.betweenClosedStream(pos.getX()-h, pos.getY(), pos.getZ()-h, pos.getX()+h, pos.getY()+v, pos.getZ()+h).iterator();
				while (aroundleaves.hasNext()) {
					BlockPos naleafpos = aroundleaves.next();
					Block leafblock = world.getBlockState(naleafpos).getBlock();
					if (isTreeLeaf(leafblock)) {
						if (TreeHarvesterConfigHandler.GENERAL.instantBreakLeavesAround.get()) {
							world.destroyBlock(naleafpos, true);
						}
					}
				}
			}
		}
		
		if (checkaround.size() == 0) {
			return logstobreak;
		}
		
		for (BlockPos capos : checkaround) {
			for (BlockPos logpos : getLogsToBreak(world, capos, logstobreak, logcount)) {
				if (!logstobreak.contains(logpos)) {
					logstobreak.add(logpos.immutable());
				}
			}
		}
		
		BlockPos up = pos.above(2);
		return getLogsToBreak(world, up.immutable(), logstobreak, logcount);
	}
	
	public static Pair<Integer, Integer> getHorizontalAndVerticalValue(int logcount) {
		int h = 3; // horizontal
		int v = 4; // vertical
		if (logcount >= 20) {
			h = 5;
			v = 5;	
		}
		else if (logcount >= 10) {
			h = 4;
			v = 5;
		}
		
		return new Pair<Integer, Integer>(h, v);
	}
}