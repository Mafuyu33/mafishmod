package net.mafuyu33.mafishmod.mixin.enchantmentblockmixin.custom.buttonrandom;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.mafuyu33.mafishmod.event.UseBlockHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.StreamSupport;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	@Shadow public abstract void sendMessage(Text message, boolean overlay);

	@Shadow public abstract PlayerInventory getInventory();

	@Unique
	Enchantment randomPositiveEnchantment;
	@Unique
	Enchantment randomNegativeEnchantment;

	@Unique
	Item randomItem;
	@Unique
	ItemStack randomItemStack;
	@Unique
	StatusEffect randomPositiveEffect;
	@Unique
	StatusEffect randomNegativeEffect;
	@Unique
	int flag = -1;
	@Unique
	int buttonCount = 0;
	@Unique
	int randomNumber = (random.nextInt(10) + 1);
	@Unique
	String goodEvent;
	@Unique
	String badEvent;


	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		if (!getWorld().isClient) {
			if(goodEvent==null||badEvent==null){//初始化
				goodEvent = getRandomGoodEvent();
				badEvent = getRandomBadEvent();
			}
			// 获取玩家的位置和视线方向
			Vec3d playerPos = this.getCameraPosVec(1.0F);
			Vec3d playerLook = this.getRotationVec(1.0F);
			// 设置射线起点和方向
			Vec3d rayEnd = playerPos.add(playerLook.multiply(10)); // 假设射线长度为 10

			// 进行射线投射
			BlockHitResult blockHitResult = getWorld().raycast(new RaycastContext(playerPos, rayEnd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this));

			// 检查是否击中按钮方块
			if (getWorld().getBlockState(blockHitResult.getBlockPos()).isIn(BlockTags.BUTTONS)
					&& !UseBlockHandler.isButtonUsed && BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,blockHitResult.getBlockPos())>0) {
				//如果击中，持续向玩家输送文字
				this.sendMessage(
						Text.literal("获得 ")
								.append(Text.literal(goodEvent))
								.append(" ")
								.append(Text.literal(toRoman(randomNumber)))
								.append(", 但代价是 ")
								.append(Text.literal(badEvent).formatted(Formatting.RED)),
						true
				);
			}
			if (UseBlockHandler.isButtonUsed && BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,blockHitResult.getBlockPos())>0) {
				this.sendMessage(Text.literal("实现了..."),true);
				if(buttonCount==0) {//只有第一次会触发
					// 触发好事件
					executeEvent(randomNumber);
					//重新获得新的好事件和坏事件
					goodEvent = getRandomGoodEvent();
					badEvent = getRandomBadEvent();
				}
				buttonCount++;
			}
			if (buttonCount >= 30) {
				buttonCount = 0;
				UseBlockHandler.isButtonUsed = false;
				randomNumber = (random.nextInt(10) + 1);
			}
		}
	}
	@Unique
	private void executeEvent(int randomNumber) {
		switch (flag) {
			case 0:
				//applyRandomPositiveEffect
				this.addStatusEffect(new StatusEffectInstance(randomPositiveEffect, 100*randomNumber, randomNumber-1)); // 持续时间和等级可以根据需要调整
				this.addStatusEffect(new StatusEffectInstance(randomNegativeEffect, 100*randomNumber, randomNumber-1)); // 持续时间和等级可以根据需要调整
				break;
			case 1:
				// grantRandomEnchantment
				PlayerEntity player = (PlayerEntity) (Object) this; // 获取当前玩家实体
				ItemStack heldItem = player.getMainHandStack(); // 获取玩家手上的物品
				heldItem.addEnchantment(randomPositiveEnchantment, randomNumber);
				heldItem.addEnchantment(randomNegativeEnchantment, randomNumber);
				break;
			case 2:
				// grantRandomItem
				randomItemStack.setCount(randomNumber);
				if (!this.getInventory().insertStack(randomItemStack)) {
					this.dropItem(randomItem);
				}
				clearRandomItems(randomNumber,randomItemStack);

				// grantRandomItem
				break;
			case -1:
				break;
			default:
				// 处理未知事件
				break;
		}
	}
	@Unique
	private String getRandomGoodEvent() {
		List<StatusEffect> POSITIVE_EFFECTS = StreamSupport.stream(Registries.STATUS_EFFECT.spliterator(), false)
				.filter(effect -> effect.getCategory() == StatusEffectCategory.BENEFICIAL || effect.getCategory() == StatusEffectCategory.NEUTRAL)
				.toList();

		List<Enchantment> POSITIVE_ENCHANTMENTS = StreamSupport.stream(Registries.ENCHANTMENT.spliterator(), false)
				.filter(enchantment -> !enchantment.isCursed())
				.toList();

		List<Item> RANDOM_ITEMS = StreamSupport.stream(Registries.ITEM.spliterator(), false)
				.filter(item -> !item.isFood())
				.toList();

		int randomChoice = random.nextInt(4);
		if (randomChoice == 0) {
			flag = 0;
			return applyRandomPositiveEffect(POSITIVE_EFFECTS);
		} else if (randomChoice == 1) {
			flag = 1;
			return applyRandomPositiveEnchantment(POSITIVE_ENCHANTMENTS);
		} else {
			flag = 2;
			return grantRandomItem(RANDOM_ITEMS);
		}
	}
	@Unique
	private String getRandomBadEvent() {
		List<StatusEffect> NEGATIVE_EFFECTS = StreamSupport.stream(Registries.STATUS_EFFECT.spliterator(), false)
				.filter(effect -> effect.getCategory() == StatusEffectCategory.HARMFUL)
				.toList();

		List<Enchantment> NEGATIVE_ENCHANTMENTS = StreamSupport.stream(Registries.ENCHANTMENT.spliterator(), false)
				.filter(Enchantment::isCursed)
				.toList();
		if (flag == 0) {
			return "获得"+applyRandomNegativeEffect(NEGATIVE_EFFECTS);
		} else if (flag == 1) {
			return "获得"+applyRandomNegativeEnchantment(NEGATIVE_ENCHANTMENTS);
		} else if(flag == 2){
			return "献祭相同数量的物品";
		}else {
			return "null";
		}
	}
	@Unique
	private String applyRandomPositiveEnchantment(List<Enchantment> POSITIVE_ENCHANTMENTS) {
		if (!POSITIVE_ENCHANTMENTS.isEmpty()) {
			randomPositiveEnchantment = POSITIVE_ENCHANTMENTS.get(random.nextInt(POSITIVE_ENCHANTMENTS.size()));
			// 创建不包含等级的附魔名称
			MutableText enchantmentName = Text.translatable(randomPositiveEnchantment.getTranslationKey());
			return enchantmentName.getString();
		}
		return "一个正面附魔";
	}

	@Unique
	private String applyRandomNegativeEnchantment(List<Enchantment> NEGATIVE_ENCHANTMENTS) {
		if (!NEGATIVE_ENCHANTMENTS.isEmpty()) {
			randomNegativeEnchantment = NEGATIVE_ENCHANTMENTS.get(random.nextInt(NEGATIVE_ENCHANTMENTS.size()));
			// 创建不包含等级的附魔名称
			MutableText enchantmentName = Text.translatable(randomNegativeEnchantment.getTranslationKey());
			return enchantmentName.getString();
		}
		return "一个反面附魔";
	}
	@Unique
	private String applyRandomPositiveEffect(List<StatusEffect> POSITIVE_EFFECTS ) {
		if (!POSITIVE_EFFECTS.isEmpty()) {
			randomPositiveEffect = POSITIVE_EFFECTS.get(random.nextInt(POSITIVE_EFFECTS.size()));
			return randomPositiveEffect.getName().getString();
		}
		return "一个正面效果";
	}
	@Unique
	private String applyRandomNegativeEffect(List<StatusEffect> NEGATIVE_EFFECTS ) {
		if (!NEGATIVE_EFFECTS.isEmpty()) {
			randomNegativeEffect = NEGATIVE_EFFECTS.get(random.nextInt(NEGATIVE_EFFECTS.size()));
			return randomNegativeEffect.getName().getString();
		}
		return "一个负面效果";
	}
	@Unique
	private void clearRandomItems(int itemCount, ItemStack priorityItem) {
		PlayerEntity player = (PlayerEntity) (Object) this; // 获取当前玩家实体
		List<ItemStack> inventory = player.getInventory().main; // 获取玩家物品栏

		Random random = new Random();
		int itemsCleared = 0;

		// 清除其他随机物品
		while (itemsCleared < itemCount) {
			int slot = random.nextInt(inventory.size());
			ItemStack stack = inventory.get(slot);

			if (!stack.isEmpty() && !stack.isOf(priorityItem.getItem())) {
				int removeCount = Math.min(stack.getCount(), itemCount - itemsCleared);
				stack.decrement(removeCount);
				itemsCleared += removeCount;

				if (stack.isEmpty()) {
					inventory.set(slot, ItemStack.EMPTY); // 将堆栈设置为空，而不是移除
				}
			}

			// 如果所有非优先物品已检查完，跳出循环
			if (itemsCleared < itemCount) {
				boolean hasNonPriorityItems = false;
				for (ItemStack itemStack : inventory) {
					if (!itemStack.isEmpty() && !itemStack.isOf(priorityItem.getItem())) {
						hasNonPriorityItems = true;
						break;
					}
				}
				if (!hasNonPriorityItems) {
					break;
				}
			}
		}

		// 如果还有剩余的数量需要清除，最后清除指定的物品
		if (itemsCleared < itemCount) {
			for (int i = 0; i < inventory.size() && itemsCleared < itemCount; i++) {
				ItemStack stack = inventory.get(i);

				if (stack.isOf(priorityItem.getItem())) {
					int removeCount = Math.min(stack.getCount(), itemCount - itemsCleared);
					stack.decrement(removeCount);
					itemsCleared += removeCount;

					if (stack.isEmpty()) {
						inventory.set(i, ItemStack.EMPTY); // 将堆栈设置为空，而不是移除
					}
				}
			}
		}
	}

	@Unique
	private String grantRandomItem( List<Item> RANDOM_ITEMS) {
		if (!RANDOM_ITEMS.isEmpty()) {
			randomItem = RANDOM_ITEMS.get(random.nextInt(RANDOM_ITEMS.size()));
			randomItemStack = new ItemStack(randomItem);
			return randomItem.getName().getString();
		}
		return "一个随机物品";
	}

	@Unique
	private static final Map<Integer, String> romanMap = new HashMap<>();

	static {
		romanMap.put(1, "I");
		romanMap.put(2, "II");
		romanMap.put(3, "III");
		romanMap.put(4, "IV");
		romanMap.put(5, "V");
		romanMap.put(6, "VI");
		romanMap.put(7, "VII");
		romanMap.put(8, "VIII");
		romanMap.put(9, "IX");
		romanMap.put(10, "X");
	}

	@Unique
	private static String toRoman(int number) {
		return romanMap.getOrDefault(number, "");
	}


}