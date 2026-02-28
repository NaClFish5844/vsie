package com.kodu16.vsie.registries;

import com.kodu16.vsie.content.particle.ShieldParticleOptions;
import com.kodu16.vsie.content.particle.ShieldParticleType;
import com.kodu16.vsie.vsie;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// ModParticleTypes.java 保持不变（你的代码已经是对的）
public class ModParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, vsie.ID);

    public static final RegistryObject<ParticleType<ShieldParticleOptions>> SHIELD =
            PARTICLES.register("shield",
                    () -> new ShieldParticleType(true));   // true = override limiter

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }
}

