package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.slippery;

import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {
	public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(at = @At(value = "HEAD"), method = "mouseClicked",cancellable = true)
	private void init(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
		Slot slot = this.getSlotAt(mouseX, mouseY);
		System.out.println(slot);
		if(slot!=null) {
			ItemStack itemStack = slot.getStack();
			System.out.println(itemStack);
			System.out.println(itemStack.getEnchantments());
			if (EnchantmentHelper.getLevel(ModEnchantments.SLIPPERY, itemStack) > 0) {
				if(placeItemInPlayerInventory(this.client.player, itemStack)){
					cir.cancel();
				}
			}
		}
	}

//	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/screen/ScreenHandler;canInsertIntoSlot(Lnet/minecraft/item/ItemStack;Lnet/minecraft/screen/slot/Slot;)Z"), method = "mouseReleased",cancellable = true)
//	private void init1(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
//		Slot slot = this.getSlotAt(mouseX, mouseY);
//		if(slot!=null) {
//			ItemStack itemStack = slot.getStack();
//			if (EnchantmentHelper.getLevel(ModEnchantments.SLIPPERY, itemStack) > 0) {
//				if(placeItemInPlayerInventory(this.client.player, itemStack)){
//					cir.cancel();
//				}
//			}
//		}
//	}

	// 在玩家背包的随机位置放置物品
	@Unique
	private static boolean placeItemInPlayerInventory(PlayerEntity player, ItemStack itemStack) {
		// 获取玩家背包
		PlayerInventory playerInventory = player.getInventory();

		//数量
		int count = itemStack.getCount();

		// 获取玩家背包的所有物品槽位
		DefaultedList<ItemStack> slots = playerInventory.main;

		// 创建一个随机数生成器
		Random random = new Random();

		// 循环直到找到一个空槽位
		int attempts = 0;
		while (attempts < 100) { // 防止无限循环
			// 生成一个随机的槽位索引
			int slotIndex = random.nextInt(slots.size());

			// 获取该槽位的物品堆
			ItemStack slotStack = slots.get(slotIndex);

			// 检查该槽位是否为空
			if (slotStack.isEmpty()) {
				// 如果槽位为空，则将物品放置到该槽位

				slots.set(slotIndex, itemStack.copy());
				itemStack.decrement(count);
				// 可选：通知玩家物品已经放置到背包中
				System.out.println("已将物品放置到背包中");
				return true;
			}

			// 尝试下一个槽位
			attempts++;
		}

		// 如果没有找到空槽位，则在控制台输出消息
		System.out.println("无法找到空槽位放置物品");
		return false;
	}
}