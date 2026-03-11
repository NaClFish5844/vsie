package com.kodu16.vsie.content.bullet.entity;

import com.kodu16.vsie.content.bullet.AbstractBulletEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PlasmaBulletEntity extends AbstractBulletEntity {

    public PlasmaBulletEntity(EntityType<? extends AbstractBulletEntity> type, Level pLevel) {
        super(type, pLevel);
    }
    }
