package net.mafuyu33.mafishmod.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.mafuyu33.mafishmod.mixinhelper.BowDashMixinHelper;
import net.mafuyu33.mafishmod.mixinhelper.ElytraJumpMixinHelper;
import net.mafuyu33.mafishmod.mixinhelper.ShieldDashMixinHelper;

public class AttackKeyCheckHandler {
//    // 获取 MinecraftClient 实例
//    private static final MinecraftClient client = MinecraftClient.getInstance();
//
//    // 获取 MinecraftClient 中的攻击键绑定
//    private static final KeyBinding attackKeyBinding = client.options.attackKey;
    private static boolean wasKeyPressedLastFrame = false;
    private static boolean wasJumpKeyPressedLastFrame = false;
    // 在初始化阶段注册攻击键的按下事件监听器
    public static void registerAttackKeyListener() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            // 检查攻击键是否在当前帧被按下
            boolean isKeyPressed = client.options.attackKey.isPressed();

            // 如果攻击键在上一帧被按下而在当前帧没有被按下，表示松开了按键
            if (wasKeyPressedLastFrame && !isKeyPressed) {
                // 当攻击键被松开时执行操作
//                System.out.println("Attack key released!");
                ShieldDashMixinHelper.setIsAttackKeyPressed(false);
                BowDashMixinHelper.setIsAttackKeyPressed(false);

                // 在这里执行您的操作
            }

            // 更新攻击键上一帧的按下状态
            wasKeyPressedLastFrame = isKeyPressed;

            // 检查攻击键是否在当前帧被按下
            if (isKeyPressed) {
                // 当攻击键被按下时执行操作
//                System.out.println("Attack key pressed!");
                ShieldDashMixinHelper.setIsAttackKeyPressed(true);
                BowDashMixinHelper.setIsAttackKeyPressed(true);
                // 在这里执行您的操作
            }


            boolean isJumpKeyPressed = client.options.jumpKey.isPressed();
            // 如果攻击键在上一帧被按下而在当前帧没有被按下，表示松开了按键
            if (wasJumpKeyPressedLastFrame && !isJumpKeyPressed) {
                // 当攻击键被松开时执行操作
//                System.out.println("Attack key released!");
                ElytraJumpMixinHelper.setIsJumpKeyPressed(false);
                // 在这里执行您的操作
            }

            // 更新攻击键上一帧的按下状态
            wasJumpKeyPressedLastFrame = isJumpKeyPressed;

            // 检查攻击键是否在当前帧被按下
            if (isJumpKeyPressed) {
                // 当攻击键被按下时执行操作
//                System.out.println("Attack key pressed!");
                ElytraJumpMixinHelper.setIsJumpKeyPressed(true);
                // 在这里执行您的操作
            }
        });
    }
}