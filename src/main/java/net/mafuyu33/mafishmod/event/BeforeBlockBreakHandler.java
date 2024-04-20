package net.mafuyu33.mafishmod.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.mafuyu33.mafishmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BeforeBlockBreakHandler implements PlayerBlockBreakEvents.Before{
    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        int k = BlockEnchantmentStorage.getLevel(Enchantments.PROTECTION,pos);//方块的破坏保护
        return k <= 0 || player.isCreative();
    }
}
