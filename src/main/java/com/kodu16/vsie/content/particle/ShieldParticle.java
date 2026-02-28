package com.kodu16.vsie.content.particle;

import com.mojang.logging.LogUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class ShieldParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final int startFrameTick;  // ← 改名更清晰

    public ShieldParticle(ClientLevel level, double x, double y, double z,
                          double xSpeed, double ySpeed, double zSpeed,
                          ShieldParticleOptions options,   // ← 改这里
                          SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.spriteSet = spriteSet;

        this.startFrameTick = options.getLifeOffset();

        this.quadSize = 0.03F;
        this.lifetime = this.startFrameTick + 3;
        this.setColor(1.0F, 1.0F, 1.0F);
        this.gravity = 0;
        this.hasPhysics = false;
        this.friction = 1.0F;

        LogUtils.getLogger().warn("particle:startFrameTick:{}", this.startFrameTick);

        this.setSprite(spriteSet.get(0, 4));
    }

    @Override
    public void tick() {
        this.setParticleSpeed(0,0,0);
        this.setSize(0.03F,0.03F);
        super.tick();
        // 根据 age 和延迟参数决定使用哪一帧
        int frameIndex;
        if (this.age < startFrameTick) {
            frameIndex = 0;                      // 延迟阶段：固定第0张
        } else {
            int elapsed = this.age - startFrameTick;
            int phase = elapsed / 2;             // 每3tick切换一次（可调）
            frameIndex = 1 + phase;              // 从第1张开始递增
            // 限制最大帧（防止超出spriteset范围）
            frameIndex = Math.min(frameIndex, 4);
        }

        this.setSprite(spriteSet.get(frameIndex, 4));

        // 尺寸脉动（你原来的逻辑，稍作微调让它更平滑）
        this.quadSize = 0.08F + 0.10F * Mth.sqrt((float) this.age / this.lifetime);

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;  // 0xF000F0 全亮（RGB 全 15，天空光/方块光全开）
    }

    // ────────────────────────────────────────────────
    //                  工厂 - 支持传入延迟参数
    // ────────────────────────────────────────────────
    public static class Provider implements ParticleProvider<ShieldParticleOptions> {  // ← 注意泛型变了

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(ShieldParticleOptions options,  // ← 改成 ShieldParticleOptions
                                       ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new ShieldParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,
                    options, this.spriteSet);
        }
    }
}
