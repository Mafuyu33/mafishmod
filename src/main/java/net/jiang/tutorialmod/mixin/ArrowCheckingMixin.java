//package net.jiang.tutorialmod.mixin;
//
//import com.mojang.authlib.GameProfile;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerAbilities;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.item.RangedWeaponItem;
//import net.minecraft.screen.PlayerScreenHandler;
//import net.minecraft.util.Arm;
//import net.minecraft.util.Uuids;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import org.spongepowered.asm.mixin.*;
//import net.minecraft.inventory.EnderChestInventory;
//import net.minecraft.inventory.Inventory;
//import net.minecraft.inventory.StackReference;
//
//import java.util.Optional;
//import java.util.function.Predicate;
//
//@Mixin(PlayerEntity.class)
//public abstract class ArrowCheckingMixin extends LivingEntity {
//
//
//	protected ArrowCheckingMixin(EntityType<? extends LivingEntity> entityType, World world) {
//		super(entityType, world);
//	}
//
//	@Shadow
//	public abstract Iterable<ItemStack> getArmorItems();
//
//	@Shadow
//	public abstract ItemStack getEquippedStack(EquipmentSlot slot);
//
//	@Shadow
//	public abstract void equipStack(EquipmentSlot slot, ItemStack stack);
//
//	@Shadow
//	public abstract Arm getMainArm();
//
//	@Final
//	@Shadow
//	private final PlayerAbilities abilities = new PlayerAbilities();
//
//	@Unique
//	private final PlayerInventory inventory = new PlayerInventory(this);
//
//	/**
//	 * @author
//	 * Mafuyu33
//	 * @reason
//	 * Checking ModArrow
//	 */
//	@Overwrite
//	public ItemStack getProjectileType(ItemStack stack) {
////		World world=getWorld();
////		Entity entity=stack.getHolder();
////		PlayerEntity player = ((PlayerEntity) entity);
//
//
//
//		if (!(stack.getItem() instanceof RangedWeaponItem)) {
//			return ItemStack.EMPTY;
//		} else {
//			Predicate<ItemStack> predicate = ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
//			ItemStack itemStack = RangedWeaponItem.getHeldProjectile(this, predicate);
//			if (!itemStack.isEmpty()) {
//				return itemStack;
//			} else {
//				predicate = ((RangedWeaponItem)stack.getItem()).getProjectiles();
//
//
//
//				for(int i = 0; i < this.inventory.size(); ++i) {
//					ItemStack itemStack2 = this.inventory.getStack(i);
//					if (predicate.test(itemStack2)) {
//						return itemStack2;
//					}
//				}
//
//				return this.abilities.creativeMode ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
//			}
//		}
//	}
//}