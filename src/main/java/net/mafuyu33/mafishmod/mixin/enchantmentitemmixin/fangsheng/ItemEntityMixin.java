package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.fangsheng;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
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
import net.minecraft.item.PickaxeItem;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements Ownable {
//	@Unique
//	public int returnTimer;
	@Shadow public abstract ItemStack getStack();

//	@Shadow @Nullable private Entity thrower;//通过Q键投掷出去的

	@Shadow @Nullable public abstract Entity getOwner();
//	@Unique
//	@Nullable
//	private Entity fuMaster;
//	@Unique
//	private Vec3d lastVelocity;
	@Unique
	private int cd=0;


	@Shadow public abstract void setPickupDelay(int pickupDelay);

	public ItemEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}



	@Override
	protected void onBlockCollision(BlockState state) {
		super.onBlockCollision(state);
		if(state.isIn(BlockTags.PICKAXE_MINEABLE)){
			dropStack(state.getBlock().asItem().getDefaultStack());
		}
	}
	@Inject(at = @At("TAIL"), method = "setThrower")
	private void init(CallbackInfo info) {
		int i = EnchantmentHelper.getLevel(ModEnchantments.FANGSHENG,this.getStack());
		ItemStack itemStack = this.getStack();
		if(itemStack.getItem() instanceof PickaxeItem && i>0) {
			this.setPickupDelay(200);
		}
		cd = 0;
	}
	@Inject(at = @At("TAIL"), method = "tick")
	private void init1(CallbackInfo info) {

		int i = EnchantmentHelper.getLevel(ModEnchantments.FANGSHENG,this.getStack());
		ItemStack itemStack = this.getStack();

		if(itemStack.getItem() instanceof PickaxeItem && i>0) {

			if(this.isOnGround()) {//如果在地上
				// 随机一个方向
				double angle = random.nextDouble() * 2 * Math.PI; // 生成一个随机角度
				double x = Math.cos(angle); // 计算 x 分量
				double z = Math.sin(angle); // 计算 z 分量
				// 创建 Vec3d 对象表示方向向量
				Vec3d direction = new Vec3d(x, 0.0, z);
				// 设置水平速度
				double horizontalSpeed = 0.3f; // 你想要施加的水平速度
				if(getWorld().isClient) {
					mafishmod$ItemEntityC2S(direction);
				}
				direction = FuC2SPacket.getDirection();
				if(direction!=null) {
					this.setVelocity(new Vec3d(direction.x * horizontalSpeed, 0.4, direction.z * horizontalSpeed));
				}
			}

			Iterable<VoxelShape> blockCollisions = this.getWorld().getBlockCollisions(this, this.getBoundingBox().expand(0.05));
			List<BlockPos> hitBlockPos = new ArrayList<>();
			// 遍历 blockCollisions 中的每个 VoxelShapeE
			for (VoxelShape voxelShape : blockCollisions) {
				// 获取与当前 VoxelShape 相关的方块位置
				voxelShape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
					// 计算方块位置，使用 Math.round() 进行四舍五入
					BlockPos pos = new BlockPos((int) Math.floor((minX + maxX) / 2.0),
                            (int) Math.floor((minY + maxY) / 2.0),
                            (int) Math.floor((minZ + maxZ) / 2.0));
					hitBlockPos.add(pos);
				});
			}
			for (BlockPos pos : hitBlockPos) {
				// 这里可以对每个方块位置进行你需要的操作
				BlockState blockState = this.getWorld().getBlockState(pos);
//				System.out.println("Hit Block Position: " + pos+ "Hit Block blockState: "+blockState);
				if(blockState.isIn(BlockTags.PICKAXE_MINEABLE) && cd == 0){
//					System.out.println(3);
					this.getWorld().breakBlock(pos,true);
					cd = 10;
				}
			}
			if(cd > 0) {
				cd = cd - 1;
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