package net.jiang.tutorialmod.item.custom;

import net.blf02.vrapi.common.VRAPI;
import net.jiang.tutorialmod.mixinhelper.BlockEnchantmentHelper;
import net.jiang.tutorialmod.particle.ParticleStorage;
import net.jiang.tutorialmod.VRPlugin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.Particle;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
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

        int level = EnchantmentHelper.getLevel( Enchantments.KNOCKBACK,user.getOffHandStack());
        user.sendMessage((Text.literal((String.valueOf(level)))),false);
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity user = context.getPlayer();

//        if (VRPlugin.isPlayerInVR(user)) {
//            user.sendMessage(Text.literal("在VR里"),false);
//        } else {
//            user.sendMessage(Text.literal("不在VR里"),false);
//        }

        BlockPos blockPos = context.getBlockPos();
        System.out.println(BlockEnchantmentHelper.getEnchantment(blockPos));

        return super.useOnBlock(context);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks % 20==0){
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE,10,0));
        }
    }
}
