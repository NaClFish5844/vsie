package com.kodu16.vsie.content.particle;

import com.kodu16.vsie.registries.ModParticleTypes;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class ShieldParticleOptions implements ParticleOptions {

    public static final Codec<ShieldParticleOptions> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("life_offset").forGetter(opt -> opt.lifeOffset)
            ).apply(instance, ShieldParticleOptions::new)
    );

    public static final ParticleOptions.Deserializer<ShieldParticleOptions> DESERIALIZER =
            new ParticleOptions.Deserializer<>() {
                @Override
                public ShieldParticleOptions fromCommand(ParticleType<ShieldParticleOptions> type, StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    int offset = reader.readInt();
                    return new ShieldParticleOptions(offset);
                }

                @Override
                public ShieldParticleOptions fromNetwork(ParticleType<ShieldParticleOptions> type, FriendlyByteBuf buf) {
                    return new ShieldParticleOptions(buf.readInt());
                }
            };

    private final int lifeOffset;

    public ShieldParticleOptions(int lifeOffset) {
        this.lifeOffset = Math.max(0, lifeOffset); // 防止负数
    }

    public int getLifeOffset() {
        return lifeOffset;
    }

    @Override
    public ParticleType<ShieldParticleOptions> getType() {
        return ModParticleTypes.SHIELD.get(); // ← 稍后注册的那个
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(lifeOffset);
    }

    @Override
    public String writeToString() {
        return String.valueOf(lifeOffset);
    }
}
