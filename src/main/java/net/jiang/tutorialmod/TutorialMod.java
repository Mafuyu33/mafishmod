package net.jiang.tutorialmod;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.jiang.tutorialmod.block.entity.ModBlockEntities;
import net.jiang.tutorialmod.enchantment.ModEnchantments;
import net.jiang.tutorialmod.event.AttackEntityHandler;
import net.jiang.tutorialmod.item.ModItemGroups;
import net.jiang.tutorialmod.item.ModItems;
import net.jiang.tutorialmod.block.ModBlocks;
import net.jiang.tutorialmod.item.custom.FireworkArrowItem;
import net.jiang.tutorialmod.item.custom.ModStatusEffects;
import net.jiang.tutorialmod.sound.ModSounds;
import net.jiang.tutorialmod.util.ModCustomTrades;
import net.jiang.tutorialmod.util.ModLootTableModifiers;
import net.jiang.tutorialmod.villager.ModVillagers;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.timer.TimerCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

public class TutorialMod implements ModInitializer {
	public static final String MOD_ID="tutorialmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
//		ClientTickEvents.START_CLIENT_TICK.register(this::onClientTick);

		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();

		FuelRegistry.INSTANCE.add(ModItems.COAL_BRIQUEITE,200);
		ModStatusEffects.registerModEffect();
		ModLootTableModifiers.modifyLootTables();
		ModVillagers.registerVillagers();
		ModSounds.registerSounds();
		ModCustomTrades.registerCustomTrades();
		ModEnchantments.registerModEnchantments();
		AttackEntityCallback.EVENT.register(new AttackEntityHandler());
	}


//	private int tickCounter = 0;
//	private final int delayTicks = 10; // 延迟的 tick 数，可以根据需要调整
//	private boolean shouldExecuteDelayedAction = false;

//	private void onClientTick(net.minecraft.client.MinecraftClient minecraftClient) {
//		// 在客户端每次 tick 开始时触发的逻辑
//		tickCounter++;
//		// 检查是否达到了延迟的 tick 数
//		if (tickCounter >= delayTicks) {
//			shouldExecuteDelayedAction = true;
//			tickCounter = 0; // 重置计时器
//		}
//
//		// 在达到延迟后执行操作
//		if (shouldExecuteDelayedAction) {
//			// 在这里执行你的延时操作
//			// 例如，修改渲染效果、创建自定义 HUD 元素等
//			executeDelayedAction();
//			shouldExecuteDelayedAction = false; // 确保操作只执行一次
//		}
//	}
//	private void executeDelayedAction() {
//		// 在这里执行延时操作的逻辑
//		// 例如，修改渲染效果、创建自定义 HUD 元素等
//	}

}