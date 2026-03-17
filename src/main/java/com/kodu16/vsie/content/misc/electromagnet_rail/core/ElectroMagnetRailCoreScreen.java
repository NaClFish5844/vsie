package com.kodu16.vsie.content.misc.electromagnet_rail.core;

import com.kodu16.vsie.vsie;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("removal")
public class ElectroMagnetRailCoreScreen extends AbstractContainerScreen<ElectroMagnetRailCoreContainerMenu> {

    private static final ResourceLocation IFF_BG_TEXTURE =
            new ResourceLocation(vsie.ID, "textures/gui/iff/iff_gui.png");

    private static final ResourceLocation SLOT_TEXTURE =
            new ResourceLocation(vsie.ID, "textures/gui/slot.png");

    public ElectroMagnetRailCoreScreen(ElectroMagnetRailCoreContainerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        // 复用 IFF 的 GUI 尺寸。
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // 复用 IFF 背景贴图，满足“与 IFF 相同 GUI 背景”的需求。
        guiGraphics.blit(IFF_BG_TEXTURE,
                this.leftPos, this.topPos,
                0, 0,
                this.imageWidth, this.imageHeight,
                this.imageWidth, this.imageHeight);

        // 先切换到通用纹理着色器，再绘制 Minecraft 默认槽位贴图。
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        // 绘制核心仓 2x2 槽位底图，对应容器菜单中的 4 个 rail 槽位。
        int coreStartX = this.leftPos + 68-1;
        int coreStartY = this.topPos + 32-1;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                guiGraphics.blit(SLOT_TEXTURE,
                        coreStartX + col * 18,
                        coreStartY + row * 18,
                        0, 0, 18, 18, 18, 18);
            }
        }

        // 绘制玩家背包 3x9 的槽位底图，保证和 vanilla 容器视觉一致。
        int playerInvStartX = this.leftPos + 8-1;
        int playerInvStartY = this.topPos + 84-1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                guiGraphics.blit(SLOT_TEXTURE,
                        playerInvStartX + col * 18,
                        playerInvStartY + row * 18,
                        0, 0, 18, 18, 18, 18);
            }
        }

        // 绘制玩家快捷栏 1x9 的槽位底图。
        int hotbarY = this.topPos + 142-1;
        for (int col = 0; col < 9; col++) {
            guiGraphics.blit(SLOT_TEXTURE,
                    playerInvStartX + col * 18,
                    hotbarY,
                    0, 0, 18, 18, 18, 18);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        // 在 GUI 上显示当前存储的 rail 数量。
        guiGraphics.drawString(this.font,
                Component.literal("Rail: " + this.menu.getRailCount()),
                32, 18,
                0x404040,
                false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
