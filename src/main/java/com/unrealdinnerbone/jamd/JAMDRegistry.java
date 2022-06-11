package com.unrealdinnerbone.jamd;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.jamd.block.PortalBlock;
import com.unrealdinnerbone.jamd.block.PortalTileEntity;
import com.unrealdinnerbone.jamd.world.CustomFlatLevelSource;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Supplier;

public class JAMDRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.Keys.BLOCKS, JAMD.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, JAMD.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, JAMD.MOD_ID);

    private static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registry.CHUNK_GENERATOR_REGISTRY, JAMD.MOD_ID);

    private static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.Keys.BIOMES, JAMD.MOD_ID);

    private static final DeferredRegister<DimensionType> DIMENSION_TYPE = DeferredRegister.create(Registry.DIMENSION_TYPE_REGISTRY, JAMD.MOD_ID);

    public static final List<DeferredRegister<?>> REGISTRIES = Arrays.asList(BLOCKS, ITEMS, TILES, CHUNK_GENERATORS, BIOMES, DIMENSION_TYPE);

    public static final RegistryObject<PortalBlock> MINE_PORTAL_BLOCK = BLOCKS.register("mine_portal_block", PortalBlock::new);
    public static final RegistryObject<Item> MINE_PORTAL_BLOCK_ITEM = ITEMS.register("mine_portal_block", () -> new BlockItem(MINE_PORTAL_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));
    public static final RegistryObject<BlockEntityType<PortalTileEntity>> PORTAL = TILES.register("portal", () -> BlockEntityType.Builder.of(PortalTileEntity::new, MINE_PORTAL_BLOCK.get()).build(null));

    public static final RegistryObject<Codec<CustomFlatLevelSource>> FLAT_LEVEL_SOURCE = CHUNK_GENERATORS.register(JAMD.DIM_ID.getPath(), () -> CustomFlatLevelSource.CODEC);


    public static final RegistryObject<Biome> BIOME = BIOMES.register(JAMD.DIM_ID.getPath(), () -> {
        BiomeGenerationSettings.Builder settings = new BiomeGenerationSettings.Builder();
        BiomeDefaultFeatures.addDefaultCrystalFormations(settings);
        BiomeDefaultFeatures.addDefaultOres(settings);
        BiomeDefaultFeatures.addExtraEmeralds(settings);
        settings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_TUFF);
        return new Biome.BiomeBuilder()
                .temperature(1)
                .downfall(0.4f)
                .precipitation(Biome.Precipitation.NONE)
                .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(8103167)
                        .fogColor(12638463)
                        .waterColor(4445678)
                        .waterFogColor(270131)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .generationSettings(settings.build())
                .build();
    });


    public static final RegistryObject<DimensionType> TYPE = DIMENSION_TYPE.register(JAMD.DIM_ID.getPath(), () -> new DimensionType(OptionalLong.of(6000),
            true,
            false,
            false,
            true,
            1.0D,
            true,
            false,
            -64,
            384,
            384,
            BlockTags.INFINIBURN_OVERWORLD,
            BuiltinDimensionTypes.OVERWORLD_EFFECTS,
            1.0F,
            new DimensionType.MonsterSettings(false,
                    false,
                    UniformInt.of(0, 7), 0)));

}
