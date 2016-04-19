package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.registry.GameRegistry;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.crafting.EtherealFamiliarUpgradeRecipe;
import makeo.gadomancy.common.crafting.FamiliarUndoRecipe;
import makeo.gadomancy.common.crafting.InfusionDisguiseArmor;
import makeo.gadomancy.common.crafting.InfusionUpgradeRecipe;
import makeo.gadomancy.common.crafting.RecipeStickyJar;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.familiar.FamiliarAugment;
import makeo.gadomancy.common.items.ItemAuraCore;
import makeo.gadomancy.common.items.baubles.ItemEtherealFamiliar;
import makeo.gadomancy.common.research.SimpleResearchItem;
import makeo.gadomancy.common.utils.RandomizedAspectList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.*;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemWispEssence;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.ArrayList;
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
    public static AspectList costsEldritchPortalCreatorMultiblock = new AspectList().add(Aspect.FIRE, 150).add(Aspect.WATER, 150).add(Aspect.EARTH, 150).add(Aspect.AIR, 150).add(Aspect.ORDER, 150).add(Aspect.ENTROPY, 150);
    public static AspectList costsAuraPylonMultiblock = new AspectList().add(Aspect.FIRE, 150).add(Aspect.WATER, 150).add(Aspect.EARTH, 150).add(Aspect.AIR, 150).add(Aspect.ORDER, 150).add(Aspect.ENTROPY, 150);
    //public static AspectList costsAuraCoreStart = new AspectList().add(Aspect.FIRE, 70).add(Aspect.WATER, 70).add(Aspect.EARTH, 70).add(Aspect.AIR, 70).add(Aspect.ORDER, 70).add(Aspect.ENTROPY, 70);

    public static List multiblockNodeManipulator;
    public static List multiblockEldritchPortalCreator;
    public static List multiblockAuraPylon;
    public static List[] auraCoreRecipes;

    public static InfusionRecipe recipeGolemSilverwood;
    public static InfusionRecipe[] recipesGolemRunicShield;
    public static InfusionRecipe recipeGolemCoreBreak;
    public static InfusionRecipe recipeInfusionClaw;
    public static InfusionRecipe recipeNodeManipulator;
    public static InfusionRecipe recipeRandomizationFocus;
    public static InfusionRecipe[] recipesFamiliar;
    public static InfusionRecipe recipeGolemCoreBodyguard;
    public static InfusionRecipe recipePortalFocus;

    //For indexing, look below...
    public static InfusionRecipe[][] recipesFamiliarAugmentation;

    public static IArcaneRecipe recipeStickyJar;
    public static IArcaneRecipe recipeArcaneDropper;
    public static IArcaneRecipe recipeRemoteJar;
    public static IArcaneRecipe recipeAncientPedestal;
    public static IArcaneRecipe[] recipeBlockProtector;

    public static CrucibleRecipe recipeAncientStonePedestal = null;
    public static IArcaneRecipe recipeAncientStone = null;

    public static CrucibleRecipe[] recipesWashAuraCore;
    public static IRecipe[] recipesUndoAuraCore;
    public static InfusionRecipe recipeAuraCore;
    public static IArcaneRecipe recipeAuraPylon;
    public static IArcaneRecipe recipeAuraPylonPeak;
    public static IArcaneRecipe recipeArcanePackager;

    public static InfusionEnchantmentRecipe recipeRevealer;

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

        recipeRemoteJar = ThaumcraftApi.addArcaneCraftingRecipe(SimpleResearchItem.getFullName("REMOTEJAR"), new ItemStack(RegisteredBlocks.blockRemoteJar), new AspectList().add(Aspect.WATER, 10).add(Aspect.EARTH, 10).add(Aspect.ORDER, 10), "GJG", "GMG", 'G', new ItemStack(ConfigBlocks.blockMagicalLog), 'J', new ItemStack(ConfigBlocks.blockJar), 'M', (Config.allowMirrors ? new ItemStack(ConfigBlocks.blockMirror, 1, 6) : new ItemStack(ConfigItems.itemResource, 1, 10) ));

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

        recipePortalFocus = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".E_PORTAL_CREATOR",
                new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 3), 7,
                new AspectList().add(Aspect.ELDRITCH, 22).add(Aspect.VOID, 38).add(Aspect.MECHANISM, 30).add(Aspect.DARKNESS, 28).add(Aspect.EXCHANGE, 38),
                new ItemStack(ConfigBlocks.blockStoneDevice, 1, 8),
                new ItemStack[] {new ItemStack(ConfigItems.itemEldritchObject, 1, 0), new ItemStack(ConfigBlocks.blockCrystal, 1, 5), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockCrystal, 1, 5), new ItemStack(ConfigItems.itemEldritchObject, 1, 0), new ItemStack(ConfigBlocks.blockCrystal, 1, 5), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockCrystal, 1, 5)} );

        multiblockNodeManipulator = Arrays.asList(costsNodeManipulatorMultiblock, 3, 3, 3,
                Arrays.asList(
                null, null, null, null, new ItemStack(RegisteredBlocks.blockNode, 1, 5), null, null, null, null,
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), null, new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 0), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), null, new ItemStack(RegisteredBlocks.blockNodeManipulator, 1, 5), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15)));

        //                                                                                    x  y  z
        multiblockEldritchPortalCreator = Arrays.asList(costsEldritchPortalCreatorMultiblock, 7, 3, 7,
                Arrays.asList(
                        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new ItemStack(RegisteredBlocks.blockNode, 1, 5), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), null, null,   null, null, null, new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 3), null, null, null,   null, null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                        null, null, null, new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 1), null, null, null, null, null, null, null, null, null, null, null, null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), null, null, new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 1), null, null, new ItemStack(RegisteredBlocks.blockNodeManipulator, 1, 5), null, null, new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 1), null, null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), null, null, null, null, null, null, null, null, null, null, null, null, new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 1), null, null, null
                ));

        multiblockAuraPylon = Arrays.asList(costsAuraPylonMultiblock, 1, 4, 1,
                Arrays.asList(
                        new ItemStack(RegisteredBlocks.blockAuraPylon, 1, 1),
                        new ItemStack(RegisteredBlocks.blockAuraPylon),
                        new ItemStack(RegisteredBlocks.blockAuraPylon),
                        new ItemStack(RegisteredBlocks.blockAuraPylon)
                ));

        recipeGolemCoreBodyguard = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".GOLEMCOREBODYGUARD", new ItemStack(RegisteredItems.itemGolemCoreBreak, 1, 1), 3, new AspectList().add(Aspect.TOOL, 28).add(Aspect.MECHANISM, 20).add(Aspect.WEAPON, 10).add(Aspect.ARMOR, 16),
                new ItemStack(ConfigItems.itemGolemCore, 1, 4), new ItemStack[] { new ItemStack(ConfigItems.itemBootsTraveller, 1, 0), new ItemStack(Items.ender_pearl, 1, 0), new ItemStack(ConfigItems.itemSwordElemental, 1, 0), new ItemStack(Items.ender_pearl, 1, 0) } );

        recipeAncientPedestal = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".E_PORTAL_CREATOR", new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 1),
                new AspectList().add(Aspect.ENTROPY, 25).add(Aspect.ORDER, 25),
                "SPS", " S ", "SPS", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), 'P', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15));

        recipesFamiliar = createFamiliarRecipes();
        recipesFamiliarAugmentation = createEtherealFamiliarUpgradeRecipes();

        recipeBlockProtector = new IArcaneRecipe[4];
        recipeBlockProtector[0] =  ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".BLOCK_PROTECTOR", new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 2),
                new AspectList().add(Aspect.ORDER, 120).add(Aspect.EARTH, 120),
                "WNW", "PJP", "GRG", 'W', new ItemStack(Items.potionitem, 1, 8232), 'N', new ItemStack(ConfigItems.itemResource, 1, 1), 'P', new ItemStack(ConfigBlocks.blockTube), 'J', new ItemStack(ConfigBlocks.blockJar), 'G', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'R', new ItemStack(Items.redstone));
        recipeBlockProtector[1] =  ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".BLOCK_PROTECTOR", new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 2),
                new AspectList().add(Aspect.ORDER, 120).add(Aspect.EARTH, 120),
                "WNW", "PJP", "GRG", 'W', new ItemStack(Items.potionitem, 1, 8200), 'N', new ItemStack(ConfigItems.itemResource, 1, 1), 'P', new ItemStack(ConfigBlocks.blockTube), 'J', new ItemStack(ConfigBlocks.blockJar), 'G', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'R', new ItemStack(Items.redstone));

        recipeBlockProtector[2] =  ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".BLOCK_PROTECTOR", new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 2),
                new AspectList().add(Aspect.ORDER, 120).add(Aspect.EARTH, 120),
                "WNV", "PJP", "GRG", 'W', new ItemStack(Items.potionitem, 1, 8232), 'V', new ItemStack(Items.potionitem, 1, 8200), 'N', new ItemStack(ConfigItems.itemResource, 1, 1), 'P', new ItemStack(ConfigBlocks.blockTube), 'J', new ItemStack(ConfigBlocks.blockJar), 'G', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'R', new ItemStack(Items.redstone));
        recipeBlockProtector[3] =  ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".BLOCK_PROTECTOR", new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 2),
                new AspectList().add(Aspect.ORDER, 120).add(Aspect.EARTH, 120),
                "WNV", "PJP", "GRG", 'W', new ItemStack(Items.potionitem, 1, 8200), 'V', new ItemStack(Items.potionitem, 1, 8232), 'N', new ItemStack(ConfigItems.itemResource, 1, 1), 'P', new ItemStack(ConfigBlocks.blockTube), 'J', new ItemStack(ConfigBlocks.blockJar), 'G', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'R', new ItemStack(Items.redstone));

        if(ModConfig.ancientStoneRecipes) {
            recipeAncientStone = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".ANCIENT_STONES", new ItemStack(ConfigBlocks.blockCosmeticSolid, 4, 11), new AspectList().add(Aspect.ENTROPY, 16).add(Aspect.EARTH, 8),
                    "SSS", "SES", "SSS", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), 'E', new ItemStack(ConfigItems.itemEldritchObject));
            recipeAncientStonePedestal = ThaumcraftApi.addCrucibleRecipe(Gadomancy.MODID.toUpperCase() + ".ANCIENT_STONES", new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7), new AspectList().add(Aspect.ELDRITCH, 8).add(Aspect.EXCHANGE, 8).add(Aspect.ENTROPY, 12));
        }

        ItemAuraCore.AuraCoreType[] auraCoreTypes = ItemAuraCore.AuraCoreType.values();
        recipesWashAuraCore = new CrucibleRecipe[auraCoreTypes.length - 1];
        List<IRecipe> recipesUndoAuraCore = new ArrayList<IRecipe>();

        for(int i = 1; i < auraCoreTypes.length; i++) {
            recipesWashAuraCore[i-1] = ThaumcraftApi.addCrucibleRecipe(Gadomancy.MODID.toUpperCase() + ".CLEAN_AURA_CORE", new ItemStack(RegisteredItems.itemAuraCore), new ItemStack(RegisteredItems.itemAuraCore, 1, i), new AspectList().add(Aspect.MAGIC, 12).add(Aspect.WATER, 12).add(Aspect.HEAL, 18)); //damn those heal potions

            if(auraCoreTypes[i].isUnused()) {
                GameRegistry.addShapelessRecipe(new ItemStack(RegisteredItems.itemAuraCore), new ItemStack(RegisteredItems.itemAuraCore, 1, i));

                List list = CraftingManager.getInstance().getRecipeList();
                recipesUndoAuraCore.add((IRecipe)list.get(list.size() - 1));
            }
        }

        RegisteredRecipes.recipesUndoAuraCore = recipesUndoAuraCore.toArray(new IRecipe[recipesUndoAuraCore.size()]);

        ItemStack blankCore = new ItemStack(RegisteredItems.itemAuraCore);
        RegisteredItems.itemAuraCore.setCoreType(blankCore, ItemAuraCore.AuraCoreType.BLANK);
        recipeAuraCore = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".AURA_CORE", blankCore, 6,
                new RandomizedAspectList().setHalfCap(true).addAspectRandomBase(Aspect.AURA, 9).addAspectRandomBase(Aspect.MAGIC, 19).addAspectRandomBase(Aspect.ELDRITCH, 17).addAspectRandomBase(Aspect.VOID, 34),
                new ItemStack(ConfigItems.itemResource, 1, 15),
                new ItemStack[] { new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 2),
                        new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 2),
                        new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 2),
                        new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 2)});

        recipeAuraPylon = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".AURA_PYLON", new ItemStack(RegisteredBlocks.blockAuraPylon, 1, 0),
                new AspectList().add(Aspect.ORDER, 80).add(Aspect.WATER, 50).add(Aspect.FIRE, 50).add(Aspect.AIR, 50),
                "TST", "S S", "TJT", 'T', new ItemStack(ConfigBlocks.blockTube, 1, 6), 'S', new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1), 'J', new ItemStack(ConfigBlocks.blockJar, 1, 0));

        ItemStack ignisCore = new ItemStack(RegisteredItems.itemAuraCore);
        RegisteredItems.itemAuraCore.setCoreType(ignisCore, ItemAuraCore.AuraCoreType.FIRE);
        recipeAuraPylonPeak = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".AURA_PYLON", new ItemStack(RegisteredBlocks.blockAuraPylon, 1, 1),
                new AspectList().add(Aspect.ORDER, 90).add(Aspect.FIRE, 120).add(Aspect.AIR, 70),
                " N ", "ACA", " P ", 'A', new ItemStack(ConfigItems.itemResource, 1, 0), 'N', new ItemStack(ConfigItems.itemResource, 1, 1), 'C', ignisCore, 'P', new ItemStack(RegisteredBlocks.blockAuraPylon, 1, 0));

        ItemStack aerCore = new ItemStack(RegisteredItems.itemAuraCore);
        RegisteredItems.itemAuraCore.setCoreType(aerCore, ItemAuraCore.AuraCoreType.AIR);
        recipeArcanePackager = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".ARCANE_PACKAGER", new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 4),
                new AspectList().add(Aspect.AIR, 100).add(Aspect.ORDER, 60).add(Aspect.ENTROPY, 50),
                "PSP", "GCG", "JTJ", 'P', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'S', new ItemStack(Blocks.piston), 'G', (Config.wardedStone ? new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2) : new ItemStack(Blocks.glass) ),
                'C', aerCore, 'J', new ItemStack(ConfigBlocks.blockJar), 'T', new ItemStack(ConfigBlocks.blockTable, 1, 15));

        auraCoreRecipes = createAuraCoreRecipes();

        ThaumcraftApi.getCraftingRecipes().add(new InfusionDisguiseArmor());

        recipeRevealer = ThaumcraftApi.addInfusionEnchantmentRecipe("", RegisteredEnchantments.revealer, 4, new AspectList().add(Aspect.SENSES, 16).add(Aspect.MAGIC, 8), new ItemStack[]{new ItemStack(ConfigItems.itemGoggles), new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 14)});

        CraftingManager.getInstance().getRecipeList().add(new FamiliarUndoRecipe());
    }

    private static List[] createAuraCoreRecipes() {
        List[] recipes = new List[7];
        for(int i = 0; i < 7; i++) {
            AspectList cost = new AspectList();
            switch (i) {
                case 0: cost.add(Aspect.AIR, 84); break;
                case 1: cost.add(Aspect.FIRE, 84); break;
                case 2: cost.add(Aspect.WATER, 84); break;
                case 3: cost.add(Aspect.EARTH, 84); break;
                case 4: cost.add(Aspect.ORDER, 84); break;
                case 5: cost.add(Aspect.ENTROPY, 84); break;
                case 6: cost.add(Aspect.AIR, 14).add(Aspect.FIRE, 14).add(Aspect.WATER, 14)
                        .add(Aspect.EARTH, 14).add(Aspect.ORDER, 14).add(Aspect.ENTROPY, 14);
            }

            ItemWandCasting item = (ItemWandCasting) ConfigItems.itemWandCasting;
            ItemStack wand = new ItemStack(item);
            item.setRod(wand, ConfigItems.WAND_ROD_GREATWOOD);
            item.setCap(wand, ConfigItems.WAND_CAP_GOLD);

            recipes[i] = Arrays.asList(cost, 3, 2, 3,
                    Arrays.asList(
                            null, null, null, null, wand, null, null, null, null,
                            null, null, null, null, new ItemStack(ConfigBlocks.blockCrystal, 1, i),
                            new ItemStack(RegisteredItems.itemAuraCore), null, null, null
                    ));
        }
        return recipes;
    }

    /*
    Indexing:
    0 = Shock 0 -> 1; 3 = Fire 0 -> 1; 6 = Poison 0 -> 1
    1 = Shock 1 -> 2; 4 = Fire 1 -> 2; 7 = Poison 1 -> 2
    2 = Shock 2 -> 3; 5 = Fire 2 -> 3; 8 = Poison 2 -> 3

     9 = Enervation 0 -> 1; 12 = Damage 0 -> 1
    10 = Enervation 1 -> 2; 13 = Damage 1 -> 2
    11 = Enervation 2 -> 3; 14 = Damage 2 -> 3

    15 = Range 0 -> 1; 17 = Speed 0 -> 1
    16 = Range 1 -> 2; 18 = Speed 1 -> 2
     */
    private static InfusionRecipe[][] createEtherealFamiliarUpgradeRecipes() {
        List<Aspect> aspects = new ArrayList<Aspect>(Aspect.aspects.values());
        InfusionRecipe[][] recipes = new InfusionRecipe[19][aspects.size()]; //Ugh.. 19xAspectSize array... nice...
        ItemWispEssence wispEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        ItemEtherealFamiliar etherealFamiliar = RegisteredItems.itemEtherealFamiliar;

        for (int i = 0; i < recipes.length; i++) {
            recipes[i] = new InfusionRecipe[aspects.size()];
        }

        ItemStack famIn;
        EtherealFamiliarUpgradeRecipe infusion;
        String modid = Gadomancy.MODID.toUpperCase();
        for (int i = 0; i < aspects.size(); i++) {
            Aspect a = aspects.get(i);
            ItemStack wispyEssence = new ItemStack(wispEssence, 1, 0);
            wispEssence.setAspects(wispyEssence, new AspectList().add(a, 2));


            //Shock 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_SHOCK", 3,
                    new AspectList(),
                    famIn, FamiliarAugment.SHOCK, 0,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[0])[i] = infusion;

            //Shock 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.SHOCK, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_SHOCK", 5,
                    new AspectList(),
                    famIn, FamiliarAugment.SHOCK, 1,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[1])[i] = infusion;

            //Shock 2 -> 3
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.SHOCK, 2);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_SHOCK", 8,
                    new AspectList(),
                    famIn, FamiliarAugment.SHOCK, 2,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[2])[i] = infusion;


            //Fire 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_FIRE", 4,
                    new AspectList(),
                    famIn, FamiliarAugment.FIRE, 0,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[3])[i] = infusion;

            //Fire 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.FIRE, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_FIRE", 6,
                    new AspectList(),
                    famIn, FamiliarAugment.FIRE, 1,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[4])[i] = infusion;

            //Fire 2 -> 3
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.FIRE, 2);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_FIRE", 9,
                    new AspectList(),
                    famIn, FamiliarAugment.FIRE, 2,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[5])[i] = infusion;


            //Poison 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_POISON", 5,
                    new AspectList(),
                    famIn, FamiliarAugment.POISON, 0,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[6])[i] = infusion;

            //Poison 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.POISON, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_POISON", 7,
                    new AspectList(),
                    famIn, FamiliarAugment.POISON, 1,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[7])[i] = infusion;

            //Poison 2 -> 3
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.POISON, 2);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_POISON", 9,
                    new AspectList(),
                    famIn, FamiliarAugment.POISON, 2,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[8])[i] = infusion;


            //Enervation 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_WEAKNESS", 6,
                    new AspectList(),
                    famIn, FamiliarAugment.WEAKNESS, 0,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[9])[i] = infusion;

            //Enervation 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.WEAKNESS, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_WEAKNESS", 8,
                    new AspectList(),
                    famIn, FamiliarAugment.WEAKNESS, 1,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[10])[i] = infusion;

            //Enervation 2 -> 3
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.WEAKNESS, 2);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_WEAKNESS", 10,
                    new AspectList(),
                    famIn, FamiliarAugment.WEAKNESS, 2,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[11])[i] = infusion;


            //Damage 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_DAMAGE", 6,
                    new AspectList(),
                    famIn, FamiliarAugment.DAMAGE_INCREASE, 0,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[12])[i] = infusion;

            //Damage 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.DAMAGE_INCREASE, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_DAMAGE", 10,
                    new AspectList(),
                    famIn, FamiliarAugment.DAMAGE_INCREASE, 1,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[13])[i] = infusion;

            //Damage 2 -> 3
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.DAMAGE_INCREASE, 2);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_DAMAGE", 10,
                    new AspectList(),
                    famIn, FamiliarAugment.DAMAGE_INCREASE, 2,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[14])[i] = infusion;


            //Range 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_RANGE", 6,
                    new AspectList(),
                    famIn, FamiliarAugment.RANGE_INCREASE, 0,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[15])[i] = infusion;

            //Range 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.RANGE_INCREASE, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_RANGE", 9,
                    new AspectList(),
                    famIn, FamiliarAugment.RANGE_INCREASE, 1,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[16])[i] = infusion;


            //Speed 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_SPEED", 6,
                    new AspectList(),
                    famIn, FamiliarAugment.ATTACK_SPEED, 0,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[17])[i] = infusion;

            //Speed 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.ATTACK_SPEED, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + "FAMILIAR_SPEED", 9,
                    new AspectList(),
                    famIn, FamiliarAugment.ATTACK_SPEED, 1,
                    wispyEssence, wispyEssence, wispyEssence, wispyEssence);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[18])[i] = infusion;
        }
        return recipes;
    }

    public static InfusionRecipe[] createFamiliarRecipes() {
        List<Aspect> aspects = new ArrayList<Aspect>(Aspect.aspects.values());
        InfusionRecipe[] recipes = new InfusionRecipe[aspects.size()];

        ItemWispEssence itemEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        for (int i = 0; i < aspects.size(); i++) {
            Aspect aspect = aspects.get(i);

            ItemStack wispyEssence = new ItemStack(itemEssence, 1, 0);
            itemEssence.setAspects(wispyEssence, new AspectList().add(aspect, 2));

            ItemStack result = new ItemStack(RegisteredItems.itemFamiliar_old);
            RegisteredItems.itemFamiliar_old.setAspect(result, aspect);

            recipes[i] = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".ETHEREAL_FAMILIAR", result, 4, new AspectList().add(aspect, 46).add(Aspect.AURA, 34).add(Aspect.MAGIC, 51), new ItemStack(ConfigItems.itemAmuletRunic, 1, 0), new ItemStack[] { wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigBlocks.blockCrystal, 1, 6) , wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 1), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigBlocks.blockCrystal, 1, 6), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 1) });
        }
        return recipes;
    }
}
