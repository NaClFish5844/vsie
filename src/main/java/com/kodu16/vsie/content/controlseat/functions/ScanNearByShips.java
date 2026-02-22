package com.kodu16.vsie.content.controlseat.functions;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3d;
import org.joml.primitives.AABBdc;
import org.valkyrienskies.core.api.ships.LoadedShip;
import org.valkyrienskies.core.api.ships.QueryableShipData;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.core.impl.shadow.Bl;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScanNearByShips {
    public static Map<String, Object> scanships(QueryableShipData<Ship> qsd, BlockPos pos, Level level) {
        Map<String, Object> mapper = new HashMap<>();
        try {
            qsd.iterator().forEachRemaining(e -> {
                AABBdc p = e.getWorldAABB();
                double[] c = getAABBdcCenter(p);
                Map<String, Object> attr = new HashMap<>();
                attr.put("id", e.getId());
                attr.put("slug", e.getSlug());
                attr.put("dimension", e.getChunkClaimDimension());
                attr.put("x", c[0]);
                attr.put("y", c[1]);
                attr.put("z", c[2]);
                //LogUtils.getLogger().warn("detected ship:"+e.getSlug()+"position:"+attr.get("x")+","+attr.get("y")+","+attr.get("z"));
                mapper.put(String.valueOf(e.getId()), attr);
            });
        } catch (RuntimeException ex) {
        }
        return mapper;
    }
    public static ArrayList<Ship> scanenemyships(QueryableShipData<Ship> qsd, BlockPos pos, Level level, String enemystr, String allystr) {
        ArrayList<Ship> enemylist = new ArrayList<>();
        try {
            qsd.iterator().forEachRemaining(e -> {
                if(getPriority(enemystr,allystr,e.getSlug())==1) {
                    enemylist.add(e);
                }
            });
        } catch (RuntimeException ex) {
        }
        return enemylist;
    }
    private static double[] getAABBdcCenter(AABBdc aabb) {
        double width = aabb.maxX() - aabb.minX();
        double len = aabb.maxZ() - aabb.minZ();
        double hight = aabb.maxY() - aabb.minY();
        double centerX = aabb.minX() + width / 2;
        double centerY = aabb.minY() + hight / 2;
        double centerZ = aabb.minZ() + len / 2;
        return new double[]{centerX, centerY, centerZ};
    }

    public static int getPriority(String a, String b, String c) {
        // 防御性编程：处理 null 和空字符串
        if (c == null || c.isEmpty() || a.isEmpty() || b.isEmpty()) {
            return 0;
        }

        // 查找第一次出现的位置（-1 表示没找到）
        int posA = c.indexOf(a);
        int posB = c.indexOf(b);

        // 都不包含
        if (posA == -1 && posB == -1) {
            return 0;
        }

        // 只包含 a
        if (posA != -1 && posB == -1) {
            return 1;
        }

        // 只包含 b
        if (posA == -1 && posB != -1) {
            return 2;
        }

        // 同时包含 → 比较第一次出现的位置
        // posA < posB 说明 a 更靠前
        return posA < posB ? 1 : 2;
    }
}
