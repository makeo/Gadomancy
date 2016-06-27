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
import makeo.gadomancy.common.items.ItemElement;
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
    public static AspectList costsCompressorMultiblock = new AspectList().add(Aspect.WATER, 140).add(Aspect.EARTH, 80).add(Aspect.AIR, 150).add(Aspect.ORDER, 20).add(Aspect.ENTROPY, 140);
    //public static AspectList costsAuraCoreStart = new AspectList().add(Aspect.FIRE, 70).add(Aspect.WATER, 70).add(Aspect.EARTH, 70).add(Aspect.AIR, 70).add(Aspect.ORDER, 70).add(Aspect.ENTROPY, 70);

    public static List multiblockNodeManipulator;
    public static List multiblockEldritchPortalCreator;
    public static List multiblockAuraPylon;
    public static List multiblockEssentiaCompressor;
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
    public static InfusionRecipe recipeElementVoid;
    public static InfusionRecipe recipeAiShutdown;

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
    public static IArcaneRecipe recipeKnowledgeBook;
    public static IArcaneRecipe recipeEssentiaCompressor;

    public static InfusionEnchantmentRecipe recipeRevealer;

    public static void init() {
        AdditionalGolemType typeSilverwood = RegisteredGolemStuff.typeSilverwood;
        recipeGolemSilverwood = ThaumcraftApi.addInfusionCraftingRecipe(SimpleResearchItem.getFullName("GOLEMSILVERWOOD"),
                /*new ItemStack(Items.bread, 1, 0),*/new ItemStack(typeSilverwood.getPlacerItem(), 1, typeSilverwood.getEnumEntry().ordinal()),
                7, new AspectList().add(Aspect.MOTION, 16).add(Aspect.MAGIC, 16).add(Aspect.MAN, 16).add(Aspect.SENSES, 16),
                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1), new ItemStack[]{ new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 9), new ItemStack(ConfigItems.itemResource, 1, 3), new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 9), new ItemStack(ConfigItems.itemResource, 1, 3)});

        recipesGolemRunicShield = InfusionUpgradeRecipe.createRecipes(SimpleResearchItem.getFullName("GOLEMRUNICSHIELD"), RegisteredGolemStuff.upgradeRunicShield, 5,
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

        recipeInfusionClaw = ThaumcraftApi.addInfusionCraftingRecipe(SimpleResearchItem.getFullName("INFUSIONCLAW"),
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
                "PSP", "GCG", "JTJ",
                'P', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'S', new ItemStack(Blocks.piston),
                'G', (Config.wardedStone ? new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2) : new ItemStack(Blocks.glass)), 'C', aerCore,
                'J', new ItemStack(ConfigBlocks.blockJar), 'T', new ItemStack(ConfigBlocks.blockTable, 1, 15));

        ItemStack ordoCore = new ItemStack(RegisteredItems.itemAuraCore);
        RegisteredItems.itemAuraCore.setCoreType(ordoCore, ItemAuraCore.AuraCoreType.ORDER);

        recipeKnowledgeBook = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".KNOWLEDGE_BOOK", new ItemStack(RegisteredBlocks.blockKnowledgeBook),
                new AspectList().add(Aspect.ORDER, 70).add(Aspect.ENTROPY, 20).add(Aspect.AIR, 20).add(Aspect.FIRE, 30),
                "STS", "BOB", "MGM",
                'S', new ItemStack(ConfigItems.itemInkwell), 'T', new ItemStack(ConfigItems.itemThaumonomicon),
                'B', new ItemStack(Items.book), 'O', ordoCore,
                'M', new ItemStack(ConfigBlocks.blockMetalDevice, 1, 12), 'G', new ItemStack(ConfigItems.itemGoggles));

        ItemStack voidMetalIngot = new ItemStack(ConfigItems.itemResource, 1, 16);
        ItemStack salisMundus = new ItemStack(ConfigItems.itemResource, 1, 14);
        ItemStack alumentum = new ItemStack(ConfigItems.itemResource);
        recipeElementVoid = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".ESSENTIA_COMPRESSOR", new ItemStack(RegisteredItems.itemElement, 1, ItemElement.EnumElementType.DARKNESS.ordinal()), 6,
                new RandomizedAspectList().setHalfCap(true).addAspectRandomBase(Aspect.VOID, 22).addAspectRandomBase(Aspect.DARKNESS, 18).addAspectRandomBase(Aspect.ELDRITCH, 20).addAspectRandomBase(Aspect.AURA, 10),
                new ItemStack(ConfigItems.itemResource, 1, 17),
                new ItemStack[] { alumentum, voidMetalIngot, voidMetalIngot, salisMundus, alumentum, voidMetalIngot, voidMetalIngot, salisMundus });

        ItemStack waterCore = new ItemStack(RegisteredItems.itemAuraCore);
        RegisteredItems.itemAuraCore.setCoreType(waterCore, ItemAuraCore.AuraCoreType.WATER);

        recipeEssentiaCompressor = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".ESSENTIA_COMPRESSOR", new ItemStack(RegisteredBlocks.blockEssentiaCompressor, 3),
                new AspectList().add(Aspect.WATER, 190).add(Aspect.ORDER, 150).add(Aspect.ENTROPY, 120).add(Aspect.FIRE, 100).add(Aspect.AIR, 160).add(Aspect.EARTH, 60),
                "LRL", "TET", "LCL",
                'L', new ItemStack(ConfigBlocks.blockMagicalLog),
                'R', new ItemStack(ConfigBlocks.blockCrystal, 1, 4),
                'T', new ItemStack(ConfigBlocks.blockTube, 1, 6),
                'E', new ItemStack(RegisteredItems.itemElement, 1, ItemElement.EnumElementType.DARKNESS.ordinal()),
                'C', waterCore);

        multiblockEssentiaCompressor = Arrays.asList(costsCompressorMultiblock, 1, 3, 1,
                Arrays.asList(
                        new ItemStack(RegisteredBlocks.blockEssentiaCompressor),
                        new ItemStack(RegisteredBlocks.blockEssentiaCompressor),
                        new ItemStack(RegisteredBlocks.blockEssentiaCompressor)
                ));

        ItemStack earthCore = new ItemStack(RegisteredItems.itemAuraCore);
        RegisteredItems.itemAuraCore.setCoreType(earthCore, ItemAuraCore.AuraCoreType.EARTH);

        /*recipeAiShutdown = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".AI_SHUTDOWN", new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 5), 9,
                new AspectList().add(Aspect.ENTROPY, 16).add(Aspect.BEAST, 23).add(Aspect.EARTH, 22).add(Aspect.AURA, 12).add(Aspect.MIND, 17),
                earthCore, new ItemStack[] {
                        new ItemStack(ConfigItems.itemZombieBrain), new ItemStack(ConfigBlocks.blockTube),
                        new ItemStack(ConfigItems.itemWandRod), new ItemStack(ConfigItems.itemShard, 1, 5),
                        new ItemStack(ConfigBlocks.blockJar), new ItemStack(ConfigBlocks.blockTube),
                        new ItemStack(ConfigItems.itemZombieBrain), new ItemStack(ConfigBlocks.blockTube),
                        new ItemStack(ConfigBlocks.blockJar), new ItemStack(ConfigItems.itemShard, 1, 5),
                        new ItemStack(ConfigItems.itemWandRod), new ItemStack(ConfigBlocks.blockTube)
                });*/


        auraCoreRecipes = createAuraCoreRecipes();

        ThaumcraftApi.getCraftingRecipes().add(new InfusionDisguiseArmor());

        recipeRevealer = ThaumcraftApi.addInfusionEnchantmentRecipe(Gadomancy.MODID.toUpperCase() + ".REVEALER", RegisteredEnchantments.revealer, 4, new AspectList().add(Aspect.SENSES, 16).add(Aspect.MAGIC, 8), new ItemStack[]{new ItemStack(ConfigItems.itemGoggles), new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 14)});

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

            ItemStack alumentum = new ItemStack(ConfigItems.itemResource, 1, 0);
            ItemStack amber = new ItemStack(ConfigItems.itemResource, 1, 6);

            //Shock 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_SHOCK", 3,
                    new AspectList().add(Aspect.MOTION, 14).add(a, 16).add(Aspect.AIR, 20),
                    famIn, FamiliarAugment.SHOCK, 0,
                    wispyEssence, alumentum, wispyEssence, amber,
                    wispyEssence, alumentum, wispyEssence, amber);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[0])[i] = infusion;

            ItemStack glowstone = new ItemStack(Items.glowstone_dust);
            ItemStack ghastTear = new ItemStack(Items.ghast_tear);

            //Shock 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.SHOCK, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_SHOCK", 5,
                    new AspectList().add(Aspect.MOTION, 22).add(a, 28).add(Aspect.AURA, 20).add(Aspect.WEAPON, 18).add(Aspect.AIR, 38),
                    famIn, FamiliarAugment.SHOCK, 1,
                    wispyEssence, alumentum, glowstone, wispyEssence, alumentum, ghastTear,
                    wispyEssence, alumentum, glowstone, wispyEssence, alumentum, ghastTear);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[1])[i] = infusion;

            ItemStack netherStar = new ItemStack(Items.nether_star);
            ItemStack airShard = new ItemStack(ConfigItems.itemShard, 1, 0);
            ItemStack quicksilver = new ItemStack(ConfigItems.itemResource, 1, 3);

            //Shock 2 -> 3
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.SHOCK, 2);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_SHOCK", 8,
                    new AspectList().add(Aspect.MOTION, 40).add(Aspect.AURA, 28).add(a, 38).add(Aspect.WEAPON, 20).add(Aspect.AIR, 36),
                    famIn, FamiliarAugment.SHOCK, 2,
                    wispyEssence, alumentum, airShard, alumentum, quicksilver, alumentum, netherStar, alumentum,
                    wispyEssence, alumentum, airShard, alumentum, quicksilver, alumentum, netherStar, alumentum);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[2])[i] = infusion;

            ItemStack blazePowder = new ItemStack(Items.blaze_powder);

            //Fire 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_FIRE", 4,
                    new AspectList().add(Aspect.FIRE, 12).add(a, 14).add(Aspect.WEAPON, 8),
                    famIn, FamiliarAugment.FIRE, 0,
                    wispyEssence, blazePowder, wispyEssence, blazePowder,
                    wispyEssence, blazePowder, wispyEssence, blazePowder);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[3])[i] = infusion;

            ItemStack redstone = new ItemStack(Items.redstone);
            ItemStack magmaCream = new ItemStack(Items.magma_cream);
            ItemStack coalBlock = new ItemStack(Blocks.coal_block);

            //Fire 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.FIRE, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_FIRE", 6,
                    new AspectList().add(Aspect.FIRE, 38).add(a, 24).add(Aspect.ENERGY, 32).add(Aspect.WEAPON, 18),
                    famIn, FamiliarAugment.FIRE, 1,
                    wispyEssence, redstone, coalBlock, wispyEssence, magmaCream, blazePowder,
                    wispyEssence, redstone, coalBlock, wispyEssence, magmaCream, blazePowder);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[4])[i] = infusion;

            ItemStack nitor = new ItemStack(ConfigItems.itemResource, 1, 1);
            ItemStack fireShard = new ItemStack(ConfigItems.itemShard, 1, 1);

            //Fire 2 -> 3
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.FIRE, 2);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_FIRE", 9,
                    new AspectList().add(Aspect.FIRE, 48).add(a, 32).add(Aspect.ENERGY, 42).add(Aspect.WEAPON, 32).add(Aspect.AURA, 30),
                    famIn, FamiliarAugment.FIRE, 2,
                    wispyEssence, blazePowder, fireShard, blazePowder, netherStar, blazePowder, nitor, blazePowder,
                    wispyEssence, blazePowder, fireShard, blazePowder, netherStar, blazePowder, nitor, blazePowder);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[5])[i] = infusion;

            ItemStack spiderEye = new ItemStack(Items.spider_eye);
            ItemStack rottenFlesh = new ItemStack(Items.rotten_flesh);

            //Poison 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_POISON", 5,
                    new AspectList().add(Aspect.WATER, 16).add(a, 14).add(Aspect.POISON, 8).add(Aspect.ELDRITCH, 4),
                    famIn, FamiliarAugment.POISON, 0,
                    wispyEssence, spiderEye, wispyEssence, rottenFlesh,
                    wispyEssence, spiderEye, wispyEssence, rottenFlesh);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[6])[i] = infusion;

            ItemStack fermentedSpiderEye = new ItemStack(Items.fermented_spider_eye);
            ItemStack enderPearl = new ItemStack(Items.ender_pearl);
            ItemStack sugarCane = new ItemStack(Items.reeds);
            ItemStack pumpkinSeeds = new ItemStack(Items.pumpkin_seeds);

            //Poison 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.POISON, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_POISON", 7,
                    new AspectList().add(a, 26).add(Aspect.POISON, 28).add(Aspect.ELDRITCH, 12).add(Aspect.WATER, 22).add(Aspect.AURA, 8),
                    famIn, FamiliarAugment.POISON, 1,
                    wispyEssence, fermentedSpiderEye, enderPearl, wispyEssence, sugarCane, pumpkinSeeds,
                    wispyEssence, fermentedSpiderEye, enderPearl, wispyEssence, sugarCane, pumpkinSeeds);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[7])[i] = infusion;

            ItemStack netherWarts = new ItemStack(Items.nether_wart);

            //Poison 2 -> 3
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.POISON, 2);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_POISON", 9,
                    new AspectList().add(a, 36).add(Aspect.POISON, 38).add(Aspect.ELDRITCH, 22).add(Aspect.WATER, 48).add(Aspect.AURA, 36),
                    famIn, FamiliarAugment.POISON, 2,
                    wispyEssence, netherWarts, rottenFlesh, netherWarts, spiderEye, netherWarts, netherStar, netherWarts,
                    wispyEssence, netherWarts, rottenFlesh, netherWarts, spiderEye, netherWarts, netherStar, netherWarts);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[8])[i] = infusion;

            ItemStack deadBush = new ItemStack(Blocks.deadbush);
            ItemStack string = new ItemStack(Items.string);

            //Enervation 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_WEAKNESS", 6,
                    new AspectList().add(a, 12).add(Aspect.DEATH, 18).add(Aspect.AURA, 6).add(Aspect.ELDRITCH, 18).add(Aspect.ENTROPY, 8),
                    famIn, FamiliarAugment.WEAKNESS, 0,
                    wispyEssence, deadBush, wispyEssence, string,
                    wispyEssence, deadBush, wispyEssence, string);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[9])[i] = infusion;

            ItemStack witherSkull = new ItemStack(Items.skull, 1, 1);

            //Enervation 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.WEAKNESS, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_WEAKNESS", 8,
                    new AspectList().add(a, 30).add(Aspect.DEATH, 28).add(Aspect.AURA, 26).add(Aspect.ELDRITCH, 28).add(Aspect.ENTROPY, 48).add(Aspect.TRAP, 14),
                    famIn, FamiliarAugment.WEAKNESS, 1,
                    wispyEssence, rottenFlesh, witherSkull, rottenFlesh,
                    wispyEssence, rottenFlesh, witherSkull, rottenFlesh);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[10])[i] = infusion;

            ItemStack entropyShard = new ItemStack(ConfigItems.itemShard, 1, 5);
            ItemStack enderEye = new ItemStack(Items.ender_eye);

            //Enervation 2 -> 3
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.WEAKNESS, 2);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_WEAKNESS", 10,
                    new AspectList().add(a, 46).add(Aspect.DEATH, 42).add(Aspect.AURA, 38).add(Aspect.ELDRITCH, 20).add(Aspect.ENTROPY, 56).add(Aspect.TRAP, 32),
                    famIn, FamiliarAugment.WEAKNESS, 2,
                    wispyEssence, enderEye, entropyShard, entropyShard, wispyEssence, enderEye, netherStar, entropyShard,
                    wispyEssence, enderEye, entropyShard, entropyShard, wispyEssence, enderEye, netherStar, entropyShard);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[11])[i] = infusion;

            ItemStack balancedShard = new ItemStack(ConfigItems.itemResource, 1, 6);
            ItemStack gunpowder = new ItemStack(Items.gunpowder);

            //Damage 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_DAMAGE", 6,
                    new AspectList().add(a, 14).add(Aspect.WEAPON, 10).add(Aspect.ENERGY, 12).add(Aspect.FIRE, 16),
                    famIn, FamiliarAugment.DAMAGE_INCREASE, 0,
                    wispyEssence, balancedShard, wispyEssence, gunpowder,
                    wispyEssence, balancedShard, wispyEssence, gunpowder);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[12])[i] = infusion;

            ItemStack primalCharm = new ItemStack(ConfigItems.itemResource, 1, 15);
            ItemStack voidSeed = new ItemStack(ConfigItems.itemResource, 1, 17);
            ItemStack amberBlock = new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 0);

            //Damage 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.DAMAGE_INCREASE, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_DAMAGE", 10,
                    new AspectList().add(a, 22).add(Aspect.WEAPON, 28).add(Aspect.ENERGY, 34).add(Aspect.FIRE, 30).add(Aspect.AURA, 14),
                    famIn, FamiliarAugment.DAMAGE_INCREASE, 1,
                    wispyEssence, balancedShard, primalCharm, wispyEssence, voidSeed, amberBlock,
                    wispyEssence, balancedShard, primalCharm, wispyEssence, voidSeed, amberBlock);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[13])[i] = infusion;

            ItemStack voidSword = new ItemStack(ConfigItems.itemSwordVoid);

            //Damage 2 -> 3
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.DAMAGE_INCREASE, 2);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_DAMAGE", 10,
                    new AspectList().add(a, 34).add(Aspect.WEAPON, 38).add(Aspect.ENERGY, 46).add(Aspect.FIRE, 58).add(Aspect.AURA, 38),
                    famIn, FamiliarAugment.DAMAGE_INCREASE, 2,
                    wispyEssence, blazePowder, voidSword, blazePowder, netherStar, blazePowder, alumentum, blazePowder,
                    wispyEssence, blazePowder, voidSword, blazePowder, netherStar, blazePowder, alumentum, blazePowder);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[14])[i] = infusion;

            ItemStack ordoShard = new ItemStack(ConfigItems.itemShard, 1, 4);

            //Range 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_RANGE", 6,
                    new AspectList().add(a, 24).add(Aspect.AURA, 30).add(Aspect.ELDRITCH, 22).add(Aspect.WEAPON, 12),
                    famIn, FamiliarAugment.RANGE_INCREASE, 0,
                    wispyEssence, ordoShard, wispyEssence, primalCharm,
                    wispyEssence, ordoShard, wispyEssence, primalCharm);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[15])[i] = infusion;

            //Range 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.RANGE_INCREASE, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_RANGE", 9,
                    new AspectList().add(a, 42).add(Aspect.AURA, 36).add(Aspect.ELDRITCH, 34).add(Aspect.WEAPON, 28).add(Aspect.AIR, 40),
                    famIn, FamiliarAugment.RANGE_INCREASE, 1,
                    wispyEssence, nitor, balancedShard, quicksilver, netherStar, alumentum, balancedShard, quicksilver,
                    wispyEssence, nitor, balancedShard, quicksilver, netherStar, alumentum, balancedShard, quicksilver);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[16])[i] = infusion;

            ItemStack elementalSword = new ItemStack(ConfigItems.itemSwordElemental);

            //Speed 0 -> 1
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_SPEED", 6,
                    new AspectList().add(a, 30).add(Aspect.AURA, 28).add(Aspect.MOTION, 38).add(Aspect.ELDRITCH, 20).add(Aspect.AIR, 36),
                    famIn, FamiliarAugment.ATTACK_SPEED, 0,
                    elementalSword, balancedShard, wispyEssence, balancedShard,
                    wispyEssence, balancedShard, wispyEssence, balancedShard);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[17])[i] = infusion;

            ItemStack primordialPearl = new ItemStack(ConfigItems.itemEldritchObject, 1, 3);
            ItemStack salisMundis = new ItemStack(ConfigItems.itemResource, 1, 14);

            //Speed 1 -> 2
            famIn = new ItemStack(etherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(famIn, a);
            ItemEtherealFamiliar.addAugmentUnsafe(famIn, FamiliarAugment.ATTACK_SPEED, 1);
            infusion = new EtherealFamiliarUpgradeRecipe(modid + ".FAMILIAR_SPEED", 9,
                    new AspectList().add(a, 46).add(Aspect.AURA, 38).add(Aspect.MOTION, 52).add(Aspect.ELDRITCH, 38).add(Aspect.AIR, 48).add(Aspect.WEAPON, 28),
                    famIn, FamiliarAugment.ATTACK_SPEED, 1,
                    wispyEssence, primalCharm, netherStar, salisMundis, wispyEssence, primalCharm, primordialPearl, salisMundis,
                    wispyEssence, primalCharm, netherStar, salisMundis, wispyEssence, primalCharm, primordialPearl, salisMundis);
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            (recipes[18])[i] = infusion;
        }
        return recipes;
    }

    public static InfusionRecipe[] createFamiliarRecipes() {
        List<Aspect> aspects = new ArrayList<Aspect>(Aspect.aspects.values());
        InfusionRecipe[] recipes = new InfusionRecipe[aspects.size()];

        ItemWispEssence itemEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        ItemStack result;
        for (int i = 0; i < aspects.size(); i++) {
            Aspect aspect = aspects.get(i);
            ItemStack wispyEssence = new ItemStack(itemEssence, 1, 0);
            itemEssence.setAspects(wispyEssence, new AspectList().add(aspect, 2));

            result = new ItemStack(RegisteredItems.itemEtherealFamiliar);
            ItemEtherealFamiliar.setFamiliarAspect(result, aspect);

            recipes[i] = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".ETHEREAL_FAMILIAR",
                    result, 4, new AspectList().add(aspect, 10).add(Aspect.AURA, 12).add(Aspect.MAGIC, 18), new ItemStack(ConfigItems.itemAmuletRunic, 1, 0),
                    new ItemStack[] { wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 1), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 1) });
        }
        return recipes;
    }
}
