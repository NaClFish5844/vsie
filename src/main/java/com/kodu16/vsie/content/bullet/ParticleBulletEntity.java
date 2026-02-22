package com.kodu16.vsie.content.bullet;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ParticleBulletEntity extends AbstractBulletEntity{

    public ParticleBulletEntity(EntityType<? extends AbstractBulletEntity> type, Level pLevel) {
        super(type, pLevel);
    }
}
