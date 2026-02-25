package com.kodu16.vsie.content.storage.energybattery.block;

import com.kodu16.vsie.content.storage.energybattery.AbstractEnergyBatteryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SmallEnergyBatteryBlockEntity extends AbstractEnergyBatteryBlockEntity {
    public SmallEnergyBatteryBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public int getcapacity() {
        return 100000;
    }

    @Override
    public String getEnergyBatterytype() {
        return "small";
    }
}
