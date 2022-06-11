package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.JAMD;
import com.unrealdinnerbone.jamd.JAMDRegistry;
import com.unrealdinnerbone.jamd.util.TelerportUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class PortalBlock extends Block implements EntityBlock {


    public PortalBlock() {
        super(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            try {
                if(level.dimension().location().equals(JAMD.DIM_ID)) {
                    TelerportUtils.teleport(this, player, JAMD.getMainWorld(level.getServer()).orElseThrow(() -> new RuntimeException("Can't find world with id " + JAMD.getMainWorld())), pos);
                }else {
                    TelerportUtils.teleport(this, player, JAMD.getMining(level.getServer()).orElseThrow(() -> new RuntimeException("Error getting mining dimension")), pos);
                }
            } catch (Exception e) {
                player.displayClientMessage(Component.literal(e.getMessage()), false);
            }
            return InteractionResult.PASS;
        }else {
            return InteractionResult.FAIL;
        }
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return JAMDRegistry.PORTAL.get().create(pos, state);
    }
}
