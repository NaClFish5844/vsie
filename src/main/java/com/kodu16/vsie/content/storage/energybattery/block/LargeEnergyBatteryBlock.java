package com.kodu16.vsie.content.storage.energybattery.block;

import com.kodu16.vsie.content.storage.energybattery.AbstractEnergyBatteryBlock;
import com.kodu16.vsie.registries.vsieBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LargeEnergyBatteryBlock extends AbstractEnergyBatteryBlock {
    public LargeEnergyBatteryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LargeEnergyBatteryBlockEntity(vsieBlockEntities.LARGE_ENERGY_BATTERY_BLOCK_ENTITY.get(), pos,state);
    }
}
