package com.kodu16.vsie.content.controlseat.client;

import com.kodu16.vsie.content.controlseat.AbstractControlSeatBlockEntity;
import com.kodu16.vsie.content.turret.client.AbstractTurretModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AbstractControlSeatGeoRenderer extends GeoBlockRenderer<AbstractControlSeatBlockEntity> {
    public AbstractControlSeatGeoRenderer(BlockEntityRendererProvider.Context context) {
        super(new AbstractControlSeatModel());
    }

    @Override
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
        switch (facing) {
            case SOUTH -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(0));
            }
            case WEST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(270));
            }
            case NORTH -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
            }
            case EAST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
            }
        }
    }

    @Override
    public boolean shouldRenderOffScreen(AbstractControlSeatBlockEntity be) {
        return true;   // 或者 return distanceSq < 某个超大值 的平方
    }

    @Override
    public boolean shouldRender(AbstractControlSeatBlockEntity be, Vec3 cameraPos) {
        // 自己写距离判断，比如 256 格以内都渲染
        return be.getBlockPos().distSqr(new Vec3i((int) cameraPos.x, (int) cameraPos.y, (int) cameraPos.z)) < 2048 * 2048;
    }
}
