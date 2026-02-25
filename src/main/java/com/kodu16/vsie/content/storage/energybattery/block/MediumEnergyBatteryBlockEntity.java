package com.kodu16.vsie.content.storage.energybattery.block;

import com.kodu16.vsie.content.storage.energybattery.AbstractEnergyBatteryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MediumEnergyBatteryBlockEntity extends AbstractEnergyBatteryBlockEntity {
    public MediumEnergyBatteryBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public int getcapacity() {
        return 1000000;
    }

    @Override
    public String getEnergyBatterytype() {
        return "medium";
    }
}
