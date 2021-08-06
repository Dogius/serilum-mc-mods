/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.smallernetherportals.events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.natamus.thevanillaexperience.mods.smallernetherportals.util.SmallerNetherPortalsUtil;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SmallerNetherPortalsPortalEvent {
	private static Map<String, BlockPos> toposes = new HashMap<String, BlockPos>();
	
	@SubscribeEvent
	public void onClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.FLINT_AND_STEEL)) {
        	BlockPos clickpos = e.getPos();
        	
        	int obsidiancount = 0;
        	Iterator<BlockPos> it = BlockPos.betweenClosedStream(clickpos.getX()-3, clickpos.getY()-3, clickpos.getZ()-3, clickpos.getX()+3, clickpos.getY()+3, clickpos.getZ()+3).iterator();
        	while (it.hasNext()) {
        		BlockPos np = it.next();
        		if (world.getBlockState(np).getBlock().equals(Blocks.OBSIDIAN)) {
        			obsidiancount+=1;
        		}
        	}
			
        	if (obsidiancount >= 6) {
				new Thread( new Runnable() {
			    	public void run()  {
			        	try  { Thread.sleep( 10 ); }
			            catch (InterruptedException ie)  {}
			        	
			        	BlockPos topos = clickpos;
			        	
			        	Boolean foundportal = false;
			        	Iterator<BlockPos> it = BlockPos.betweenClosedStream(clickpos.getX()-1, clickpos.getY()-1, clickpos.getZ()-1, clickpos.getX()+1, clickpos.getY()+1, clickpos.getZ()+1).iterator();
			        	while (it.hasNext()) {
			        		BlockPos np = it.next();
			        		Block bsblock = world.getBlockState(np).getBlock();
			        		if (bsblock instanceof NetherPortalBlock) {
			        			foundportal = true;
			        		}
			        		else if (bsblock.equals(Blocks.FIRE)) {
			        			if (SmallerNetherPortalsUtil.isAir(world.getBlockState(np.below(1)))) {
			        				topos = np.below(1).immutable();
			        			}
			        			else if (SmallerNetherPortalsUtil.isAir(world.getBlockState(np.below(2)))) {
			        				topos = np.below(2).immutable();
			        			}
			        			else {
			        				topos = np.immutable();
			        			}
			        		}
			        	}
			        	
			        	if (!foundportal) {
			        		if (SmallerNetherPortalsUtil.isAir(world.getBlockState(topos))) {
			        			SmallerNetherPortalsUtil.processSmallerPortal(world, topos.immutable());
			        		}
			        	}
			    	}
			    } ).start();
        	}
		}
	}
	
	@SubscribeEvent
	public void onDimensionChange(PlayerChangedDimensionEvent e) {
		Player player = e.getPlayer();
		Level world = player.level;
		if (world.isClientSide) {
			return;
		}
		
		BlockPos pos = player.blockPosition();
		Block block = world.getBlockState(pos).getBlock();
		
		if (block instanceof NetherPortalBlock) {
			return;
		}
		
		String playername = player.getName().getString();
		
		if (!toposes.containsKey(playername)) {
			BlockPos foundpos = SmallerNetherPortalsUtil.findPortalAround(world, pos);
			if (foundpos != null) {
				List<BlockPos> frontblocks = SmallerNetherPortalsUtil.getFrontBlocks(world, foundpos);
				SmallerNetherPortalsUtil.setObsidian(world, frontblocks);
				
	        	toposes.put(playername, frontblocks.get(frontblocks.size()-1).above().immutable());
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		String playername = player.getName().getString();
		if (!toposes.containsKey(playername)) {
			return;
		}
		
		BlockPos topos = toposes.get(playername); 
		
		player.setPortalCooldown(); // reset time until portal
		player.teleportTo(((double)topos.getX())+0.5, (double)topos.getY(), ((double)topos.getZ())+0.5);
		toposes.remove(playername);
	}
}
