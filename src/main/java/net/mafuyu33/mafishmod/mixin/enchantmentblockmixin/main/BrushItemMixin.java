package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.main;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.mafuyu33.mafishmod.mixinhelper.InjectHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BrushItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(BrushItem.class)
public abstract class BrushItemMixin extends Item {
	public BrushItemMixin(Settings settings) {
		super(settings);
	}

	@Inject(at = @At("HEAD"), method = "useOnBlock")
	private void init(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		if (!context.getWorld().isClient) {//只在服务端运行
			if (!Objects.equals(context.getStack().getEnchantments(), new NbtList())) {//如果刷子有附魔
				if(Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getBlockPos()), new NbtList())){//如果Pos位置方块没有附魔
				NbtList enchantments = context.getStack().getEnchantments();//获取刷子上的附魔信息列表
				BlockEnchantmentStorage.addBlockEnchantment(context.getBlockPos().toImmutable(), enchantments);//储存信息
				EquipmentSlot equipmentSlot = context.getStack().equals(context.getPlayer().getEquippedStack(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
				context.getStack().damage(1, context.getPlayer(), (userx) -> {
					userx.sendEquipmentBreakStatus(equipmentSlot);
				});
				}else {//位置方块有附魔
					NbtList oldEnchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getBlockPos());
					BlockEnchantmentStorage.removeBlockEnchantment(context.getBlockPos().toImmutable());//先删除信息
					NbtList enchantments = context.getStack().getEnchantments();//获取刷子上的附魔信息列表
					NbtList newEnchantments =mergeNbtLists(oldEnchantments, enchantments); // 合并附魔列表
					BlockEnchantmentStorage.addBlockEnchantment(context.getBlockPos().toImmutable(), newEnchantments);//储存信息
				}
			}else {//如果刷子没有附魔
				BlockEnchantmentStorage.removeBlockEnchantment(context.getBlockPos().toImmutable());//删除信息
			}
		}

	}
	// 合并两个 NBT 列表
	@Unique
	private static NbtList mergeNbtLists(NbtList list1, NbtList list2) {
		NbtList mergedList = new NbtList();
		mergedList.addAll(list1);
		mergedList.addAll(list2);
		return mergedList;
	}
}