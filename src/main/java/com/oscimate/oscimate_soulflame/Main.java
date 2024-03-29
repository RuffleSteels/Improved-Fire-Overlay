package com.oscimate.oscimate_soulflame;

import com.google.common.base.Suppliers;
import com.oscimate.oscimate_soulflame.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SoulFireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.EntityList;

import java.lang.reflect.Field;
import java.util.function.Supplier;


@Environment(EnvType.CLIENT)
public class Main implements ClientModInitializer {
    public static final String MODID = "oscimate_soulflame";
    public static final ConfigManager CONFIG_MANAGER = new ConfigManager();
    public static double currentFireHeight = 0.0;
    public static final Supplier<Sprite> BLANK_FIRE = Suppliers.memoize(() -> new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("oscimate_soulflame:block/blank_fire_1")).getSprite());

    public int getColorInt(int r, int g, int b) {
        return r << 16 | g << 8 | b;
    }

    static class NewFireTexture extends ForwardingBakedModel {
        NewFireTexture(BakedModel model) {
            wrapped = model;
        }

        @Override
        public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
            super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }
    }


    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.modifyModelAfterBake().register(ModelModifier.WRAP_PHASE, (model, context) -> {
                if (context.id().getPath().contains("block/fire_")) {
                    return new NewFireTexture(model);
//                    return context.loader().getOrLoadModel(new Identifier("block/netherite_block"));
                }
                return model;
            });
        });

//        ModelLoadingRegistry.INSTANCE.registerVariantProvider(manager -> new PillarModelVariantProvider());

//        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.FIRE, CustomRenderLayer.getCustomTint());
//
//        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> {
//            int value = Color.ORANGE.hashCode();
//
//            if(MinecraftClient.getInstance().world.getBiome(pos).getKey().orElseThrow().equals(BiomeKeys.BASALT_DELTAS)) {
//                value = this.getColorInt(39, 40, 48);
//            } else if (MinecraftClient.getInstance().world.getBiome(pos).getKey().orElseThrow().equals(BiomeKeys.CRIMSON_FOREST)) {
//                value = this.getColorInt(255, 0, 0);
//            } else if (MinecraftClient.getInstance().world.getBiome(pos).getKey().orElseThrow().equals(BiomeKeys.WARPED_FOREST)) {
//                value = this.getColorInt(0, 255, 0);
//            }
//
//            return value;
//        }), Blocks.FIRE);

        if(!CONFIG_MANAGER.fileExists()) {
            CONFIG_MANAGER.save();
        }
        CONFIG_MANAGER.getStartupConfig();
//        System.out.println(CONFIG_MANAGER.getCurrentFireHeightSlider() + " -- " + CONFIG_MANAGER.getCurrentFireLogic());
    }
}