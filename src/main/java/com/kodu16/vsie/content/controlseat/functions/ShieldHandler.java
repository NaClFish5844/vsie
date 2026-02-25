package com.kodu16.vsie.content.controlseat.functions;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class ShieldHandler {
    public static void spawnRippleParticles(ServerLevel level, Vec3 hitPoint, Vec3 hitDir) {
        int numRings = 3;
        int particlesPerRing = 16;
        double speed = 0.02;

        // 构造一个与 hitDir 垂直的局部坐标系 (u, v)
        Vec3 w = hitDir.normalize();
        Vec3 arbitrary = Math.abs(w.y) < 0.9 ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
        Vec3 u = w.cross(arbitrary).normalize();   // 第一个正交向量
        Vec3 v = w.cross(u).normalize();           // 第二个正交向量（也垂直于 w）

        for (int ring = 0; ring < numRings; ring++) {
            double ringRadius = 0.2 + ring * 0.4;
            for (int i = 0; i < particlesPerRing; i++) {
                double angle = (i * 2 * Math.PI) / particlesPerRing;
                Vec3 localOffset = u.scale(Math.cos(angle) * ringRadius)
                        .add(v.scale(Math.sin(angle) * ringRadius));

                // 计算粒子位置
                Vec3 pos = hitPoint.add(localOffset);

                // 计算漂浮速度
                Vec3 outwardMovement = hitDir.scale(0.05); // 向外漂浮的速度

                // 使用 DustParticleOptions（浅蓝色）
                Vector3f dustColor = new Vector3f(1.0f, 0.8f, 1.0f);  // 浅蓝色 RGB
                DustParticleOptions dustParticle = new DustParticleOptions(dustColor, 1.0f); // 浅蓝色
                level.sendParticles(dustParticle,
                        pos.x, pos.y, pos.z,
                        1, outwardMovement.x, outwardMovement.y, outwardMovement.z, speed);

                // 使用 Glow 发光粒子
                level.sendParticles(ParticleTypes.GLOW,
                        pos.x, pos.y, pos.z,
                        1, outwardMovement.x, outwardMovement.y, outwardMovement.z, speed);
            }
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
