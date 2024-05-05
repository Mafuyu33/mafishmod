package net.mafuyu33.mafishmod.mixin.itemmixin.elytra;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.mafuyu33.mafishmod.VRPlugin;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.mafuyu33.mafishmod.mixinhelper.ElytraJumpMixinHelper;
import net.mafuyu33.mafishmod.util.VRDataHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	// 假设这些变量是在类中定义的，以便跨多个ticks记住上一次的位置
	@Unique
	private double lastMainPosY = 0;
	@Unique
	private double lastOffPosY = 0;
	@Unique
	private final double THRESHOLD = 0.01; // 可以调整的阈值

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
		if(this.isFallFlying() && EnchantmentHelper.getLevel(ModEnchantments.BUTTERFLY,itemStack)>0){
			PlayerEntity player =(PlayerEntity) (Object)this;
			if (VRPlugin.canRetrieveData(player)) {
				// 获取当前的位置
//				float yaw = player.getYaw();
//				float pitch = player.getPitch();
				Vec3d pos = player.getPos();
				Vec3d mainPos = VRDataHandler.getMainhandControllerPosition(player);
				Vec3d offPos = VRDataHandler.getOffhandControllerPosition(player);
//				player.sendMessage(Text.literal(("a"+ String.format("%.2f", pos.y))
//						+Text.literal(("b"+String.format("%.2f", mainPos.y))
//						+Text.literal("c"+String.format("%.2f", lastMainPosY))
//						+Text.literal("d"+String.format("%.2f", (pos.y - mainPos.y - lastMainPosY))))),true);
				if(lastMainPosY!=0 && lastOffPosY!=0) {
					// 检查主手位置变化是否小于阈值
					if (pos.y - mainPos.y - lastMainPosY > THRESHOLD) {
						// 添加速度逻辑，向左上添加速度
						player.addVelocity(0, 0.03, 0); // 以x轴正方向为例
						System.out.println("addVelocity r");
					}

					// 检查副手位置变化是否小于阈值
					if (pos.y - offPos.y - lastOffPosY > THRESHOLD) {
						// 添加速度逻辑，向右上添加速度，尝试和主手速度合成
						player.addVelocity(0, 0.03, 0); // 以x轴负方向为例
						System.out.println("addVelocity l");
					}
				}
				// 更新位置记录以用于下一次比较
				lastMainPosY = pos.y - mainPos.y;
				lastOffPosY = pos.y - offPos.y;

			}else if(ElytraJumpMixinHelper.isJumpKeyPressed()){
				this.setPose(EntityPose.STANDING);
				this.addVelocity(0, 0.06, 0);
			}
		}
	}
}