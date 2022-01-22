package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.JAMD;
import com.unrealdinnerbone.jamd.JAMDRegistry;
import com.unrealdinnerbone.jamd.util.TelerportUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Optional;

public class PortalBlock extends Block implements EntityBlock {


    public PortalBlock() {
        super(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            try {
                TelerportUtils.teleport(this, player, getWorldFromTileEntity(level, pos).orElseThrow(() -> new RuntimeException("Invalid world ID set")), pos);
            } catch (Exception e) {
                player.displayClientMessage(new TextComponent(e.getMessage()), false);
            }
            return InteractionResult.PASS;
        }else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof PortalTileEntity portalTileEntity) {
                if(portalTileEntity.getWorldId() != null) {
                    if(!level.dimension().equals(Level.OVERWORLD)) {
                        portalTileEntity.setWorldId(Level.OVERWORLD.location());
                    }else {
                        portalTileEntity.setWorldId(JAMD.DIM_ID);
                    }
                    portalTileEntity.setChanged();
                }
            }
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    public static void placeBlock(Block block, Level level, BlockPos blockPos, ResourceKey<Level> portalTo) {
        level.setBlockAndUpdate(blockPos, block.defaultBlockState());
        if(level.getBlockEntity(blockPos) instanceof PortalTileEntity blockEntity) {
            blockEntity.setWorldId(portalTo.location());
            blockEntity.setChanged();
        }
    }

    public static Optional<Level> getWorldFromTileEntity(Level level, BlockPos blockPos) {
        if (level.getBlockEntity(blockPos) instanceof PortalTileEntity portalTileEntity) {
            if (portalTileEntity.getWorldId() != null) {
                return Optional.ofNullable(level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, portalTileEntity.getWorldId())));
            }
        }
        return Optional.empty();
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return JAMDRegistry.PORTAL.get().create(pos, state);
    }
}
