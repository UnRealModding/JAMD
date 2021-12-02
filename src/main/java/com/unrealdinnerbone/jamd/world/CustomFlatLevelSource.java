package com.unrealdinnerbone.jamd.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

public class CustomFlatLevelSource extends FlatLevelSource {

    public static final Codec<CustomFlatLevelSource> CODEC = FlatLevelGeneratorSettings.CODEC.fieldOf("settings").xmap(CustomFlatLevelSource::new, CustomFlatLevelSource::settings).codec();


    public CustomFlatLevelSource(FlatLevelGeneratorSettings p_64168_) {
        super(p_64168_);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    public int getMinY() {
        return -64;
    }
}
