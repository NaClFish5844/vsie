package com.kodu16.vsie.content.storage.fueltank;

import com.kodu16.vsie.content.storage.energybattery.AbstractEnergyBatteryBlockEntity;
import com.kodu16.vsie.content.storage.energybattery.AbstractEnergyBatteryModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AbstractFuelTankGeoRenderer extends GeoBlockRenderer<AbstractFuelTankBlockEntity> {
    public AbstractFuelTankGeoRenderer(BlockEntityRendererProvider.Context context) {
        super(new AbstractFuelTankModel());
    }
    @Override
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
        switch (facing) {
            case SOUTH -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(-90));
            }
            case WEST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
            }
            case NORTH -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
            }
            case EAST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(0));
            }
        }
    }
}
