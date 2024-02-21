package net.jiang.tutorialmod.item.custom;

import dev.architectury.event.events.common.TickEvent;
import net.blf02.vrapi.api.IVRAPI;
import net.jiang.tutorialmod.vr.VRPlugin;
import net.jiang.tutorialmod.vr.VRPluginVerify;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class BreadSwordHotItem extends SwordItem {
    public BreadSwordHotItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ItemStack offhanditemStack = user.getOffHandStack();
//        System.out.println(offhanditemStack.getOrCreateNbt());
        if (world.isClient && VRPluginVerify.hasAPI && VRPlugin.API.playerInVR(user)) {   //有MC-VR-API并且在VR中的时候
            Vec3d mainController = getControllerPosition(user,0);
            Vec3d offController = getControllerPosition(user,1);
            user.sendMessage(Text.literal("mainController"+mainController),false);
            user.sendMessage(Text.literal("offController"+offController),false);


        }
        return TypedActionResult.success(this.getDefaultStack(), world.isClient());
    }

    public static Vec3d getControllerPosition(PlayerEntity player, int controllerIndex) {
        IVRAPI vrApi = VRPlugin.API; // 这里假设 VRPlugin 是你的 VR 插件类
        if (vrApi != null && vrApi.apiActive(player)) {
            return vrApi.getVRPlayer(player).getController(controllerIndex).position();
        }
        return null;
    }
}
