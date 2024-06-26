package com.oscimate.oscimate_soulflame;

import com.google.common.base.Suppliers;
import com.oscimate.oscimate_soulflame.config.ConfigManager;
import com.oscimate.oscimate_soulflame.test.TestModel;
import it.unimi.dsi.fastutil.Hash;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

import static com.oscimate.oscimate_soulflame.CustomRenderLayer.getCustomTint;


@Environment(EnvType.CLIENT)
public class Main implements ClientModInitializer {
    public static final String MODID = "oscimate_soulflame";
    public static final ConfigManager CONFIG_MANAGER = new ConfigManager();
    public static double currentFireHeight = 0.0;
    public static final Supplier<Sprite> BLANK_FIRE_0 = Suppliers.memoize(() -> new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("oscimate_soulflame:block/blank_fire_0")).getSprite());
    public static final Supplier<Sprite> BLANK_FIRE_0_OVERLAY = Suppliers.memoize(() -> new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("oscimate_soulflame:block/blank_fire_overlay_0")).getSprite());
    public static final Supplier<Sprite> SOUL_FIRE_1 = Suppliers.memoize(() -> new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("block/soul_fire_1")).getSprite());
    public static final Supplier<Sprite> SOUL_FIRE_0 = Suppliers.memoize(() -> new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("block/soul_fire_0")).getSprite());
    public static final Supplier<Sprite> BLANK_FIRE_1 = Suppliers.memoize(() -> new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("oscimate_soulflame:block/blank_fire_1")).getSprite());
    public static final Supplier<Sprite> BLANK_FIRE_1_OVERLAY = Suppliers.memoize(() -> new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("oscimate_soulflame:block/blank_fire_overlay_1")).getSprite());

    public static void settingFireColor(Entity entity) {
        Box box = entity.getBoundingBox();
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.minY);
        int l = MathHelper.ceil(box.maxY);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        boolean bl2 = false;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int p = i; p < j; ++p) {
            for (int q = k; q < l; ++q) {
                for (int r = m; r < n; ++r) {
                    double e;
                    mutable.set(p, q, r);
                    Block block = entity.getWorld().getBlockState(mutable).getBlock();
                    Block blockUnder = entity.getWorld().getBlockState(mutable.down()).getBlock();
                    if (!((float)q + 1f >= box.minY)) continue;
                    if (!entity.isInLava()) {
                        if (Main.CONFIG_MANAGER.getCurrentBlockFireColors().containsKey(blockUnder.getTranslationKey()) && block instanceof FireBlock) {
                            ((RenderFireColorAccessor) entity).setRenderFireColor(Main.CONFIG_MANAGER.getCurrentBlockFireColors().get(blockUnder.getTranslationKey()));
                        } else {
                            if (block instanceof SoulFireBlock) {
                                ((RenderFireColorAccessor) entity).setRenderFireColor(new int[]{1});
                            } else if (block instanceof FireBlock){
                                ((RenderFireColorAccessor) entity).setRenderFireColor(new int[]{2});
                            } else {
                                if(((RenderFireColorAccessor) entity).getRenderFireColor() == null) {
                                    ((RenderFireColorAccessor) entity).setRenderFireColor(new int[]{2});
                                }
                            }
                        }
                    } else {
                        ((RenderFireColorAccessor) entity).setRenderFireColor(new int[]{2});
                    }
                }
            }
        }
    }
    public int getColorInt(int r, int g, int b) {
        return r << 16 | g << 8 | b;
    }
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.FIRE, getCustomTint());
        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.modifyModelAfterBake().register(ModelModifier.WRAP_PHASE, (model, context) -> {
                if (context.id().getPath().contains("block/fire_side") || context.id().getPath().contains("block/fire_floor") || context.id().getPath().contains("block/fire_up") ) {
                    return new TestModel(model);
                }
                return model;
            });
        });
        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> {
            if (world.hasBiomes()) {
                HashMap<String, int[]> map = CONFIG_MANAGER.getCurrentBlockFireColors();
                String blockUnder = world.getBlockState(pos.down()).getBlock().getTranslationKey();
                if (map.containsKey(blockUnder)) {
                    int[] colors = map.get(blockUnder).clone();
                    if (tintIndex == 1) {
                        return colors[0];
                    }
                    if (tintIndex == 2) {
                        return colors[1];
                    }
                }
//                RegistryKey<Biome> biome = world.getBiomeFabric(pos).getKey().orElseThrow();
//                if (biome.equals(BiomeKeys.WARPED_FOREST)) {
//                    if (tintIndex == 1) {
//                        return this.getColorInt(0, 255, 200);
//                    }
//                    if (tintIndex == 2) {
//                        return this.getColorInt(195, 255, 0);
//                    }
//                }
//                else if (biome.equals(BiomeKeys.BASALT_DELTAS)) {
//                    if (tintIndex == 1) {
//                        return this.getColorInt(23, 68, 0);
//                    }
//                    if (tintIndex == 2) {
//                        return this.getColorInt(45, 95, 0);
//                    }
//                } else {
//                    return this.getColorInt(0, 0, 0);
//                }
            }
            return this.getColorInt(0, 0, 0);
        }), Blocks.FIRE);

        if(!CONFIG_MANAGER.fileExists()) {
            CONFIG_MANAGER.save();
        }
        CONFIG_MANAGER.getStartupConfig();
    }
}