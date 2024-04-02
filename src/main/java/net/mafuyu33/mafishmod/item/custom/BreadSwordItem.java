package net.mafuyu33.mafishmod.item.custom;

import net.mafuyu33.mafishmod.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BreadSwordItem extends SwordItem {

    public BreadSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

//        int level = EnchantmentHelper.getLevel( Enchantments.KNOCKBACK,user.getOffHandStack());
//        user.sendMessage((Text.literal((String.valueOf(level)))),false);
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity user = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
//        if (VRPlugin.canRetrieveData(user)) {
//            user.sendMessage(Text.literal("在VR里"),false);
//        } else {
//            user.sendMessage(Text.literal("不在VR里"),false);
//        }

    if(!world.isClient) {
        NbtList nbtList = BlockEnchantmentStorage.getEnchantmentsAtPosition(blockPos);
            System.out.println(nbtList);
            if(user!=null) {
                user.sendMessage((Text.literal((String.valueOf(nbtList)))), false);
            }
    }
        return super.useOnBlock(context);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks % 20==0){
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE,10,0));
        }
    }
}
