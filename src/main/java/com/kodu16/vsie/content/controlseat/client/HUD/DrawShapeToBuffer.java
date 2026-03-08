package com.kodu16.vsie.content.controlseat.client.HUD;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import org.joml.Matrix4f;

public class DrawShapeToBuffer {

    public static void drawPartialArc(BufferBuilder buffer, Matrix4f mat, int cx, int cy, int radius, int thickness,
                                      int argb, float startAngleDeg, float endAngleDeg) {

        float startRad = (float) Math.toRadians(startAngleDeg);
        float endRad = (float) Math.toRadians(endAngleDeg);
        if (endRad < startRad) endRad += (float) (Math.PI * 2f);

        int segments = Math.max(16, (int) Math.toDegrees(endRad - startRad)*2); // 每度1段就够顺滑了

        float a = (argb >> 24 & 255) / 255f;
        float r = (argb >> 16 & 255) / 255f;
        float g = (argb >>  8 & 255) / 255f;
        float b = (argb & 255) / 255f;

        float innerR = radius - thickness;

        // Draw the partial arc
        for (int i = 0; i <= segments; i++) {
            float angle = startRad + i * (endRad - startRad) / segments;
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            float outerX = cx + cos * radius;
            float outerY = cy + sin * radius;
            float innerX = cx + cos * innerR;
            float innerY = cy + sin * innerR;

            // Outer ring
            buffer.vertex(mat, outerX, outerY, 0).color(r, g, b, a).endVertex();
            // Inner ring
            buffer.vertex(mat, innerX, innerY, 0).color(r, g, b, a).endVertex();
            if(i==0 || i==segments) {
                insertDegenerate(buffer,mat,outerX,outerY,argb);
            }
        }
    }

    public static void drawHollowRectangle(BufferBuilder buffer, Matrix4f mat, int centerX, int centerY,
                                           int width, int height, int thickness, int argb) {
        float halfOuterWidth = width / 2.0f;
        float halfOuterHeight = height / 2.0f;
        float halfInnerWidth = halfOuterWidth - thickness;
        float halfInnerHeight = halfOuterHeight - thickness;

        // 防止厚度过大导致内框负数
        if (halfInnerWidth < 0) halfInnerWidth = 0;
        if (halfInnerHeight < 0) halfInnerHeight = 0;

        float a = (argb >> 24 & 255) / 255.0f;
        float r = (argb >> 16 & 255) / 255.0f;
        float g = (argb >>  8 & 255) / 255.0f;
        float b = (argb       & 255) / 255.0f;

        // 按顺序画四条边（每条边用两个点 + 退化连接）
        // 左上外 → 左上内 → 右上外 → 右上内 → 右下外 → 右下内 → 左下外 → 左下内 → 回到左上外

        // 左上外
        buffer.vertex(mat, centerX - halfOuterWidth, centerY - halfOuterHeight, 0).color(r,g,b,a).endVertex();
        // 左上内
        buffer.vertex(mat, centerX - halfInnerWidth, centerY - halfInnerHeight, 0).color(r,g,b,a).endVertex();

        // 右上外
        buffer.vertex(mat, centerX + halfOuterWidth, centerY - halfOuterHeight, 0).color(r,g,b,a).endVertex();
        // 右上内
        buffer.vertex(mat, centerX + halfInnerWidth, centerY - halfInnerHeight, 0).color(r,g,b,a).endVertex();

        // 右下外
        buffer.vertex(mat, centerX + halfOuterWidth, centerY + halfOuterHeight, 0).color(r,g,b,a).endVertex();
        // 右下内
        buffer.vertex(mat, centerX + halfInnerWidth, centerY + halfInnerHeight, 0).color(r,g,b,a).endVertex();

        // 左下外
        buffer.vertex(mat, centerX - halfOuterWidth, centerY + halfOuterHeight, 0).color(r,g,b,a).endVertex();
        // 左下内
        buffer.vertex(mat, centerX - halfInnerWidth, centerY + halfInnerHeight, 0).color(r,g,b,a).endVertex();

        // 闭合回到起点（左上外）
        buffer.vertex(mat, centerX - halfOuterWidth, centerY - halfOuterHeight, 0).color(r,g,b,a).endVertex();
        buffer.vertex(mat, centerX - halfInnerWidth, centerY - halfInnerHeight, 0).color(r,g,b,a).endVertex();
    }


    public static void drawThickLine(BufferBuilder buffer, Matrix4f mat,
                                     int x1, int y1,
                                     int x2, int y2,
                                     int thickness,
                                     int argb) {
        if (thickness <= 0) return;

        float a = ((argb >> 24) & 255) / 255f;
        float r = ((argb >> 16) & 255) / 255f;
        float g = ((argb >>  8) & 255) / 255f;
        float b = ( argb        & 255) / 255f;

        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float) Math.sqrt(dx*dx + dy*dy);
        if(len<0.1) {return;}

        // 关键：逆时针法线（从起点看过去，左侧在上）
        float nx =  dy / len;
        float ny = -dx / len;

        float half = thickness / 2f;

        // 逆时针顺序
        insertDegenerate(buffer,mat,x1,y1,argb);
        buffer.vertex(mat, x1 + nx * half, y1 + ny * half, 0).color(r,g,b,a).endVertex(); // 起点左
        buffer.vertex(mat, x1 - nx * half, y1 - ny * half, 0).color(r,g,b,a).endVertex(); // 起点右
        buffer.vertex(mat, x2 + nx * half, y2 + ny * half, 0).color(r,g,b,a).endVertex(); // 终点左
        buffer.vertex(mat, x2 - nx * half, y2 - ny * half, 0).color(r,g,b,a).endVertex(); // 终点右
        insertDegenerate(buffer,mat,x2,y2,argb);
    }

    public static void insertDegenerate(BufferBuilder buffer, Matrix4f mat, float x, float y, int argb) {
        float a = ((argb >> 24) & 255) / 255f;
        float r = ((argb >> 16) & 255) / 255f;
        float g = ((argb >> 8) & 255) / 255f;
        float b = (argb & 255) / 255f;

        // 重复两次顶点
        buffer.vertex(mat, x, y, 0).color(r, g, b, a).endVertex();
        buffer.vertex(mat, x, y, 0).color(r, g, b, a).endVertex();
    }

}
