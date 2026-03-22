package com.kodu16.vsie.content.turret.heavyturret;

import com.kodu16.vsie.network.turret.HeavyTurretC2SPacket;
import com.kodu16.vsie.network.turret.TurretDefaultSpinC2SPacket;
import com.kodu16.vsie.registries.ModNetworking;
import com.kodu16.vsie.vsie;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings({"removal"})
public class HeavyTurretScreen extends AbstractContainerScreen<HeavyTurretContainerMenu> {

    // 功能：提供重型炮塔默认俯仰角输入框，允许玩家配置空闲时的 X 轴朝向。
    private EditBox editBoxSpinX;
    // 功能：提供重型炮塔默认偏航角输入框，允许玩家配置空闲时的 Y 轴朝向。
    private EditBox editBoxSpinY;
    private static final ResourceLocation TEXTURE = new ResourceLocation(vsie.ID, "textures/gui/turret/turret_gui.png");

    public HeavyTurretScreen(HeavyTurretContainerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    // 1.20.1 必须重写这个新签名的方法
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);   // 背景（半透明黑）
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);   // 鼠标悬停提示
    }

    // renderBg 也必须改成 GuiGraphics
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // 获取 TurretBlockEntity
        AbstractHeavyTurretBlockEntity turret = menu.getBlockEntity();  // 这里 menu.getBlockEntity() 返回 TurretBlockEntity
        // 直接用 GuiGraphics 的 blit 方法绘制纹理
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // 功能：绘制与武器界面一致的 4 路频道状态图标。
        ResourceLocation iconChannel1 = turret.getData().channel1
                ? new ResourceLocation(vsie.ID, "textures/gui/weapon/channel1_on.png")
                : new ResourceLocation(vsie.ID, "textures/gui/weapon/channel1_off.png");
        ResourceLocation iconChannel2 = turret.getData().channel2
                ? new ResourceLocation(vsie.ID, "textures/gui/weapon/channel2_on.png")
                : new ResourceLocation(vsie.ID, "textures/gui/weapon/channel2_off.png");
        ResourceLocation iconChannel3 = turret.getData().channel3
                ? new ResourceLocation(vsie.ID, "textures/gui/weapon/channel3_on.png")
                : new ResourceLocation(vsie.ID, "textures/gui/weapon/channel3_off.png");
        ResourceLocation iconChannel4 = turret.getData().channel4
                ? new ResourceLocation(vsie.ID, "textures/gui/weapon/channel4_on.png")
                : new ResourceLocation(vsie.ID, "textures/gui/weapon/channel4_off.png");

        guiGraphics.blit(iconChannel1, this.leftPos + 30, this.topPos + 20, 0, 0, 20, 20, 20, 20);
        guiGraphics.blit(iconChannel2, this.leftPos + 60, this.topPos + 20, 0, 0, 20, 20, 20, 20);
        guiGraphics.blit(iconChannel3, this.leftPos + 90, this.topPos + 20, 0, 0, 20, 20, 20, 20);
        guiGraphics.blit(iconChannel4, this.leftPos + 120, this.topPos + 20, 0, 0, 20, 20, 20, 20);

        // 根据状态选择不同的图标
        ResourceLocation iconmanual = new ResourceLocation(vsie.ID, "textures/gui/heavyturret/target_manual.png");
        ResourceLocation iconauto = new ResourceLocation(vsie.ID, "textures/gui/heavyturret/target_auto.png");
        ResourceLocation iconsmart = new ResourceLocation(vsie.ID, "textures/gui/heavyturret/target_smart.png");

        // 绘制状态图标
        if (turret.getData().firetype == 0) {
            guiGraphics.blit(iconmanual, this.leftPos + 78, this.topPos + 70, 0, 0, 20, 20, 19, 19);
        }
        if (turret.getData().firetype == 1) {
            guiGraphics.blit(iconauto, this.leftPos + 78, this.topPos + 70, 0, 0, 20, 20, 19, 19);
        }
        if (turret.getData().firetype == 2) {
            guiGraphics.blit(iconsmart, this.leftPos + 78, this.topPos + 70, 0, 0, 20, 20, 19, 19);
        }
    }

    // 可选：如果你还想显示物品栏标签、玩家背包等文字，也可以重写这个
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // 标题
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        // 玩家背包文字 “物品栏”
        //guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
    }

    @Override
    protected void init() {
        super.init();
        BlockPos pos = menu.getBlockEntity().getBlockPos(); // 如果有 getBlockEntity() 方法
        AbstractHeavyTurretBlockEntity turret = menu.getBlockEntity();
        // 功能：初始化默认旋转输入框，并显示当前重型炮塔保存的默认角度。
        this.editBoxSpinX = createIntEditBox("SpinX", this.leftPos + 112, this.topPos + 48, String.valueOf(turret.defaultspinx));
        this.editBoxSpinY = createIntEditBox("SpinY", this.leftPos + 48, this.topPos + 48, String.valueOf(turret.defaultspiny));
        // 功能：新增与主武器一致的四个频道切换按键。
        this.addRenderableWidget(Button.builder(
                Component.literal("CH1"),
                btn -> ModNetworking.CHANNEL.sendToServer(new HeavyTurretC2SPacket(pos, 1))
        ).bounds(leftPos + 30, topPos + 40, 20, 10).build());
        this.addRenderableWidget(Button.builder(
                Component.literal("CH2"),
                btn -> ModNetworking.CHANNEL.sendToServer(new HeavyTurretC2SPacket(pos, 2))
        ).bounds(leftPos + 60, topPos + 40, 20, 10).build());
        this.addRenderableWidget(Button.builder(
                Component.literal("CH3"),
                btn -> ModNetworking.CHANNEL.sendToServer(new HeavyTurretC2SPacket(pos, 3))
        ).bounds(leftPos + 90, topPos + 40, 20, 10).build());
        this.addRenderableWidget(Button.builder(
                Component.literal("CH4"),
                btn -> ModNetworking.CHANNEL.sendToServer(new HeavyTurretC2SPacket(pos, 4))
        ).bounds(leftPos + 120, topPos + 40, 20, 10).build());

        // 功能：保留并更新模式切换按钮，使用 100+firetype 编码避免与频道按键冲突。
        this.addRenderableWidget(Button.builder(
                        Component.literal("switch"),
                        button -> ModNetworking.CHANNEL.sendToServer(new HeavyTurretC2SPacket(pos, ((menu.getBlockEntity().getData().firetype + 1) % 3) + 100)))
                .pos(this.leftPos + 73, this.topPos + 100)
                .size(30, 15)
                .build());

        // 功能：保存 GUI 中填写的默认旋转角度，并同步到服务端方块实体。
        this.addRenderableWidget(Button.builder(
                        Component.literal("保存"),
                        button -> saveAndClose())
                .bounds(this.leftPos + 32, this.topPos + 140, 40, 20)
                .build());

        // 功能：关闭界面但不提交默认旋转修改，保持与普通炮塔界面一致。
        this.addRenderableWidget(Button.builder(
                        Component.literal("取消"),
                        button -> this.minecraft.player.closeContainer())
                .bounds(this.leftPos + 72, this.topPos + 140, 40, 20)
                .build());
    }

    // 功能：创建仅用于输入默认旋转角度的整数输入框，并填充当前值。
    private EditBox createIntEditBox(String name, int x, int y, String initialValue) {
        EditBox box = new EditBox(this.font, x, y, 24, 10, Component.literal(name));
        box.setMaxLength(8);
        box.setValue(initialValue);
        box.setFocused(false);
        this.addRenderableWidget(box);
        return box;
    }

    // 功能：安全解析整数输入，在空值或非法值时回退到已有默认值，避免 GUI 输入导致异常。
    private int safeParseInt(String text, int defaultValue) {
        if (text == null || text.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // 功能：将 GUI 中配置的默认 X/Y 角度写回重型炮塔，并通过数据包同步到服务端保存。
    private void saveAndClose() {
        AbstractHeavyTurretBlockEntity turret = this.menu.getBlockEntity();
        int spinX = safeParseInt(editBoxSpinX.getValue(), turret.defaultspinx);
        int spinY = safeParseInt(editBoxSpinY.getValue(), turret.defaultspiny);
        turret.defaultspinx = spinX;
        turret.defaultspiny = spinY;
        ModNetworking.CHANNEL.sendToServer(new TurretDefaultSpinC2SPacket(turret.getBlockPos(), spinX, spinY));
        this.minecraft.player.closeContainer();
    }
}
