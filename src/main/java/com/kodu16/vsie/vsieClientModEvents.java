package com.kodu16.vsie;

import com.kodu16.vsie.content.item.IFF.IFFScreen;
import com.kodu16.vsie.content.item.shieldtool.shieldtoolScreen;
import com.kodu16.vsie.content.missile.AbstractMissileGeoRenderer;
import com.kodu16.vsie.content.turret.client.TurretScreen;
import com.kodu16.vsie.content.weapon.client.WeaponScreen;
import com.kodu16.vsie.registries.ModMenuTypes;
import com.kodu16.vsie.registries.vsieEntities;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = vsie.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class vsieClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() ->
                MenuScreens.register(ModMenuTypes.TURRET_MENU.get(), TurretScreen::new)
        );
        event.enqueueWork(() ->
                MenuScreens.register(ModMenuTypes.WEAPON_MENU.get(), WeaponScreen::new)
        );
        event.enqueueWork(() ->
                MenuScreens.register(ModMenuTypes.IFF_MENU.get(), IFFScreen::new)
        );
        event.enqueueWork(() ->
                MenuScreens.register(ModMenuTypes.SHIELD_TOOL_MENU.get(), shieldtoolScreen::new)
        );
        event.enqueueWork(() ->
                EntityRenderers.register(vsieEntities.BASIC_MISSILE.get(), AbstractMissileGeoRenderer::new)
        );
    }
}

