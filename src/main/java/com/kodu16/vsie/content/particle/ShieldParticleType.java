package com.kodu16.vsie.content.particle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class ShieldParticleType extends ParticleType<ShieldParticleOptions> {

    public ShieldParticleType(boolean overrideLimiter) {
        // 必须同时传入 overrideLimiter 和 你的 Deserializer
        super(overrideLimiter, ShieldParticleOptions.DESERIALIZER);
    }

    @Override
    public Codec<ShieldParticleOptions> codec() {
        return ShieldParticleOptions.CODEC;
    }

    // getDeserializer() 已经由 super 调用了 DESERIALIZER，这里可以不重写
    // 但如果你想保留接口完整，也可以保留
    @Override
    public ParticleOptions.Deserializer<ShieldParticleOptions> getDeserializer() {
        return ShieldParticleOptions.DESERIALIZER;
    }
}

