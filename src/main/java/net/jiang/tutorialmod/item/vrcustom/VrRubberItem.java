package net.jiang.tutorialmod.item.vrcustom;

import net.blf02.vrapi.api.IVRAPI;
import net.jiang.tutorialmod.mixinhelper.VrRubberItemHelper;
import net.jiang.tutorialmod.particle.ModParticles;
import net.jiang.tutorialmod.particle.ParticleStorage;
import net.jiang.tutorialmod.vr.VRPlugin;
import net.jiang.tutorialmod.vr.VRPluginVerify;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class VrRubberItem extends Item {
    public VrRubberItem(Settings settings) {
        super(settings);
    }

    public static boolean isErasing = false;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient) {
            isErasing = !isErasing;
            user.sendMessage(Text.literal((String.valueOf("切换橡皮模式"))), true);
        }
        return TypedActionResult.success(user.getStackInHand(hand),world.isClient());
    }
}
