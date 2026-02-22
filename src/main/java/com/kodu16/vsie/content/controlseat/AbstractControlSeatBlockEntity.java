package com.kodu16.vsie.content.controlseat;


import com.kodu16.vsie.content.controlseat.server.ControlSeatServerData;
import com.kodu16.vsie.content.weapon.AbstractWeaponBlockEntity;
import com.mojang.logging.LogUtils;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"deprecation", "unchecked"})
public abstract class AbstractControlSeatBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, GeoBlockEntity {

    // Common State
    protected ControlSeatServerData controlseatData;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public float calculatedstrength = 0;

    //Links(nbt:true)
    private final List<Vec3> linkedThrusters = new ArrayList<>();
    public final List<Vec3> linkedWeapons = new ArrayList<>();
    private final List<Vec3> linkedShields = new ArrayList<>();
    private final List<Vec3> linkedTurrets = new ArrayList<>();



    public AbstractControlSeatBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        controlseatData = new ControlSeatServerData();
    }


    // 修改 tick 方法，在此方法中确保座椅输入与对应玩家的 UUID 匹配

    protected abstract boolean isWorking();

    public ControlSeatServerData getControlSeatData() {
        return controlseatData;
    }

    // 在移除座椅时清除控制记录
    public abstract void onRemove();

    public abstract String getcontrolseattype();

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        nbt.putString("enemy", controlseatData.enemy);
        nbt.putString("ally", controlseatData.ally);

        writeVec3List(nbt, "Thrusters", linkedThrusters);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if(this.controlseatData == null) {
            this.controlseatData = new ControlSeatServerData();
        }
        if(nbt.contains("enemy")) {this.controlseatData.enemy = nbt.getString("enemy");}
        if(nbt.contains("ally")) {this.controlseatData.ally = nbt.getString("ally");}

        linkedThrusters.clear();
        linkedWeapons.clear();
        linkedShields.clear();
        linkedTurrets.clear();

        readVec3List(nbt, "Thrusters", linkedThrusters);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        write(tag, true);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            handleUpdateTag(tag);
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        read(tag, true);
    }

    public void setEnemy(String str) {controlseatData.enemy = str;}

    public void setAlly(String str) {
        boolean onship = VSGameUtilsKt.isBlockInShipyard(level, getBlockPos());
        if(onship) {
            ServerShip Ship = (ServerShip) VSGameUtilsKt.getShipObjectManagingPos(level, getBlockPos());
            ValkyrienSkiesMod.getVsCore().renameShip(Ship, processSlug(Ship.getSlug(), str));
        }
        controlseatData.ally = str;
    }

    public void addLinkedPeripheral(Vec3 pos, int type) { //0：推进器 1：主武器 2：护盾 3：炮塔，务必不要写错
        Logger LOGGER = LogUtils.getLogger();
        if (type == 0 && !linkedThrusters.contains(pos)) {
            linkedThrusters.add(pos);
            LOGGER.warn("adding thruster to controlseat: " + pos);
            setChanged(); // 标记方块实体脏了，强制保存
        }
        if (type == 1 && !linkedWeapons.contains(pos)) {
            linkedWeapons.add(pos);
            LOGGER.warn("adding thruster to controlseat: " + pos);
            setChanged(); // 标记方块实体脏了，强制保存
        }
    }

    public void removeLinkedPeripheral(Vec3 pos, int type) {
        if (type==0 && linkedThrusters.contains(pos)) {
            linkedThrusters.remove(pos);
            setChanged(); // 标记方块实体脏了，强制保存
        }
        if (type==1 && linkedWeapons.contains(pos)) {
            linkedWeapons.remove(pos);
            setChanged(); // 标记方块实体脏了，强制保存
        }
    }

    public void forEachLinkedPeripheral(Consumer<Vec3> action, int type) { //0：推进器 1：主武器 2：护盾 3：炮塔，务必不要写错
        if(type==0) {
            linkedThrusters.forEach(action);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static String processSlug(String a, String b) {
        // 匹配格式：开头是 [任意内容] + 后面任意字符
        if (a != null && a.matches("^\\[.*?\\].*")) {
            // 找到第一个 ] 的位置
            int endIndex = a.indexOf(']');
            if (endIndex != -1) {
                String suffix = a.substring(endIndex + 1);
                return "[" + b + "]" + suffix;
            }
        }
        // 不符合 [xxx]yyy 格式，或者 a 是 null
        return "[" + b + "]" + a;
    }

    private void writeVec3List(CompoundTag nbt, String key, List<Vec3> positions) {
        ListTag list = new ListTag();
        for (Vec3 vec : positions) {
            CompoundTag vecTag = new CompoundTag();
            vecTag.putDouble("x", vec.x);
            vecTag.putDouble("y", vec.y);
            vecTag.putDouble("z", vec.z);
            list.add(vecTag);
        }
        nbt.put(key, list);
    }

    private void readVec3List(CompoundTag nbt, String key, List<Vec3> targetList) {
        if (!nbt.contains(key, Tag.TAG_LIST)) return;

        ListTag list = nbt.getList(key, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag vecTag = list.getCompound(i);
            double x = vecTag.getDouble("x");
            double y = vecTag.getDouble("y");
            double z = vecTag.getDouble("z");
            targetList.add(new Vec3(x, y, z));
        }
    }

}
