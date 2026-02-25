package com.kodu16.vsie.content.storage.energybattery.block;

import com.kodu16.vsie.content.storage.energybattery.AbstractEnergyBatteryBlock;
import com.kodu16.vsie.registries.vsieBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class SmallEnergyBatteryBlock extends AbstractEnergyBatteryBlock {
    public SmallEnergyBatteryBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SmallEnergyBatteryBlockEntity(vsieBlockEntities.SMALL_ENERGY_BATTERY_BLOCK_ENTITY.get(), pos,state);
    }
}
