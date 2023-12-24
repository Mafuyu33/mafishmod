package net.jiang.tutorialmod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.jiang.tutorialmod.item.ModItemGroups;
import net.jiang.tutorialmod.item.ModItems;
import net.jiang.tutorialmod.block.ModBlocks;
import net.jiang.tutorialmod.item.custom.ModStatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TutorialMod implements ModInitializer {
	public static final String MOD_ID="tutorialmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ModStatusEffects.registerModEffect();

		FuelRegistry.INSTANCE.add(ModItems.COAL_BRIQUEITE,200);

	}
}