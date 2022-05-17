package com.oscimate.oscimate_soulflame;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

@Environment(EnvType.CLIENT)
public class Main implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            if(client.world != null) {
                client.world.getEntities().forEach(entity -> {

                    if (entity.isInLava()) {
                        ((OnSoulFireAccessor) entity).setRenderSoulFire(false);
                        return;
                    }


                    Box box = entity.getBoundingBox();
                    BlockPos.Mutable mutable = new BlockPos.Mutable();
                    BlockPos blockPos = new BlockPos(box.minX + 0.001D, box.minY + 0.001D, box.minZ + 0.001D);
                    BlockPos blockPos2 = new BlockPos(box.maxX - 0.001D, box.maxY - 0.001D, box.maxZ - 0.001D);

                    for(int i = blockPos.getX(); i <= blockPos2.getX(); ++i) {
                        for (int j = blockPos.getY(); j <= blockPos2.getY(); ++j) {
                            for (int k = blockPos.getZ(); k <= blockPos2.getZ(); ++k) {
                                mutable.set(i, j, k);
                                if (client.world.getBlockState(mutable).getBlock() == Blocks.SOUL_FIRE) {
                                    ((OnSoulFireAccessor) entity).setRenderSoulFire(true);
                                    return;
                                }
                                if (client.world.getBlockState(mutable).getBlock() == Blocks.FIRE) {
                                    ((OnSoulFireAccessor) entity).setRenderSoulFire(false);
                                    return;
                                }
                            }
                        }
                    }

                });
            }
        });
    }
}