package com.code.tama.mtm.server.worlds;

import com.code.tama.mtm.server.registries.MTMBlocks;
import com.code.tama.mtm.server.worlds.tree.custom.GallifreyanFoliagePlacer;
import com.code.tama.mtm.server.worlds.tree.custom.GallifreyanOakTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

import static com.code.tama.mtm.MTMMod.MODID;

public class MConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ZEITON_ORE_KEY = registerKey("zeiton_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_ZEITON_ORE_KEY = registerKey("nether_zeiton_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> END_ZEITON_ORE_KEY = registerKey("end_zeiton_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GALLIFREYAN_OAK_KEY = registerKey("gallifreyan_oak");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplacables = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> overworldSapphireOres = List.of(OreConfiguration.target(stoneReplaceable,
                MTMBlocks.ZEITON_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, MTMBlocks.DEEPSLATE_ZEITON_ORE.get().defaultBlockState()));

        register(context, OVERWORLD_ZEITON_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSapphireOres, 9));
        register(context, NETHER_ZEITON_ORE_KEY, Feature.ORE, new OreConfiguration(netherrackReplacables,
                MTMBlocks.NETHER_ZEITON_ORE.get().defaultBlockState(), 9));
        register(context, END_ZEITON_ORE_KEY, Feature.ORE, new OreConfiguration(endReplaceables,
                MTMBlocks.END_STONE_ZEITON_ORE.get().defaultBlockState(), 9));

        register(context, GALLIFREYAN_OAK_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(MTMBlocks.GALLIFREYAN_OAK_LOG.get()),
                new GallifreyanOakTrunkPlacer(5, 0, 0),

                BlockStateProvider.simple(MTMBlocks.GALLIFREYAN_OAK_LEAVES.get()),
                new GallifreyanFoliagePlacer(ConstantInt.of(3), ConstantInt.of(2), 3),

                new TwoLayersFeatureSize(1, 0, 2)).build());
    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}