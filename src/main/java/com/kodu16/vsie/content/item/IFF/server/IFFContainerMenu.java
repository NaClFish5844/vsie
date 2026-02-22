package com.kodu16.vsie.content.item.IFF.server;

import com.kodu16.vsie.registries.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class IFFContainerMenu extends AbstractContainerMenu {

    public final ItemStack itemStack;

    // 服务端 + 客户端都会调用这个构造
    public IFFContainerMenu(int id, Inventory playerInv, ItemStack stack) {
        super(ModMenuTypes.IFF_MENU.get(), id);   // 后面会注册 MenuType
        this.itemStack = stack;
    }

    // 必须实现
    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem() == itemStack || player.getOffhandItem() == itemStack;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY; // 简单粗暴版
        // 或者上面完整的标准实现
    }

    // 可选：如果你想在服务端直接操作 NBT，可以在这里加逻辑
    // 但更推荐在 Screen 的 send 包里处理（见下面）
}
