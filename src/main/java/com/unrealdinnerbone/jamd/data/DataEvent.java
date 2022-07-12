package com.unrealdinnerbone.jamd.data;

import com.mojang.datafixers.util.Pair;
import com.unrealdinnerbone.jamd.JAMD;
import com.unrealdinnerbone.jamd.JAMDRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class DataEvent {

    public static void onData(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new Recipe(event.getGenerator()));
        event.getGenerator().addProvider(true, new BlockState(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new Item(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new LootTable(event.getGenerator()));
        event.getGenerator().addProvider(true, new Tag(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new CodecTypedGenerator<>(event.getGenerator(), JAMD.MOD_ID, ForgeRegistries.Keys.BIOMES, Biome.DIRECT_CODEC));
        event.getGenerator().addProvider(true, new CodecTypedGenerator<>(event.getGenerator(), JAMD.MOD_ID, Registry.DIMENSION_TYPE_REGISTRY, DimensionType.DIRECT_CODEC));
    }

    public static class Tag extends BlockTagsProvider {

        public Tag(DataGenerator dataGenerator, ExistingFileHelper fileHelper) {
            super(dataGenerator, JAMD.MOD_ID, fileHelper);
        }

        @Override
        protected void addTags() {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(JAMDRegistry.MINE_PORTAL_BLOCK.get());
            tag(BlockTags.NEEDS_DIAMOND_TOOL).add(JAMDRegistry.MINE_PORTAL_BLOCK.get());
        }
    }

    public static class LootTable extends LootTableProvider {

        public LootTable(DataGenerator gen) {
            super(gen);
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, net.minecraft.world.level.storage.loot.LootTable.Builder>>>, LootContextParamSet>> getTables() {
            return Collections.singletonList(Pair.of(BlockLootTable::new, LootContextParamSets.BLOCK));
        }

        public static class BlockLootTable extends BlockLoot {

            @Override
            public void addTables() {
                dropSelf(JAMDRegistry.MINE_PORTAL_BLOCK.get());
            }

            protected Iterable<Block> getKnownBlocks() {
                return Collections.singleton(JAMDRegistry.MINE_PORTAL_BLOCK.get());
            }


        }

        @Override
        protected void validate(Map<ResourceLocation, net.minecraft.world.level.storage.loot.LootTable> map, ValidationContext validationtracker) {

        }
    }

    public static class Item extends net.minecraftforge.client.model.generators.ItemModelProvider {

        public Item(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, JAMD.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(JAMDRegistry.MINE_PORTAL_BLOCK.get());
            cubeAll(id.getPath(),new ResourceLocation(JAMD.MOD_ID, "block/mine_portal_block"));
        }

        public void itemGenerated(net.minecraft.world.item.Item item, ResourceLocation texture) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            getBuilder(id.getPath()).parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", texture);
        }

    }

    public static class BlockState extends BlockStateProvider {

        public BlockState(DataGenerator gen, ExistingFileHelper exFileHelper) {
            super(gen, JAMD.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            simpleBlock(JAMDRegistry.MINE_PORTAL_BLOCK.get());
        }
    }

    public static class Recipe extends RecipeProvider {

        Recipe(DataGenerator generatorIn) {
            super(generatorIn);
        }


        @Override
        protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
            ShapedRecipeBuilder.shaped(JAMDRegistry.MINE_PORTAL_BLOCK_ITEM::get)
                    .pattern("OOO")
                    .pattern("OEO")
                    .pattern("OOO")
                    .define('O', Tags.Items.OBSIDIAN)
                    .define('E', Items.DIAMOND_PICKAXE)
                    .unlockedBy("has_pick", has(Items.DIAMOND_PICKAXE))
                    .unlockedBy("has_obsidian", has(Items.OBSIDIAN))
                    .save(consumer);
        }

    }
}
