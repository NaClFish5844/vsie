package com.kodu16.vsie.content.controlseat.client.HUD;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;

public class StatusIndicator {
    private static final int SIDEARC_RADIUS = 90; // 半圆半径（像素）
    private static final int SIDEARC_THICKNESS = 3; // 弧线厚度
    private static final int TEXT_ALPHA    = 5;   // 主文字透明度
    // 颜色（ARGB）
    private static final int MAIN_COLOR = FastColor.ARGB32.color(TEXT_ALPHA, 0x00, 0xFF, 0x99);
    private static final int SUB_COLOR  = FastColor.ARGB32.color(TEXT_ALPHA, 0x00, 0x55, 0x55);

    private static final int MAIN_COLOR_SHIELD = FastColor.ARGB32.color(TEXT_ALPHA, 0x00, 0x55, 0xFF);
    private static final int SUB_COLOR_SHIELD  = FastColor.ARGB32.color(TEXT_ALPHA, 0x00, 0x22, 0x55);
    private static final Minecraft mc = Minecraft.getInstance(); // drawGlowText 要用

    public static void renderDecorative(GuiGraphics gg ,float energypercent, float fuelpercent, float shieldpercent) {
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();
        int centerX = sw / 2;
        int centerY = sh / 2;

        DrawShape.drawPartialArc(gg, centerX-centerX/4, centerY, SIDEARC_RADIUS, SIDEARC_THICKNESS, MAIN_COLOR, -210, -210+60*energypercent);
        DrawShape.drawPartialArc(gg, centerX-centerX/4, centerY, SIDEARC_RADIUS, SIDEARC_THICKNESS, SUB_COLOR, -210+60*energypercent, -150);
        /*// 文字直接用 gg.drawString（它内部也是批量的）
        String text = throttlePercent + "%";
        int textWidth = mc.font.width(text);
        gg.drawString(mc.font, text, centerX - textWidth / 2, centerY - 9, MAIN_COLOR, false);*/
        DrawShape.drawPartialArc(gg, centerX+centerX/4, centerY, SIDEARC_RADIUS, SIDEARC_THICKNESS, MAIN_COLOR_SHIELD, 30-60*shieldpercent,30);
        DrawShape.drawPartialArc(gg, centerX+centerX/4, centerY, SIDEARC_RADIUS, SIDEARC_THICKNESS, SUB_COLOR_SHIELD, -30,30-60*shieldpercent);
    }
}
