package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.throwableaxe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.mafuyu33.mafishmod.networking.ModMessages;
import net.mafuyu33.mafishmod.networking.packet.C2S.FuC2SPacket;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.Math.abs;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements Ownable {
//	@Unique
//	public int returnTimer;
	@Shadow public abstract ItemStack getStack();

//	@Shadow @Nullable private Entity thrower;//通过Q键投掷出去的

	@Shadow @Nullable public abstract Entity getOwner();
	@Unique
	@Nullable
	private Entity fuMaster;
	@Unique
	private Vec3d lastVelocity;

	public ItemEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}


	@Inject(at = @At("TAIL"), method = "tick")
	private void init1(CallbackInfo info) {
		int i = EnchantmentHelper.getLevel(Enchantments.LOYALTY,this.getStack());
		ItemStack itemStack = this.getStack();

		if(itemStack.getItem() instanceof AxeItem && i>0){

			if(FuC2SPacket.getUuid()!=null) {
				fuMaster = getWorld().getPlayerByUuid(FuC2SPacket.getUuid());//owner是通过右键投掷出去的玩家
			}
//			else if(getWorld().getClosestPlayer(this,100)!=null){
//				fuMaster = getWorld().getClosestPlayer(this.getOwner(),100);
//			}
			else {
				fuMaster = null;
			}

			if(fuMaster!=null && fuMaster.isAlive()){
//				BlockState blockState = getBlockStateAtPos();
				Vec3d fuMasterPos = fuMaster.getPos();
				if(this.isOnGround()) {//如果在地上
//					System.out.println(fuMaster);
//					System.out.println(fuMasterPos);
					// 计算从实体到玩家的方向向量
					Vec3d direction = fuMasterPos.subtract(this.getPos()).normalize();
					// 设置水平速度
					double horizontalSpeed = 0.5f; // 你想要施加的水平速度
					if(getWorld().isClient) {
						mafishmod$ItemEntityC2S(direction);
					}
					direction = FuC2SPacket.getDirection();
					if(direction!=null) {

//						if(abs(this.getVelocity().x)+abs(this.getVelocity().z)<0.1){
//							this.setVelocity(-direction.x * horizontalSpeed, 0.4, -direction.z * horizontalSpeed);
//						}

						lastVelocity = new Vec3d(direction.x * horizontalSpeed, 0.4, direction.z * horizontalSpeed);
						this.setVelocity(lastVelocity);

					}
				}
			}
		}
	}
	@Unique
	@Environment(EnvType.CLIENT)
	private void mafishmod$ItemEntityC2S(Vec3d direction){
		PacketByteBuf buf = PacketByteBufs.create();//C2S
		buf.writeInt(1);
		buf.writeVec3d(direction);
		ClientPlayNetworking.send(ModMessages.FU_ID, buf);
	}
}