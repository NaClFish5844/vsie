package com.kodu16.vsie.content.misc.electromagnet_rail.top;


import com.kodu16.vsie.vsie;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
@SuppressWarnings("removal")
public class ElectroMagnetRailTopModel extends DefaultedBlockGeoModel<ElectroMagnetRailTopBlockEntity> {
    public ElectroMagnetRailTopModel() {
        super(new ResourceLocation(vsie.ID,"heavy_electromagnet_turret"));
    }
    @Override
    public ResourceLocation getModelResource(ElectroMagnetRailTopBlockEntity Top) {
        return new ResourceLocation(vsie.ID, "geo/block/electro_magnet_rail_top.geo.json");
    }
    public ResourceLocation getTextureResource(ElectroMagnetRailTopBlockEntity Top) {
        return new ResourceLocation(vsie.ID, "textures/block/electro_magnet_rail_top.png");
    }
    public ResourceLocation getAnimationResource(ElectroMagnetRailTopBlockEntity Top) {
        return new ResourceLocation(vsie.ID, "animations/block/electro_magnet_rail_top_anim.json");
    }
}
