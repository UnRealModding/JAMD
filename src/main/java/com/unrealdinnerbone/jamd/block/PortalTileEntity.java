package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.JAMDRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PortalTileEntity extends BlockEntity {

    private ResourceLocation worldId;

    public PortalTileEntity(BlockPos blockPos, BlockState blockState) {
        super(JAMDRegistry.PORTAL.get(), blockPos, blockState);
    }

    public void setWorldId(ResourceLocation worldId) {
        this.worldId = worldId;
        setChanged();
    }

    public ResourceLocation getWorldId() {
        return worldId == null ? new ResourceLocation("minecraft", "empty") : worldId;
    }

    @Override
    public void load(CompoundTag tag) {
        if(tag.contains("world_id")) {
            worldId = ResourceLocation.tryParse(tag.getString("world_id"));
        }
        super.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putString("world_id", getTheWorldId());
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }


    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    public String getTheWorldId() {
        return worldId == null ? "" : worldId.toString();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

}
