package com.unrealdinnerbone.jamd.util;

import com.unrealdinnerbone.jamd.JAMDRegistry;
import com.unrealdinnerbone.jamd.block.PortalBlock;
import com.unrealdinnerbone.jamd.block.PortalTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class TelerportUtils {


    public static void teleport(Block clickedBlock, Player playerEntity, Level toWorld, BlockPos blockPos) throws RuntimeException {
        if(!toWorld.isClientSide() && playerEntity.level instanceof ServerLevel && toWorld instanceof ServerLevel) {
            BlockPos portalLocation = findPortalLocation(toWorld, blockPos).orElseThrow(() -> new RuntimeException("Cant find location to spawn portal"));
            if (toWorld.getBlockState(portalLocation).isAir()) {
                PortalBlock.placeBlock(clickedBlock, toWorld, portalLocation, playerEntity.level.dimension());
            }
            playerEntity.changeDimension((ServerLevel) toWorld, new SimpleTeleporter(portalLocation.getX(), portalLocation.above().getY(), portalLocation.getZ()));
        }
    }


    private static Optional<BlockPos> findPortalLocation(Level worldTo, BlockPos fromPos) {
        if(worldTo.getBlockState(fromPos).getBlock() == JAMDRegistry.MINE_PORTAL_BLOCK.get() && isSafeSpawnLocation(worldTo, fromPos)) {
            return Optional.of(fromPos.above());
        }

        int range = 5;
        return Optional.ofNullable(ChunkPos.rangeClosed(worldTo.getChunkAt(fromPos).getPos(), range)
                .map(chunkPos -> worldTo.getChunk(chunkPos.x, chunkPos.z).getBlockEntitiesPos())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()).stream()
                .filter(pos -> worldTo.getBlockEntity(pos) instanceof PortalTileEntity)
                .findFirst()
                .orElseGet(() -> {
                    BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(0, 0, 0);
                        for (int y = worldTo.getMinBuildHeight(); y < worldTo.getMaxBuildHeight(); y++) {
                            for (int x = fromPos.getX() - 6; x < fromPos.getX() + 6; x++) {
                                for (int z = fromPos.getZ() - 6; z < fromPos.getZ() + 6; z++) {
                                    mutableBlockPos.set(x, y, z);
                                    BlockState blockState = worldTo.getBlockState(mutableBlockPos);
                                    if (blockState.isAir() && isSafeSpawnLocation(worldTo, mutableBlockPos.above())) {
                                        return mutableBlockPos;
                                    }
                                }
                            }
                        }
                    return null;
                }));

    }


    private static boolean isSafeSpawnLocation(Level world, BlockPos blockPos) {
        return world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos.above()).isAir();
    }


}
