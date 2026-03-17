package com.kodu16.vsie.content.misc.electromagnet_rail.core;

import com.kodu16.vsie.content.misc.electromagnet_rail.top.ElectroMagnetRailTopBlock;
import com.kodu16.vsie.registries.vsieBlocks;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class ElectroMagnetRailCoreBlockEntity extends SmartBlockEntity implements MenuProvider, IItemHandlerModifiable, GeoBlockEntity {
    // 扫描结果状态码：用于同步到 GUI 并显示检测文案。
    public static final int TERMINAL_STATUS_IDLE = 0;
    public static final int TERMINAL_STATUS_FOUND = 1;
    public static final int TERMINAL_STATUS_FACING_ERROR = 2;
    public static final int TERMINAL_STATUS_NOT_FOUND = 3;
    public static final int TERMINAL_STATUS_BLOCKED = 4;

    // 核心仓仅有 4 个槽位，且只允许放入 electromagnet_rail 方块物品。
    public final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final ItemStackHandler inventory = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.is(vsieBlocks.ELECTRO_MAGNET_RAIL_BLOCK.asItem());
        }
    };

    private LazyOptional<IItemHandlerModifiable> itemHandlerCap = LazyOptional.of(() -> this);
    // 记录最近一次“终端检测”结果，供容器菜单同步给客户端 GUI。
    private int terminalStatus = TERMINAL_STATUS_IDLE;
    private BlockPos terminalPos = BlockPos.ZERO;

    public ElectroMagnetRailCoreBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {

    }

    public void tick(){

    }

    // 提供给 GUI 与红石比较器读取：统计仓内 rail 总数。
    public int getStoredRailCount() {
        int total = 0;
        for (int i = 0; i < inventory.getSlots(); i++) {
            total += inventory.getStackInSlot(i).getCount();
        }
        return total;
    }

    // 执行终端检测：沿核心朝向在 rail 数量范围内从近到远查找可到达的 top。
    public void detectTerminal() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }

        Direction facing = this.getBlockState().getValue(ElectroMagnetRailCoreBlock.FACING);
        int maxDistance = this.getStoredRailCount();

        this.terminalStatus = TERMINAL_STATUS_NOT_FOUND;
        this.terminalPos = BlockPos.ZERO;

        for (int step = 1; step <= maxDistance; step++) {
            BlockPos checkPos = this.worldPosition.relative(facing, step);
            BlockState checkState = this.level.getBlockState(checkPos);

            if (checkState.is(vsieBlocks.ELECTRO_MAGNET_RAIL_TOP_BLOCK.get())) {
                Direction topFacing = checkState.getValue(ElectroMagnetRailTopBlock.FACING);
                if (topFacing == facing) {
                    // 找到合法终端：记录坐标用于 GUI 展示。
                    this.terminalStatus = TERMINAL_STATUS_FOUND;
                    this.terminalPos = checkPos;
                } else {
                    // 找到终端但朝向错误。
                    this.terminalStatus = TERMINAL_STATUS_FACING_ERROR;
                    this.terminalPos = checkPos;
                }
                this.setChanged();
                this.sendData();
                return;
            }

            if (!checkState.isAir() && !checkState.is(vsieBlocks.ELECTRO_MAGNET_RAIL_BLOCK.get())) {
                // 核心与终端之间出现非 rail 的障碍方块，判定为阻挡。
                this.terminalStatus = TERMINAL_STATUS_BLOCKED;
                this.terminalPos = checkPos;
                this.setChanged();
                this.sendData();
                return;
            }
        }

        // 范围内未找到终端。
        this.setChanged();
        this.sendData();
    }

    // 提供容器菜单读取检测状态。
    public int getTerminalStatus() {
        return terminalStatus;
    }

    // 提供容器菜单读取检测终端坐标。
    public BlockPos getTerminalPos() {
        return terminalPos;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.vsie.electro_magnet_rail_core");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ElectroMagnetRailCoreContainerMenu(containerId, playerInventory, this);
    }

    @Override
    public void read(CompoundTag tag, boolean clientpacket) {
        super.read(tag,clientpacket);
        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(tag.getCompound("Inventory"));
        }
        // 同步最近一次终端检测结果，保证 GUI 重开后仍能展示。
        if (tag.contains("TerminalStatus")) {
            this.terminalStatus = tag.getInt("TerminalStatus");
        }
        if (tag.contains("TerminalPos")) {
            this.terminalPos = BlockPos.of(tag.getLong("TerminalPos"));
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientpacket) {
        super.write(tag,clientpacket);
        tag.put("Inventory", inventory.serializeNBT());
        // 持久化并同步终端检测状态与坐标。
        tag.putInt("TerminalStatus", this.terminalStatus);
        tag.putLong("TerminalPos", this.terminalPos.asLong());
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


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandlerCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerCap.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandlerCap = LazyOptional.of(() -> this);
    }

    // IItemHandlerModifiable 接口转发到内部 ItemStackHandler。
    @Override
    public int getSlots() {
        return inventory.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return inventory.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return inventory.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return inventory.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return inventory.isItemValid(slot, stack);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
        setChanged();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
