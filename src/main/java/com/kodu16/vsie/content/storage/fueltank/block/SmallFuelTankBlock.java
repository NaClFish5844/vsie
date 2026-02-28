package com.kodu16.vsie.content.storage.fueltank.block;

import com.kodu16.vsie.content.storage.fueltank.AbstractFuelTankBlock;
import com.kodu16.vsie.registries.vsieBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SmallFuelTankBlock extends AbstractFuelTankBlock {
    public SmallFuelTankBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SmallFuelTankBlockEntity(vsieBlockEntities.SMALL_FUELTANK_BLOCK_ENTITY.get(), pos,state);
    }
}
