package net.jiang.tutorialmod.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ColliableItem extends Item {
    public ColliableItem(Settings settings) {
        super(settings);
    }
    private static boolean colliable = false;
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            colliable = !colliable;
            user.sendMessage(Text.literal(("已切换碰撞模式")),true);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    public static boolean isColliable(){
        return colliable;
    }
}
