package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.JAMDRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class PortalTileEntity extends TileEntity {

    private ResourceLocation worldId;

    public PortalTileEntity() {
        super(JAMDRegistry.PORTAL.get());
    }

    public void setWorldId(ResourceLocation worldId) {
        this.worldId = worldId;
    }

    public ResourceLocation getWorldId() {
        return worldId == null ? new ResourceLocation("minecraft", "empty") : worldId;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putString("world_id", getTheWorldId());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        if(nbt.contains("world_id")) {
            worldId = ResourceLocation.tryParse(nbt.getString("world_id"));
        }
        super.load(state, nbt);
    }


    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putString("world_id", getTheWorldId());
        return new SUpdateTileEntityPacket(getBlockPos(), 0, compoundNBT);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        if(nbt.contains("world_id")) {
            worldId = ResourceLocation.tryParse(nbt.getString("world_id"));
        }

    }

    public String getTheWorldId() {
        return worldId == null ? "" : worldId.toString();
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putString("world_id", getTheWorldId());
        return compoundNBT;
    }

}
