/*
 * This is the latest source code of Surface Mushrooms.
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

package com.natamus.surfacemushrooms.events;

import com.natamus.collective_fabric.data.GlobalVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MushroomBlockEvent {
    public static boolean onMushroomPlace(Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
        if (world.isClientSide) {
            return true;
        }

        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        Block block = Block.byItem(item);
        if ((!(block instanceof MushroomBlock))) {
            return true;
        }

        BlockState state = world.getBlockState(pos);
        if (!state.isSolidRender(world, pos)) { // may place mushroom on block
            return true;
        }

        BlockPos above = pos.above();
        Block aboveblock = world.getBlockState(above).getBlock();
        if (aboveblock.equals(Blocks.AIR)) {
            BlockState placestate = block.defaultBlockState();
            world.setBlock(above, placestate, 3);

            player.swing(hand);

            if (!player.isCreative()) {
                itemstack.shrink(1);
            }

            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 0.5F, 0.4F / (GlobalVariables.random.nextFloat() * 0.4F + 0.8F));
            return false;
        }
        return true;
    }
}