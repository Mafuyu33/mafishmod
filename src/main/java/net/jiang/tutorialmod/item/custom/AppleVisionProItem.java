package net.jiang.tutorialmod.item.custom;

import com.google.common.collect.ImmutableMap;
import com.ibm.icu.util.CharsTrie;
import com.mojang.datafixers.types.templates.List;
import net.jiang.tutorialmod.item.ModArmorMaterials;
import net.jiang.tutorialmod.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.world.World;

import java.util.Map;

public class AppleVisionProItem extends ArmorItem {
    private static final Map<ArmorMaterial, StatusEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, StatusEffectInstance>())
                    .put(ModArmorMaterials.RUBY, new StatusEffectInstance(ModStatusEffects.IRONMAN, 210, 0,
                            false, false, true)).build();


    public AppleVisionProItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient()) {
            if(entity instanceof PlayerEntity player && hasSuitOfArmorOn(player)) {
                evaluateArmorEffects(player);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
    private void evaluateArmorEffects(PlayerEntity player) {
        for (Map.Entry<ArmorMaterial, StatusEffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            StatusEffectInstance mapStatusEffect = entry.getValue();
            player.addStatusEffect(new StatusEffectInstance(mapStatusEffect));
        }
    }

    private boolean hasSuitOfArmorOn(PlayerEntity player) {

        ItemStack helmet = player.getInventory().getArmorStack(3);
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.HEAD);
        if (itemStack.isOf(ModItems.APPLE_VISION_PRO)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40, 3));
        }
        return !helmet.isEmpty();
    }
}

