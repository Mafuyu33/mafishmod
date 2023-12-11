package net.jiang.tutorialmod;

import net.fabricmc.api.ModInitializer;

import net.jiang.tutorialmod.Item.ModItemGroups;
import net.jiang.tutorialmod.Item.ModItems;
import net.jiang.tutorialmod.block.ModBlocks;
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
	}
}