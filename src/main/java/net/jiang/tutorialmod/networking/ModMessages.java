package net.jiang.tutorialmod.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.jiang.tutorialmod.TutorialMod;
import net.jiang.tutorialmod.networking.packet.ExampleC2SPacket;
import net.jiang.tutorialmod.networking.packet.ExampleS2CPacket;
import net.jiang.tutorialmod.networking.packet.ShieldDashC2SPacket;
import net.minecraft.util.Identifier;

public class ModMessages {

    public static final Identifier EXAMPLE_ID = new Identifier(TutorialMod.MOD_ID, "example");
    public static final Identifier Shield_Dash_ID = new Identifier(TutorialMod.MOD_ID, "shield_dash");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(Shield_Dash_ID, ShieldDashC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleS2CPacket::receive);
    }
}
