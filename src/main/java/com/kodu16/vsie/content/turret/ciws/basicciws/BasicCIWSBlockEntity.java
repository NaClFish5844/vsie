package com.kodu16.vsie.content.turret.ciws.basicciws;

import com.kodu16.vsie.content.turret.ciws.AbstractCIWSBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;

import java.util.List;

public class BasicCIWSBlockEntity extends AbstractCIWSBlockEntity {
    public BasicCIWSBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public Vector3d getShootLocation(Vector3d vec, List<Vector3d> preV, Level lv, Vector3d pos) {
        return pos;
    }

    @Override
    public String getturrettype() {
        return "basic_ciws";
    }

    @Override
    public double getYAxisOffset() {
        return 1.5;
    }

    @Override
    public float getMaxSpinSpeed() {
        return Mth.PI/8;
    }

    @Override
    public int getCoolDown() {
        return 1;
    }

    @Override
    public int getenergypertick() {
        return 100;
    }

    @Override
    public void shootentity() {

    }

    @Override
    public void shootship() {

    }

    @Override
    public void interceptprojectile() {

    }
}
