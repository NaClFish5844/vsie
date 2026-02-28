package com.kodu16.vsie.content.storage.fueltank;

import com.kodu16.vsie.content.storage.energybattery.AbstractEnergyBatteryBlockEntity;
import com.kodu16.vsie.vsie;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

@SuppressWarnings({"removal"})
public class AbstractFuelTankModel extends DefaultedBlockGeoModel<AbstractFuelTankBlockEntity> {
    public AbstractFuelTankModel() {
        super(new ResourceLocation(vsie.ID,"fueltank"));
    }

    @Override
    public ResourceLocation getModelResource(AbstractFuelTankBlockEntity be) {
        return switch (be.getFuelTanktype()) {
            case "small" -> new ResourceLocation(vsie.ID, "geo/block/small_fueltank.geo.json");
            case "medium" -> new ResourceLocation(vsie.ID, "geo/block/medium_fueltank.geo.json");
            case "large" -> new ResourceLocation(vsie.ID, "geo/block/large_fueltank.geo.json");
            default -> throw new IllegalStateException("Unexpected value for fueltank");
        };
    }

    @Override
    public ResourceLocation getTextureResource(AbstractFuelTankBlockEntity be) {
        return switch (be.getFuelTanktype()) {
            case "small" -> new ResourceLocation(vsie.ID, "textures/block/small_fueltank.png");
            case "medium" -> new ResourceLocation(vsie.ID, "textures/block/medium_fueltank.png");
            case "large" -> new ResourceLocation(vsie.ID, "textures/block/large_fueltank.png");
            default -> throw new IllegalStateException("Unexpected value for fueltank");
        };
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractFuelTankBlockEntity be) {
        return switch (be.getFuelTanktype()) {
            case "small" -> new ResourceLocation(vsie.ID, "animations/block/small_fueltank_anim.json");
            case "medium" -> new ResourceLocation(vsie.ID, "animations/block/medium_fueltank_anim.json");
            case "large" -> new ResourceLocation(vsie.ID, "animations/block/large_fueltank_anim.json");
            default -> throw new IllegalStateException("Unexpected value for fueltank");
        };
    }
}
