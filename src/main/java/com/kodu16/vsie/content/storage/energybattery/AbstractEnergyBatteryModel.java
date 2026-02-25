package com.kodu16.vsie.content.storage.energybattery;

import com.kodu16.vsie.vsie;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

@SuppressWarnings({"removal"})
public class AbstractEnergyBatteryModel extends DefaultedBlockGeoModel<AbstractEnergyBatteryBlockEntity> {
    public AbstractEnergyBatteryModel() {
        super(new ResourceLocation(vsie.ID,"control_seat"));
    }

    @Override
    public ResourceLocation getModelResource(AbstractEnergyBatteryBlockEntity be) {
        return switch (be.getEnergyBatterytype()) {
            case "small" -> new ResourceLocation(vsie.ID, "geo/block/small_energy_battery.geo.json");
            case "medium" -> new ResourceLocation(vsie.ID, "geo/block/medium_energy_battery.geo.json");
            case "large" -> new ResourceLocation(vsie.ID, "geo/block/large_energy_battery.geo.json");
            default -> throw new IllegalStateException("Unexpected value for EnergyBattery");
        };
    }

    @Override
    public ResourceLocation getTextureResource(AbstractEnergyBatteryBlockEntity be) {
        return switch (be.getEnergyBatterytype()) {
            case "small" -> new ResourceLocation(vsie.ID, "textures/block/small_energy_battery.png");
            case "medium" -> new ResourceLocation(vsie.ID, "textures/block/medium_energy_battery.png");
            case "large" -> new ResourceLocation(vsie.ID, "textures/block/large_energy_battery.png");
            default -> throw new IllegalStateException("Unexpected value for EnergyBattery");
        };
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractEnergyBatteryBlockEntity be) {
        return switch (be.getEnergyBatterytype()) {
            case "small" -> new ResourceLocation(vsie.ID, "animations/block/small_energy_battery_anim.json");
            case "medium" -> new ResourceLocation(vsie.ID, "animations/block/medium_energy_battery_anim.json");
            case "large" -> new ResourceLocation(vsie.ID, "animations/block/large_energy_battery_anim.json");
            default -> throw new IllegalStateException("Unexpected value for EnergyBattery");
        };
    }
}
