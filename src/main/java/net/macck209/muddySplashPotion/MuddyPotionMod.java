package net.macck209.muddySplashPotion;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuddyPotionMod implements ModInitializer {
	public static final String MOD_ID = "muddypotion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static GameRules.Key<GameRules.BooleanRule> splashPotionMuddying;

	@Override
	public void onInitialize() {

		LOGGER.info("Adding a new potion gamerule");

		splashPotionMuddying = GameRuleRegistry.register(
				"splashPotionMuddying",
				GameRules.Category.MISC,
				GameRuleFactory.createBooleanRule(true));

	}
}
