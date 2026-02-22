package com.kodu16.vsie.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.phys.Vec3;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

@Mixin(ClientboundSetEntityMotionPacket.class)
public class FastProjectionFixMixin {
    @Shadow
    @Final
    @Mutable
    private int id;

    @Shadow
    @Final
    @Mutable
    private int xa;

    @Shadow
    @Final
    @Mutable
    private int ya;

    @Shadow
    @Final
    @Mutable
    private int za;

    // Constructor from Vector
    @Inject(method = "<init>(ILnet/minecraft/world/phys/Vec3;)V", at = @At(value = "RETURN"), cancellable = true)
    public void ClientboundSetEntityMotionPacket(int entityId, Vec3 motionVector, CallbackInfo ci) {
        this.id = entityId;
        this.xa = (int) (motionVector.x * 8000.0D);
        this.ya = (int) (motionVector.y * 8000.0D);
        this.za = (int) (motionVector.z * 8000.0D);
    }

    // Constructor from Packet
    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At(value = "RETURN"), cancellable = true)
    public void ClientboundSetEntityMotionPacket(FriendlyByteBuf buf, CallbackInfo ci) {
        this.id = buf.readVarInt();
        this.xa = buf.readInt();
        this.ya = buf.readInt();
        this.za = buf.readInt();
    }

    // Write Packet
    @Inject(method = "write", at = @At(value = "HEAD"), cancellable = true)
    public void readPacketData(FriendlyByteBuf buf, CallbackInfo ci) {
        ci.cancel();
        buf.writeVarInt(this.id);
        buf.writeInt(this.xa);
        buf.writeInt(this.ya);
        buf.writeInt(this.za);
    }
}