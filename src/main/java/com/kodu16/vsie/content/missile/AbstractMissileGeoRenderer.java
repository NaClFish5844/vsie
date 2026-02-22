package com.kodu16.vsie.content.missile;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AbstractMissileGeoRenderer extends GeoEntityRenderer<AbstractMissileEntity> {
    public AbstractMissileGeoRenderer(EntityRendererProvider.Context context){
        super(context, new AbstractMissileModel());
    }
}
