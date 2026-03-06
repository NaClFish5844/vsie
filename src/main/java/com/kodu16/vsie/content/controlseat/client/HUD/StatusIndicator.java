package com.kodu16.vsie.content.controlseat.client.HUD;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;

public class StatusIndicator {
    private static final int SIDEARC_RADIUS = 90; // 半圆半径（像素）
    private static final int SIDEARC_THICKNESS = 2; // 弧线厚度
    private static final int TEXT_ALPHA    = 7;   // 主文字透明度
    // 颜色（ARGB）
    private static final int WHITE = FastColor.ARGB32.color(TEXT_ALPHA, 0xBB, 0xBB, 0xBB);

    private static final int MAIN_COLOR = FastColor.ARGB32.color(TEXT_ALPHA, 0x00, 0xFF, 0x99);
    private static final int SUB_COLOR  = FastColor.ARGB32.color(TEXT_ALPHA, 0x00, 0x55, 0x55);

    private static final int MAIN_COLOR_FUEL = FastColor.ARGB32.color(TEXT_ALPHA, 0xFF, 0xAA, 0x11);
    private static final int SUB_COLOR_FUEL  = FastColor.ARGB32.color(TEXT_ALPHA, 0x55, 0x55, 0x11);

    private static final int MAIN_COLOR_SHIELD = FastColor.ARGB32.color(TEXT_ALPHA, 0x00, 0x55, 0xFF);
    private static final int SUB_COLOR_SHIELD  = FastColor.ARGB32.color(TEXT_ALPHA, 0x00, 0x22, 0x55);

    private static final int MAIN_COLOR_HEAT = FastColor.ARGB32.color(TEXT_ALPHA, 0xFF, 0x22, 0x22);
    private static final int SUB_COLOR_HEAT  = FastColor.ARGB32.color(TEXT_ALPHA, 0x55, 0x22, 0x22);
    private static final Minecraft mc = Minecraft.getInstance(); // drawGlowText 要用

    public static void renderDecorative(GuiGraphics gg, float energypercent, float fuelpercent, float shieldpercent) {
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();
        int centerX = sw / 2;
        int centerY = sh / 2;

        final int LINE_THICKNESS = 1;           // 分界线粗细（可调1~3）
        final float LINE_INNER = SIDEARC_RADIUS - SIDEARC_THICKNESS;   // 从弧内侧往里一点
        final float LINE_OUTER  = SIDEARC_RADIUS;                      // 稍微超出弧外一点，更醒目

        // ────────────── 能量 ──────────────
        float energyAngle = -210 + 60 * energypercent;
        DrawShape.drawPartialArc(gg, centerX - centerX/4, centerY, SIDEARC_RADIUS, SIDEARC_THICKNESS, MAIN_COLOR,      -210, energyAngle);
        DrawShape.drawPartialArc(gg, centerX - centerX/4, centerY, SIDEARC_RADIUS, SIDEARC_THICKNESS, WHITE,       energyAngle, -150);

        //drawRadialSeparator(gg, centerX - centerX/4, centerY, energyAngle, LINE_INNER, LINE_OUTER, LINE_THICKNESS, WHITE);

        // ────────────── 燃油 ──────────────
        float fuelAngle = -211 + 62 * fuelpercent;
        DrawShape.drawPartialArc(gg, centerX - centerX/4 - 2, centerY, SIDEARC_RADIUS+4, SIDEARC_THICKNESS, MAIN_COLOR_FUEL, -211, fuelAngle);
        DrawShape.drawPartialArc(gg, centerX - centerX/4 - 2, centerY, SIDEARC_RADIUS+4, SIDEARC_THICKNESS, WHITE,  fuelAngle, -149);

        //drawRadialSeparator(gg, centerX - centerX/4 - 3, centerY, fuelAngle, LINE_INNER, LINE_OUTER, LINE_THICKNESS, WHITE);

        // ────────────── 护盾 ──────────────
        float shieldAngle = 30 - 60 * shieldpercent;
        DrawShape.drawPartialArc(gg, centerX + centerX/4, centerY, SIDEARC_RADIUS, SIDEARC_THICKNESS, MAIN_COLOR_SHIELD, shieldAngle, 30);
        DrawShape.drawPartialArc(gg, centerX + centerX/4, centerY, SIDEARC_RADIUS, SIDEARC_THICKNESS, WHITE,  -30, shieldAngle);

        //drawRadialSeparator(gg, centerX + centerX/4, centerY, shieldAngle, LINE_INNER, LINE_OUTER, LINE_THICKNESS, WHITE);

        //热量（未实装）
        // DrawShape.drawPartialArc(gg, centerX + centerX/4 + 2, centerY, SIDEARC_RADIUS, SIDEARC_THICKNESS, MAIN_COLOR_HEAT, heatStart, heatAngle);
        DrawShape.drawPartialArc(gg, centerX + centerX/4 + 2, centerY, SIDEARC_RADIUS+4, SIDEARC_THICKNESS, WHITE,  -31, 31);

        //drawRadialSeparator(gg, centerX + centerX/4 + 3, centerY, heatAngle, LINE_INNER, LINE_OUTER, LINE_THICKNESS, WHITE);
    }

    public static void drawRadialSeparator(
            GuiGraphics gg,
            int cx, int cy,
            float angleDeg,
            float lengthInner, float lengthOuter,
            int thickness,
            int argb)
    {
        float rad = (float) Math.toRadians(angleDeg);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        // 内端点（靠近圆心）
        int x1 = (int) (cx + cos * lengthInner);
        int y1 = (int) (cy + sin * lengthInner);

        // 外端点（远离圆心）
        int x2 = (int) (cx + cos * lengthOuter);
        int y2 = (int) (cy + sin * lengthOuter);

        DrawShape.drawThickLine(gg, x1, y1, x2, y2, thickness, argb);
    }

}
