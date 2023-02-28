package net.macck209.muddySplashPotion.mixin;

import net.macck209.muddySplashPotion.MuddyPotionMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;


@Mixin(PotionEntity.class)
public abstract class MuddyPotionMixin{

	@Inject(method = "onBlockHit", at = @At("TAIL"))
	public void injectBlockHitMethod(BlockHitResult blockHitResult, CallbackInfo ci){
		// Getting this world variable
		World world = ((PotionEntity) (Object) this).world;

		// Necessary variables copied from injected method
		ItemStack itemStack = ((PotionEntity) (Object) this).getStack();
		List<StatusEffectInstance> list = PotionUtil.getPotionEffects(itemStack);
		boolean bl = list.isEmpty();
		BlockPos blockPos = blockHitResult.getBlockPos();

		// Apply muddying if potion has no effects & gamerule is on
		GameRules gamerules = world.getGameRules();
		if(bl && gamerules.getBoolean(MuddyPotionMod.splashPotionMuddying)){

			// Two different sizes of mud blobs (lists of fixed vectors)
			// Added cuz repetitiveness looked boring and unnatural
			int[][] smallBlob ={
					{-1,-1,0}, {0,-1,0}, {1,-1,0}, {0,-1,-1}, {0,-1,1},
					{0,0,0}, {0,0,-1}, {-1,0,-1}, {-1,0,0}, {1,0,0}, {1,0,1}, {0,0,1}, {-1,0,1}, {1,0,-1},
					{-1,1,0}, {1,1,0}, {0,1,-1}, {0,1,1}, {0,1,0}
			};
			int[][] bigBlob ={
					{0,-2,0},
					{0,-1,0}, {0,-1,-1}, {-1,-1,-1}, {-1,-1,0}, {1,-1,0}, {1,-1,1}, {0,-1,1}, {-1,-1,1}, {1,-1,-1},
					{0,0,0}, {0,0,-1}, {-1,0,-1}, {-1,0,0}, {1,0,0}, {1,0,1}, {0,0,1}, {-1,0,1}, {1,0,-1}, {2,0,0}, {0,0,2}, {-2,0,0}, {0,0,-2},
					{0,1,0}, {0,1,-1}, {-1,1,-1}, {-1,1,0}, {1,1,0}, {1,1,1}, {0,1,1}, {-1,1,1}, {1,1,-1},
					{0,2,0}
			};

			// Random blob
			Random rnd = new Random();

			for(int[] i : (rnd.nextInt(3)==1?smallBlob:bigBlob)){
				BlockPos blockPos1 = new BlockPos(i[0]+blockPos.getX(),i[1]+blockPos.getY(),i[2]+blockPos.getZ());

				// Check if one of 4 modifiable blocks
				Block temp = world.getBlockState(blockPos1).getBlock();
				if(temp.equals(Blocks.DIRT) ||
					temp.equals(Blocks.GRASS_BLOCK) ||
					temp.equals(Blocks.COARSE_DIRT) ||
					temp.equals(Blocks.ROOTED_DIRT)){

					world.setBlockState(blockPos1, Blocks.MUD.getDefaultState());
				}
			}
		}
	}

}
