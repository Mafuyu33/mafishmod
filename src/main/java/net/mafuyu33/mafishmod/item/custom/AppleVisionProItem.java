//package net.mafuyu33.mafishmod.item.custom;
//
//import com.google.common.collect.ImmutableMap;
//import com.ibm.icu.util.CharsTrie;
//import com.mojang.datafixers.types.templates.List;
//import net.mafuyu33.mafishmod.item.ModArmorMaterials;
//import net.mafuyu33.mafishmod.item.ModItems;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.entity.effect.StatusEffectInstance;
//import net.minecraft.entity.effect.StatusEffects;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.*;
//import net.minecraft.potion.PotionUtil;
//import net.minecraft.registry.tag.FluidTags;
//import net.minecraft.world.World;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static net.mafuyu33.mafishmod.item.ModItems.COOL_GLASS;
//
//public class AppleVisionProItem extends ArmorItem {
//    private HashMap<PlayerEntity, Boolean> playerCoolGlassMap = new HashMap<>();
//    private static final Map<ArmorMaterial, StatusEffectInstance> MATERIAL_TO_EFFECT_MAP =
//            (new ImmutableMap.Builder<ArmorMaterial, StatusEffectInstance>())
//                    .put(ModArmorMaterials.RUBY, new StatusEffectInstance(ModStatusEffects.IRONMAN, 210, 0,
//                            false, false, true)).build();
//
//
//    public AppleVisionProItem(ArmorMaterial material, Type type, Settings settings) {
//        super(material, type, settings);
//    }
//
//
//    @Override
//    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
//        if(!world.isClient() && entity instanceof PlayerEntity player) {
//            if(hasSuitOfArmorOn(player)) {
//                evaluateArmorEffects(player);
//            }
//            damageWhenGetOffShen(player);
//        }
//        super.inventoryTick(stack, world, entity, slot, selected);
//    }
//
//    private void evaluateArmorEffects(PlayerEntity player) {
//        for (Map.Entry<ArmorMaterial, StatusEffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
//            StatusEffectInstance mapStatusEffect = entry.getValue();
//            player.addStatusEffect(new StatusEffectInstance(mapStatusEffect));
//        }
//    }
//
//    private boolean hasSuitOfArmorOn(PlayerEntity player) {
//
//        ItemStack helmet = player.getInventory().getArmorStack(3);
//        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.HEAD);
//        if (itemStack.isOf(ModItems.APPLE_VISION_PRO)) {
//            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40, 3));
//        }
//        return !helmet.isEmpty();
//    }
//
//    public void damageWhenGetOffShen(PlayerEntity player) {
////        ArmorItem breastplate = ((ArmorItem)player.getInventory().getArmorStack(2).getItem());
////        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.CHEST);
//
//        if(player.isDead()){
//            // 如果玩家死亡，重置其在 HashMap 中的状态
//            playerCoolGlassMap.put(player, false);
//            return; // 由于玩家已经死亡，不需要继续执行剩余的代码
//        }
//        // 获取玩家当前胸甲
//        ItemStack currentChestplate = player.getEquippedStack(EquipmentSlot.CHEST);
//        Item currentChestplateItem = currentChestplate.getItem();
//
//        // 检查玩家是否之前穿着 COOL_GLASS
//        boolean wasWearingCoolGlass = playerCoolGlassMap.getOrDefault(player, false);
//
//        // 检查玩家是否当前穿着 COOL_GLASS
//        boolean isWearingCoolGlass = currentChestplateItem == COOL_GLASS;
//
//        // 如果玩家之前穿着，但现在没有穿着，则对玩家造成一次伤害
//        if (wasWearingCoolGlass && !isWearingCoolGlass) {
//            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 10, 0));
//        }
//
//        // 更新玩家的装备状态
//        playerCoolGlassMap.put(player, isWearingCoolGlass);
//
//
//    }
//
//}