package com.kodu16.vsie.content.item.IFF;

import com.kodu16.vsie.content.controlseat.AbstractControlSeatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class iff extends Item {

    private static final String KEY_ENEMY = "enemy";
    private static final String KEY_ALLY   = "ally";

    public iff(Properties pProperties) {
        super(pProperties);
    }

    // ───────────────────────────────────────────────
    //           常用读取方法（推荐都加上）
    // ───────────────────────────────────────────────

    /** 获取敌方队伍名，没设置返回空字符串（永不返回 null） */
    public static String getEnemy(ItemStack stack) {
        return stack.getOrCreateTag().getString(KEY_ENEMY);
    }

    /** 获取友方队伍名，没设置返回空字符串（永不返回 null） */
    public static String getAlly(ItemStack stack) {
        return stack.getOrCreateTag().getString(KEY_ALLY);
    }

    /** 判断是否有设置过敌方队伍（非空字符串） */
    public static boolean hasEnemy(ItemStack stack) {
        return !getEnemy(stack).isEmpty();
    }

    /** 判断是否有设置过友方队伍（非空字符串） */
    public static boolean hasAlly(ItemStack stack) {
        return !getAlly(stack).isEmpty();
    }

    /** 可选：给物品栏 tooltip 显示用 */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, java.util.List<net.minecraft.network.chat.Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        String enemy = getEnemy(stack);
        String ally   = getAlly(stack);

        if (!enemy.isEmpty()) {
            tooltip.add(Component.literal("§c敌方: " + enemy));
        }
        if (!ally.isEmpty()) {
            tooltip.add(Component.literal("§a友方: " + ally));
        }
        if (enemy.isEmpty() && ally.isEmpty()) {
            tooltip.add(Component.literal("§7未设置 IFF"));
        }
    }

    // 你原来的右键打开界面代码（保持不变）
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        if (!level.isClientSide) {
            //LogUtils.getLogger().warn("opening iff GUI");
            player.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("container.vsie.iff");
                }

                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
                    return new IFFContainerMenu(windowId, inv, player.getMainHandItem());
                }
            });
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.PASS;

        ServerPlayer player = (ServerPlayer) context.getPlayer();
        BlockPos clickedPos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        CompoundTag nbt = stack.getOrCreateTag();
        // 如果没控制椅，绑定控制椅
        BlockEntity be = level.getBlockEntity(clickedPos);
        if (be instanceof AbstractControlSeatBlockEntity controlSeat) {
            if (nbt.contains("enemy")) {
                controlSeat.setEnemy(nbt.getString("enemy"));
            }
            if (nbt.contains("ally")) {
                controlSeat.setAlly(nbt.getString("ally"));
            }
            player.displayClientMessage(Component.literal("setting IFF: Enemy:"+nbt.getString("enemy")+" Ally:"+nbt.getString("ally")), true);
            return InteractionResult.CONSUME;
        }
        else {
            player.displayClientMessage(Component.literal(clickedPos+"is not a valid controlseat"), true);
            return InteractionResult.PASS;
        }
    }

}
