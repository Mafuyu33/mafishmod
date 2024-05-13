package net.mafuyu33.mafishmod.mixin.enchantmentitemmixin.throwableaxe;

import net.mafuyu33.mafishmod.entity.FuProjectileEntity;
import net.mafuyu33.mafishmod.util.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends MiningToolItem {
	public AxeItemMixin(float attackDamage, float attackSpeed, ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
		super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

		boolean CFG_isFuThrowable = ConfigHelper.isFuThrowable();
		ItemStack itemStack = user.getStackInHand(hand);

		if(CFG_isFuThrowable) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(),
					SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));

			user.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!user.getAbilities().creativeMode) {
				itemStack.damage(1, user, (p) -> {
					p.sendToolBreakStatus(user.getActiveHand());
				});
				user.getInventory().removeOne(itemStack);
			}

			if (!world.isClient) {
				FuProjectileEntity fuProjectileEntity = new FuProjectileEntity(user, world);
				fuProjectileEntity.setItem(itemStack);
				fuProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
				world.spawnEntity(fuProjectileEntity);
			}

			return TypedActionResult.success(itemStack, world.isClient());
		}else {
			return TypedActionResult.pass(itemStack);
		}
	}

//	@Inject(at = @At("HEAD"), method = "loadWorld")
//	private void init(CallbackInfo info) {
//		// This code is injected into the start of MinecraftServer.loadWorld()V
//	}
}