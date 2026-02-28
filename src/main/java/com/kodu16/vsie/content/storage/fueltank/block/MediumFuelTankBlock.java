package com.kodu16.vsie.content.storage.fueltank.block;

import com.kodu16.vsie.content.storage.fueltank.AbstractFuelTankBlock;
import com.kodu16.vsie.registries.vsieBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MediumFuelTankBlock extends AbstractFuelTankBlock {
    public MediumFuelTankBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MediumFuelTankBlockEntity(vsieBlockEntities.MEDIUM_FUELTANK_BLOCK_ENTITY.get(), pos,state);
    }
}
