package net.mafuyu33.mafishmod.item.custom;

import net.mafuyu33.mafishmod.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MathSwordItem extends SwordItem {
    public MathSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    private static boolean mathMode = false;
    private static int level = 0;
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        level = EnchantmentHelper.getLevel(ModEnchantments.VERY_EASY, itemStack);

        if (!world.isClient) {
            mathMode = !mathMode;
        }

        if(mathMode){
            user.sendMessage(Text.literal(("数学领域展开")),true);
        }else
        {
            user.sendMessage(Text.literal(("数学领域关闭")),true);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    public static boolean isMathMode() {
        return mathMode;
    }

    public static int getLevel() {
        return level;
    }
}