package com.kodu16.vsie.content.heavyturret.heavyelectromagnetturret;

import com.kodu16.vsie.content.heavyturret.AbstractHeavyTurretBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;

import java.util.List;

public class HeavyElectroMagnetTurretBlockEntity extends AbstractHeavyTurretBlockEntity {
    public HeavyElectroMagnetTurretBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public Vector3d getShootLocation(Vector3d vec, List<Vector3d> preV, Level lv, Vector3d pos) {
        return vec;
    }

    @Override
    public String getturrettype() {
        return "heavy_electro_magnet";
    }

    @Override
    public double getYAxisOffset() {
        return 5.5;
    }

    @Override
    public float getMaxSpinSpeed() {
        return Mth.PI/256;
    }

    @Override
    public int getCoolDown() {
        return 120;
    }

    @Override
    public int getenergypertick() {
        return 10000;
    }

    @Override
    public void shootentity() {

    }

    @Override
    public void shootship() {

    }

    @Override
    public int getmaxpitchdowndegrees() {
        return 20;
    }
}
