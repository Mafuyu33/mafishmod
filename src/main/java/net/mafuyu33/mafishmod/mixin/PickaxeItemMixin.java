package net.mafuyu33.mafishmod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;


@Mixin(PickaxeItem.class)
public abstract class PickaxeItemMixin extends Item {
    public PickaxeItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = context.getStack();
        PlayerEntity playerEntity = context.getPlayer();
        LivingEntity user = ((LivingEntity) context.getPlayer());

        int k = EnchantmentHelper.getLevel(Enchantments.INFINITY, itemStack);
        if (k > 0 && !world.isClient) {
            world.breakBlock(blockPos, true);
            world.setBlockState(blockPos, (blockState.getBlock()).getDefaultState(), 3);
            EquipmentSlot equipmentSlot = itemStack.equals(playerEntity.getEquippedStack(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
            itemStack.damage(1, user, (userx) -> {
                userx.sendEquipmentBreakStatus(equipmentSlot);
            });
        }
        return super.useOnBlock(context);
    }

}




