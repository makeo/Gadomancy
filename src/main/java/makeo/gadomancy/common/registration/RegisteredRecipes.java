package makeo.gadomancy.common.registration;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.crafting.InfusionUpgradeRecipe;
import makeo.gadomancy.common.crafting.RecipeStickyJar;
import makeo.gadomancy.common.research.SimpleResearchItem;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

import java.util.Arrays;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 16.06.2015 01:17
 */
public class RegisteredRecipes {
    private RegisteredRecipes() {}

    public static AspectList costsNodeManipulatorMultiblock = new AspectList().add(Aspect.FIRE, 100).add(Aspect.WATER, 100).add(Aspect.EARTH, 100).add(Aspect.AIR, 100).add(Aspect.ORDER, 100).add(Aspect.ENTROPY, 100);

    public static List multiblockNodeManipulator;

    public static InfusionRecipe recipeGolemSilverwood;
    public static InfusionRecipe[] recipesGolemRunicShield;
    public static InfusionRecipe recipeGolemCoreBreak;
    public static InfusionRecipe recipeInfusionClaw;
    public static InfusionRecipe recipeNodeManipulator;
    public static InfusionRecipe recipeRandomizationFocus;

    public static IArcaneRecipe recipeStickyJar;
    public static IArcaneRecipe recipeArcaneDropper;
    public static IArcaneRecipe recipeRemoteJar;

    public static void init() {
        AdditionalGolemType typeSilverwood = RegisteredGolemStuff.typeSilverwood;
        recipeGolemSilverwood = ThaumcraftApi.addInfusionCraftingRecipe(SimpleResearchItem.getFullName("GOLEMSILVERWOOD"),
                /*new ItemStack(Items.bread, 1, 0),*/new ItemStack(typeSilverwood.getPlacerItem(), 1, typeSilverwood.getEnumEntry().ordinal()),
                7, new AspectList().add(Aspect.MOTION, 16).add(Aspect.MAGIC, 16).add(Aspect.MAN, 16).add(Aspect.SENSES, 16),
                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1), new ItemStack[]{ new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 9), new ItemStack(ConfigItems.itemResource, 1, 3), new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 9), new ItemStack(ConfigItems.itemResource, 1, 3)});

        recipesGolemRunicShield = InfusionUpgradeRecipe.createRecipes(SimpleResearchItem.getFullName("GOLEMSILVERWOOD"), RegisteredGolemStuff.upgradeRunicShield, 5,
                new AspectList().add(Aspect.MAGIC, 16).add(Aspect.ENERGY, 16).add(Aspect.ARMOR, 8).add(Aspect.AURA, 4),
                new ItemStack[]{new ItemStack(ConfigItems.itemGirdleRunic, 1, 0), new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 14)});
        ThaumcraftApi.getCraftingRecipes().addAll(Arrays.asList(recipesGolemRunicShield));

        recipeStickyJar = new RecipeStickyJar();
        ThaumcraftApi.getCraftingRecipes().add(recipeStickyJar);
        //GameRegistry.addRecipe(recipeStickyJar);

        recipeArcaneDropper = ThaumcraftApi.addArcaneCraftingRecipe(SimpleResearchItem.getFullName("ARCANEDROPPER"), new ItemStack(RegisteredBlocks.blockArcaneDropper), new AspectList().add(Aspect.ORDER, 14).add(Aspect.AIR, 14).add(Aspect.EARTH, 10),
                "SDS", "BAB", "SSS", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), 'D', new ItemStack(Blocks.dropper), 'A', new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), 'B', new ItemStack(ConfigBlocks.blockWoodenDevice));

        recipeGolemCoreBreak = ThaumcraftApi.addInfusionCraftingRecipe(SimpleResearchItem.getFullName("GOLEMCOREBREAK"),
                new ItemStack(RegisteredItems.itemGolemCoreBreak),
                3, new AspectList().add(Aspect.ENTROPY, 16).add(Aspect.TOOL, 24).add(Aspect.MECHANISM, 16),
                new ItemStack(ConfigItems.itemGolemCore, 1, 3), new ItemStack[] {new ItemStack(ConfigItems.itemShovelElemental), new ItemStack(ConfigItems.itemPickElemental), new ItemStack(ConfigItems.itemAxeElemental)});

        recipeInfusionClaw = ThaumcraftApi.addInfusionCraftingRecipe(SimpleResearchItem.getFullName("GOLEMCOREBREAK"),
                new ItemStack(RegisteredBlocks.blockInfusionClaw),
                10, new AspectList().add(Aspect.ELDRITCH, 25).add(Aspect.MECHANISM, 20).add(Aspect.MAGIC, 16).add(Aspect.ORDER, 20).add(Aspect.DARKNESS, 12),
                new ItemStack(ConfigBlocks.blockStoneDevice, 1, 5), new ItemStack[]{new ItemStack(ConfigItems.itemFocusPrimal), /*new ItemStack(Items.redstone),*/ new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), new ItemStack(ConfigItems.itemGolemCore, 1, 8), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 15), /*new ItemStack(Items.redstone),*/ new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), new ItemStack(ConfigItems.itemZombieBrain), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6)});

        recipeRemoteJar = ThaumcraftApi.addArcaneCraftingRecipe(SimpleResearchItem.getFullName("REMOTEJAR"), new ItemStack(RegisteredBlocks.blockRemoteJar), new AspectList().add(Aspect.WATER, 10).add(Aspect.EARTH, 10).add(Aspect.ORDER, 10), "GJG", "GMG", 'G', new ItemStack(ConfigBlocks.blockMagicalLog), 'J', new ItemStack(ConfigBlocks.blockJar), 'M', new ItemStack(ConfigBlocks.blockMirror, 1, 6));

        recipeNodeManipulator = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".NODE_MANIPULATOR",
                new ItemStack(RegisteredBlocks.blockNodeManipulator, 1, 5), 10,
                new AspectList().add(Aspect.AURA, 42).add(Aspect.ELDRITCH, 22).add(Aspect.MAGIC, 38).add(Aspect.MECHANISM, 28).add(Aspect.DARKNESS, 14),
                new ItemStack(ConfigBlocks.blockStoneDevice, 1, 5),
                new ItemStack[] {new ItemStack(ConfigBlocks.blockStoneDevice, 1, 9), new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 17), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), new ItemStack(ConfigItems.itemResource, 1, 1), new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 3), new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 17), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), new ItemStack(ConfigItems.itemResource, 1, 1), new ItemStack(ConfigItems.itemResource, 1, 15)});

        recipeRandomizationFocus = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".NODE_MANIPULATOR",
                new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 0), 7,
                new AspectList().add(Aspect.ELDRITCH, 18).add(Aspect.MAGIC, 18).add(Aspect.MECHANISM, 20).add(Aspect.DARKNESS, 28).add(Aspect.ORDER, 30),
                new ItemStack(ConfigBlocks.blockStoneDevice, 1, 8),
                new ItemStack[] {new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 16)});

        multiblockNodeManipulator = Arrays.asList(costsNodeManipulatorMultiblock, 3, 3, 3,
                Arrays.asList(
                null, null, null, null, new ItemStack(RegisteredBlocks.blockNode, 1, 5), null, null, null, null,
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), null, new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 0), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), null, new ItemStack(RegisteredBlocks.blockNodeManipulator, 1, 5), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15)));
    }

    private static IArcaneRecipe[] stickyJarRecipes = null;

    public static IArcaneRecipe[] getVisualStickyJarRecipes() {
        if(stickyJarRecipes == null) {
            List<ItemStack> stacks = RegisteredItems.getStickyJarStacks();
            stickyJarRecipes = new IArcaneRecipe[stacks.size()];

            ItemStack slime = new ItemStack(Items.slime_ball, 1, 0);
            for(int i = 0; i < stacks.size(); i++) {
                ItemStack stack = stacks.get(i);

                ItemStack output = stack.copy();
                NBTHelper.getData(output).setBoolean("isStickyJar", true);

                stickyJarRecipes[i] = new ShapedArcaneRecipe(SimpleResearchItem.getFullName("STICKYJAR"), output,
                        new AspectList().add(Aspect.WATER, 10).add(Aspect.EARTH, 10),
                        "   ",
                        " A ",
                        " B ",
                        'A', stack, 'B', slime);
            }
        }

        return stickyJarRecipes;
    }
}
