package com.unrealdinnerbone.jamd.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class CustomFlatLevelSource extends FlatLevelSource {

    public static final Codec<CustomFlatLevelSource> CODEC = RecordCodecBuilder.create((instance) ->
            commonCodec(instance).and(
                    FlatLevelGeneratorSettings.CODEC
                            .fieldOf("settings")
                            .forGetter(CustomFlatLevelSource::settings))
                    .apply(instance, instance.stable(CustomFlatLevelSource::new)));
    public CustomFlatLevelSource(Registry<StructureSet> registry, FlatLevelGeneratorSettings settings) {
        super(registry, settings);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    public int getMinY() {
        return -64;
    }
}
