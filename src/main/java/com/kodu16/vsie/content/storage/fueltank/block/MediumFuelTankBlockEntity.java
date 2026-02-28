package com.kodu16.vsie.content.storage.fueltank.block;

import com.kodu16.vsie.content.storage.fueltank.AbstractFuelTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MediumFuelTankBlockEntity extends AbstractFuelTankBlockEntity {
    public MediumFuelTankBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public int getCapacity() {
        return 1000000;
    }

    @Override
    public String getFuelTanktype() {
        return "medium";
    }
}
