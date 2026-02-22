// ModMenuTypes.java
package com.kodu16.vsie.registries;

import com.kodu16.vsie.content.item.IFF.server.IFFContainerMenu;
import com.kodu16.vsie.content.turret.AbstractTurretBlockEntity;
import com.kodu16.vsie.content.turret.server.TurretContainerMenu;
import com.kodu16.vsie.content.weapon.AbstractWeaponBlockEntity;
import com.kodu16.vsie.content.weapon.server.WeaponContainerMenu;
import com.kodu16.vsie.vsie;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, vsie.ID);

    // 正确写法：使用 IForgeMenuType 让容器工厂可以读取额外数据（BlockPos）
    public static final RegistryObject<MenuType<TurretContainerMenu>> TURRET_MENU = MENUS.register("turret_menu",
            () -> IForgeMenuType.create((windowId, inv, data) -> {
                // data 里读出客户端传来的 BlockPos
                BlockPos pos = data.readBlockPos();
                AbstractTurretBlockEntity turret = (AbstractTurretBlockEntity) inv.player.level().getBlockEntity(pos);
                return new TurretContainerMenu(windowId, inv, turret);
            }));
    public static final RegistryObject<MenuType<WeaponContainerMenu>> WEAPON_MENU = MENUS.register("weapon_menu",
            () -> IForgeMenuType.create((windowId, inv, data) -> {
                // data 里读出客户端传来的 BlockPos
                BlockPos pos = data.readBlockPos();
                AbstractWeaponBlockEntity weapon = (AbstractWeaponBlockEntity) inv.player.level().getBlockEntity(pos);
                return new WeaponContainerMenu(windowId, inv, weapon);
            }));
    public static final RegistryObject<MenuType<IFFContainerMenu>> IFF_MENU = MENUS.register("iff_menu",
            () -> IForgeMenuType.create((id, inv, data) ->
                    new IFFContainerMenu(id, inv, inv.player.getMainHandItem())));
}
