package net.mafuyu33.mafishmod.sound;

import net.mafuyu33.mafishmod.TutorialMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent METAL_DETECTOR_FOUND_ORE = registerSoundEvent("metal_detector_found_ore");
    public static final SoundEvent SOUND_BLOCK_BREAK = registerSoundEvent("sound_block_break");
    public static final SoundEvent SOUND_BLOCK_STEP = registerSoundEvent("sound_block_step");
    public static final SoundEvent SOUND_BLOCK_PLACE = registerSoundEvent("sound_block_place");
    public static final SoundEvent SOUND_BLOCK_HIT = registerSoundEvent("sound_block_hit");
    public static final SoundEvent SOUND_BLOCK_FALL = registerSoundEvent("sound_block_fall");
    public static final SoundEvent DASH_SOUND = registerSoundEvent("dash_sound");
    public static final SoundEvent NEVER1 = registerSoundEvent("never1");
    public static final SoundEvent NEVER2 = registerSoundEvent("never2");
    public static final SoundEvent NEVER3 = registerSoundEvent("never3");
    public static final SoundEvent NEVER4 = registerSoundEvent("never4");
    public static final SoundEvent NEVER5 = registerSoundEvent("never5");
    public static final SoundEvent NEVER6 = registerSoundEvent("never6");
    public static final SoundEvent NEVER7 = registerSoundEvent("never7");
    public static final SoundEvent NEVER8 = registerSoundEvent("never8");
    public static final SoundEvent NEVER9 = registerSoundEvent("never9");
    public static final SoundEvent NEVER10 = registerSoundEvent("never10");
    public static final SoundEvent CHEESE_BERGER_MAN = registerSoundEvent("cheese_berger_man");
    public static final SoundEvent CHEESE_BERGER_CAT = registerSoundEvent("cheese_berger_cat");
    public static final SoundEvent PIN = registerSoundEvent("pin");


    public static final BlockSoundGroup SOUND_BLOCK_SOUNDS = new BlockSoundGroup(1f, 1f,
            ModSounds.SOUND_BLOCK_BREAK, ModSounds.SOUND_BLOCK_STEP, ModSounds.SOUND_BLOCK_PLACE,
            ModSounds.SOUND_BLOCK_HIT, ModSounds.SOUND_BLOCK_FALL);


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(TutorialMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        TutorialMod.LOGGER.info("注册一个声音 " + TutorialMod.MOD_ID);
    }
}
