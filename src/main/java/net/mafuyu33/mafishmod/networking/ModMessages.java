package net.mafuyu33.mafishmod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mafuyu33.mafishmod.TutorialMod;
import net.mafuyu33.mafishmod.networking.packet.*;
import net.minecraft.util.Identifier;

public class ModMessages {

    public static final Identifier EXAMPLE_ID = new Identifier(TutorialMod.MOD_ID, "example");
    public static final Identifier Shield_Dash_ID = new Identifier(TutorialMod.MOD_ID, "shield_dash");
    public static final Identifier Bow_Dash_ID = new Identifier(TutorialMod.MOD_ID, "bow_dash");
    public static final Identifier PARTICLE_DATA_ID = new Identifier(TutorialMod.MOD_ID, "particle_data");
    public static final Identifier NEVER_GONNA_ID = new Identifier(TutorialMod.MOD_ID, "never_gonna");
    public static final Identifier GAME_OPTIONS_ID = new Identifier(TutorialMod.MOD_ID, "game_option");
    public static final Identifier THROW_POWER_ID = new Identifier(TutorialMod.MOD_ID, "throw_power");
    public static final Identifier BELL_SOUND_ID = new Identifier(TutorialMod.MOD_ID, "bell_sound");

//    public static final Identifier Particle_Color_ID = new Identifier(TutorialMod.MOD_ID, "particle_color");
    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(Shield_Dash_ID, ShieldDashC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(Bow_Dash_ID, BowDashC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(PARTICLE_DATA_ID, ParticleDataC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GAME_OPTIONS_ID,GameOptionsC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(THROW_POWER_ID,ThrowPowerC2SPacket::receive);
//        ServerPlayNetworking.registerGlobalReceiver(Particle_Color_ID, ParticleColorC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(NEVER_GONNA_ID, NeverGonnaS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(BELL_SOUND_ID, BellSoundS2CPacket::receive);
    }
}
