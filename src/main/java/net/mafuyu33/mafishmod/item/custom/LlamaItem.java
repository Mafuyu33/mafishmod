package net.mafuyu33.mafishmod.item.custom;

import net.mafuyu33.mafishmod.entity.CustomLlamaSpitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class LlamaItem extends Item {
    public LlamaItem(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_LLAMA_SPIT, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            CustomLlamaSpitEntity llamaSpitEntity = new CustomLlamaSpitEntity(world,user);
            llamaSpitEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
            world.spawnEntity(llamaSpitEntity);
        }
        return super.use(world, user, hand);
    }
}
