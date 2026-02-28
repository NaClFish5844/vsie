package com.kodu16.vsie.content.controlseat.functions;

import com.kodu16.vsie.content.particle.ShieldParticleOptions;
import com.kodu16.vsie.content.particle.ShieldParticleType;
import com.kodu16.vsie.registries.ModParticleTypes;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Random;

public class ShieldHandler {
    private static final Random RANDOM = new Random();
    public static void spawnRippleParticles(ServerLevel level, Vec3 hitPoint, Vec3 center) {
        if (hitPoint.distanceToSqr(center) < 1e-6) {
            return;
        }

        Vec3 normal = hitPoint.subtract(center).normalize();

        long currentGameTime = level.getServer().getTickCount();

        // 总共几环（可调）
        final int ringCount = 10;
        final double baseRadius = 0.4;
        final double radiusGrowPerRing = 0.5;
        final int particlesPerRingBase = 16;

        for (int i = 0; i < ringCount; i++) {
            final int stage = i;
            if (level.isClientSide) return;
            double planeRadius = baseRadius + stage * radiusGrowPerRing;
            int particleCount = particlesPerRingBase + stage * 4; // 越外圈粒子越多，看起来更自然

            spawnCircleParticles(
                    level, hitPoint, normal,
                    planeRadius,
                    particleCount,
                    0.6 + stage * 0.08,   // 外圈可以稍快一点扩散
                    i
            );
        }
    }


    /**
     * 在指定平面生成一圈粒子
     *
     * @param center       圆心（即 hitPoint）
     * @param normal       平面法向量（已单位化）
     * @param radius       平面上的圆半径
     * @param particleCount 一圈的粒子数量
     * @param speed        粒子初始速度倍率（通常设为0~1）
     */
    private static void spawnCircleParticles(
            ServerLevel level,
            Vec3 center,
            Vec3 normal,
            double radius,
            int particleCount,
            double speed,
            int count
    ) {
        if (particleCount < 3) return;

        // 构建两个正交于 normal 的基向量 u, v
        Vec3 u = getPerpendicularVector(normal).normalize();
        Vec3 v = normal.cross(u).normalize();

        for (int i = 0; i < particleCount; i++) {
            double theta = (double) i / particleCount * Math.PI * 2;

            double dx = Math.cos(theta) * radius;
            double dy = Math.sin(theta) * radius;

            // 在平面上的偏移
            Vec3 offset = u.scale(dx).add(v.scale(dy));

            // 最终粒子位置
            Vec3 pos = center.add(offset);

            // 可选：添加一点随机高度扰动，让涟漪更有自然感
            // pos = pos.add(normal.scale(ThreadLocalRandom.current().nextGaussian() * 0.08));

            // 生成粒子（速度可以朝外扩散，也可以静止）
            level.sendParticles(
                    new ShieldParticleOptions(count),
                    pos.x, pos.y, pos.z,
                    1,                      // 每次只发1颗
                    0, 0, 0,         // 速度偏移（可设小随机值）
                    0
            );
        }
    }

    /**
     * 获取一个与输入向量垂直的向量（任意一个）
     */
    private static Vec3 getPerpendicularVector(Vec3 vec) {
        if (Math.abs(vec.y) < 0.9) {
            return new Vec3(-vec.z, 0, vec.x).normalize();
        } else {
            return new Vec3(0, -vec.z, vec.y).normalize();
        }
    }



    public static double[] getMinMaxDistance(List<Vec3> points) {
        if (points == null || points.size() < 2) {
            return new double[]{0, 0};
        }

        double minDist = Double.MAX_VALUE;
        double maxDist = 0;

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double d = points.get(i).distanceTo(points.get(j));
                if (d < minDist) minDist = d;
                if (d > maxDist) maxDist = d;
            }
        }

        return new double[]{minDist, maxDist};
    }

}
