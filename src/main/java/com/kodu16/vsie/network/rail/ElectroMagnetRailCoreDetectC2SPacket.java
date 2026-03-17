package com.kodu16.vsie.network.rail;

import com.kodu16.vsie.content.misc.electromagnet_rail.core.ElectroMagnetRailCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ElectroMagnetRailCoreDetectC2SPacket {
    private final BlockPos pos;

    public ElectroMagnetRailCoreDetectC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(ElectroMagnetRailCoreDetectC2SPacket pkt, FriendlyByteBuf buf) {
        buf.writeBlockPos(pkt.pos);
    }

    public static ElectroMagnetRailCoreDetectC2SPacket decode(FriendlyByteBuf buf) {
        return new ElectroMagnetRailCoreDetectC2SPacket(buf.readBlockPos());
    }

    public static void handle(ElectroMagnetRailCoreDetectC2SPacket pkt, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        // 在服务端主线程执行检测，保证世界读写安全。
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (sender == null) {
                return;
            }

            ServerLevel level = sender.serverLevel();
            BlockEntity be = level.getBlockEntity(pkt.pos);
            if (be instanceof ElectroMagnetRailCoreBlockEntity core) {
                // 按钮触发核心执行“终端扫描”逻辑。
                core.detectTerminal();
            }
        });
        ctx.setPacketHandled(true);
    }
}
