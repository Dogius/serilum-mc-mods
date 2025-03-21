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

package com.natamus.collective_fabric.schematic;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ParsedSchematicObject {
    public Schematic schematic;
    public List<Pair<BlockPos, BlockState>> blocks;
    public List<Pair<BlockPos, Entity>> entities;
    public List<BlockPos> blockEntityPositions;
    public String parseMessageString;
    public boolean parsedCorrectly;

    public int offsetX;
    public int offsetY;
    public int offsetZ;

    public ParsedSchematicObject(Schematic _schematic, List<Pair<BlockPos, BlockState>> _blocks, List<Pair<BlockPos, Entity>> _entities, List<BlockPos> _blockEntityPositions, String _parseMessageString, boolean _parsedCorrectly) {
        schematic = _schematic;
        blocks = _blocks;
        entities = _entities;
        blockEntityPositions = _blockEntityPositions;
        parseMessageString = _parseMessageString;
        parsedCorrectly = _parsedCorrectly;

        offsetX = _schematic.getOffsetX();
        offsetY = _schematic.getOffsetY();
        offsetZ = _schematic.getOffsetZ();
    }

    public void placeBlockEntitiesInWorld(Level level) {
        List<CompoundTag> compoundTags = schematic.getBlockEntities();

        int i = 0;
        for (Pair<BlockPos, BlockEntity> pair : getBlockEntities(level)) {
            BlockPos pos = pair.getFirst();

            CompoundTag compoundTag = compoundTags.get(i);
            compoundTag.remove("Pos");
            compoundTag.putString("id", compoundTag.getString("Id"));

            BlockEntity blockEntity = BlockEntity.loadStatic(pos, level.getBlockState(pos), compoundTag);
            level.setBlockEntity(blockEntity);
            i+=1;
        }
    }

    public List<Pair<BlockPos, BlockEntity>> getBlockEntities(Level level) {
        List<Pair<BlockPos, BlockEntity>> blockEntities = new ArrayList<Pair<BlockPos, BlockEntity>>();
        for (BlockPos pos : blockEntityPositions) {
            blockEntities.add(new Pair<BlockPos, BlockEntity>(pos, level.getBlockEntity(pos)));
        }
        return blockEntities;
    }
}