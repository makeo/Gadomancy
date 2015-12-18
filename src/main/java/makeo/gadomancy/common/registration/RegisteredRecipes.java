package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.registry.GameRegistry;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.crafting.InfusionUpgradeRecipe;
import makeo.gadomancy.common.crafting.RecipeStickyJar;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.items.ItemAuraCore;
import makeo.gadomancy.common.items.baubles.ItemFamiliar;
import makeo.gadomancy.common.research.SimpleResearchItem;
import makeo.gadomancy.common.utils.NBTHelper;
import makeo.gadomancy.common.utils.RandomizedAspectList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemWispEssence;

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
    public static InfusionRecipe[] recipesFamilar;
    public static InfusionRecipe recipeGolemCoreBodyguard;
    public static InfusionRecipe recipePortalFocus;

    //ID's: 0-2=Strength upgrades, 3=range, 4=cdReduction
    public static InfusionRecipe[][] recipesFamiliarAugmentation;

    public static IArcaneRecipe recipeStickyJar;
    public static IArcaneRecipe recipeArcaneDropper;
    public static IArcaneRecipe recipeRemoteJar;
    public static IArcaneRecipe recipeAncientPedestal;
    public static IArcaneRecipe recipeBlockProtector;

    public static CrucibleRecipe recipeAncientStonePedestal = null;
    public static IArcaneRecipe recipeAncientStone = null;

    public static CrucibleRecipe[] recipesWashAuraCore;
    public static IRecipe[] recipesUndoAuraCore;
    public static InfusionRecipe recipeAuraCore;
    public static IArcaneRecipe recipeAuraPylon;
    public static IArcaneRecipe recipeAuraPylonPeak;
    public static IArcaneRecipe recipeArcanePackager;

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

        recipesFamilar = createFamilarRecipes();
        recipesFamiliarAugmentation = createFamiliarAugmentationRecipes();

        recipeBlockProtector =  ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".BLOCK_PROTECTOR", new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 2),
                new AspectList().add(Aspect.ORDER, 120).add(Aspect.EARTH, 120),
                "WNW", "PJP", "GRG", 'W', new ItemStack(Items.potionitem, 1, 8232), 'V', new ItemStack(Items.potionitem, 1, 8264), 'N', new ItemStack(ConfigItems.itemResource, 1, 1), 'P', new ItemStack(ConfigBlocks.blockTube), 'J', new ItemStack(ConfigBlocks.blockJar), 'G', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'R', new ItemStack(Items.redstone));

        if(ModConfig.ancientStoneRecipes) {
            recipeAncientStone = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".ANCIENT_STONES", new ItemStack(ConfigBlocks.blockCosmeticSolid, 4, 11), new AspectList().add(Aspect.ENTROPY, 16).add(Aspect.EARTH, 8),
                    "SSS", "SES", "SSS", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), 'E', new ItemStack(ConfigItems.itemEldritchObject));
            recipeAncientStonePedestal = ThaumcraftApi.addCrucibleRecipe(Gadomancy.MODID.toUpperCase() + ".ANCIENT_STONES", new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7), new AspectList().add(Aspect.ELDRITCH, 8).add(Aspect.EXCHANGE, 8).add(Aspect.ENTROPY, 12));
        }

        ItemAuraCore.AuraCoreType[] auraCoreTypes = ItemAuraCore.AuraCoreType.values();
        recipesWashAuraCore = new CrucibleRecipe[auraCoreTypes.length - 1];
        List<IRecipe> recipesUndoAuraCore = new ArrayList<IRecipe>();

        for(int i = 1; i < auraCoreTypes.length; i++) {
            recipesWashAuraCore[i-1] = ThaumcraftApi.addCrucibleRecipe(Gadomancy.MODID.toUpperCase() + ".CLEAN_AURA_CORE", new ItemStack(RegisteredItems.itemAuraCore), new ItemStack(RegisteredItems.itemAuraCore, 1, i), new AspectList().add(Aspect.MAGIC, 12).add(Aspect.WATER, 18).add(Aspect.HEAL, 24)); //damn those heal potions

            if(auraCoreTypes[i].isUnused()) {
                GameRegistry.addShapelessRecipe(new ItemStack(RegisteredItems.itemAuraCore), new ItemStack(RegisteredItems.itemAuraCore, 1, i));

                List list = CraftingManager.getInstance().getRecipeList();
                recipesUndoAuraCore.add((IRecipe)list.get(list.size() - 1));
            }
        }

        RegisteredRecipes.recipesUndoAuraCore = recipesUndoAuraCore.toArray(new IRecipe[recipesUndoAuraCore.size()]);

        ItemStack blankCore = new ItemStack(RegisteredItems.itemAuraCore);
        RegisteredItems.itemAuraCore.setCoreType(blankCore, ItemAuraCore.AuraCoreType.BLANK);
        recipeAuraCore = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".AURA_CORE", blankCore, 10,
                new RandomizedAspectList().setHalfCap(true).addAspectRandomBase(Aspect.AURA, 21).addAspectRandomBase(Aspect.MAGIC, 27).addAspectRandomBase(Aspect.ELDRITCH, 18).addAspectRandomBase(Aspect.VOID, 35),
                new ItemStack(ConfigItems.itemResource, 1, 15),
                new ItemStack[] { new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 2),
                        new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 2),
                        new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 2),
                        new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 2)});

        recipeAuraPylon = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".AURA_PYLON", new ItemStack(RegisteredBlocks.blockAuraPylon, 1, 0),
                new AspectList().add(Aspect.ORDER, 80).add(Aspect.WATER, 50).add(Aspect.FIRE, 50).add(Aspect.AIR, 50),
                "TST", "S S", "TJT", 'T', new ItemStack(ConfigBlocks.blockTube, 1, 6), 'S', new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1), 'J', new ItemStack(ConfigBlocks.blockJar, 1, 0));

        ItemStack ordoCore = new ItemStack(RegisteredItems.itemAuraCore);
        RegisteredItems.itemAuraCore.setCoreType(ordoCore, ItemAuraCore.AuraCoreType.ORDER);
        recipeAuraPylonPeak = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".AURA_PYLON", new ItemStack(RegisteredBlocks.blockAuraPylon, 1, 1),
                new AspectList().add(Aspect.ORDER, 120).add(Aspect.FIRE, 90).add(Aspect.AIR, 70),
                " N ", "ACA", " P ", 'A', new ItemStack(ConfigItems.itemResource, 1, 0), 'N', new ItemStack(ConfigItems.itemResource, 1, 1), 'C', ordoCore, 'P', new ItemStack(RegisteredBlocks.blockAuraPylon, 1, 0));

        ItemStack aerCore = new ItemStack(RegisteredItems.itemAuraCore);
        RegisteredItems.itemAuraCore.setCoreType(aerCore, ItemAuraCore.AuraCoreType.AIR);
        recipeArcanePackager = ThaumcraftApi.addArcaneCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".ARCANE_PACKAGER", new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 4),
                new AspectList().add(Aspect.AIR, 120).add(Aspect.ORDER, 80).add(Aspect.ENTROPY, 80),
                "PSP", "GCG", "JTJ", 'P', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'S', new ItemStack(Blocks.piston), 'G', (Config.wardedStone ? new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2) : new ItemStack(Blocks.glass) ),
                'C', aerCore, 'J', new ItemStack(ConfigBlocks.blockJar), 'T', new ItemStack(ConfigBlocks.blockTable, 1, 15));

        auraCoreRecipes = createAuraCoreRecipes();
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

            recipes[i] = Arrays.asList(cost, 3, 1, 3,
                    Arrays.asList(
                            null, null, null, null, new ItemStack(ConfigBlocks.blockCrystal, 1, i),
                            new ItemStack(RegisteredItems.itemAuraCore), null, null, null
                    ));
        }
        return recipes;
    }

    private static InfusionRecipe[][] createFamiliarAugmentationRecipes() {
        InfusionRecipe[][] recipes = new InfusionRecipe[5][];
        ItemWispEssence wispEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        ItemFamiliar familiar = RegisteredItems.itemFamiliar;

        List<Aspect> aspects = new ArrayList<Aspect>(Aspect.aspects.values());
        InfusionRecipe[] upgradeArrayStr1 = new InfusionRecipe[aspects.size()];
        InfusionRecipe[] upgradeArrayStr2 = new InfusionRecipe[aspects.size()];
        InfusionRecipe[] upgradeArrayStr3 = new InfusionRecipe[aspects.size()];
        InfusionRecipe[] upgradeArrayRange1 = new InfusionRecipe[aspects.size()];
        InfusionRecipe[] upgradeArrayCd1 = new InfusionRecipe[aspects.size()];

        for (int i = 0; i < aspects.size(); i++) {
            Aspect aspect = aspects.get(i);
            ItemStack wispyEssence = new ItemStack(wispEssence, 1, 0);
            wispEssence.setAspects(wispyEssence, new AspectList().add(aspect, 2));

            ItemStack familiarIn = new ItemStack(RegisteredItems.itemFamiliar);
            familiar.setAspect(familiarIn, aspect);

            FamiliarAugmentInfusion infusion = new FamiliarAugmentInfusion(Gadomancy.MODID.toUpperCase() + ".FAM_ATTACK_1", 4, new AspectList().add(Aspect.AURA, 37).add(Aspect.MAGIC, 53).add(aspect, 35), familiarIn, ItemFamiliar.FamiliarUpgrade.ATTACK_1,
                    new ItemStack[]{ wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 6), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 4), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 6), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 4)});
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            upgradeArrayStr1[i] = infusion;

            familiarIn = new ItemStack(RegisteredItems.itemFamiliar);
            familiar.setAspect(familiarIn, aspect);
            familiar.addUpgrade(familiarIn, ItemFamiliar.FamiliarUpgrade.ATTACK_1);

            infusion = new FamiliarAugmentInfusion(Gadomancy.MODID.toUpperCase() + ".FAM_ATTACK_2", 6, new AspectList().add(Aspect.AURA, 43).add(Aspect.MAGIC, 71).add(Aspect.ELDRITCH, 37).add(aspect, 59), familiarIn, ItemFamiliar.FamiliarUpgrade.ATTACK_2,
                    new ItemStack[] { wispyEssence, new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 15), wispyEssence, new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 17), wispyEssence, new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 15), wispyEssence, new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 17)});
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            upgradeArrayStr2[i] = infusion;


            familiarIn = new ItemStack(RegisteredItems.itemFamiliar);
            familiar.setAspect(familiarIn, aspect);
            familiar.addUpgrade(familiarIn, ItemFamiliar.FamiliarUpgrade.ATTACK_1);
            familiar.addUpgrade(familiarIn, ItemFamiliar.FamiliarUpgrade.ATTACK_2);

            infusion = new FamiliarAugmentInfusion(Gadomancy.MODID.toUpperCase() + ".FAM_ATTACK_3", 10, new AspectList().add(Aspect.AURA, 77).add(Aspect.MAGIC, 93).add(Aspect.WEAPON, 49).add(aspect, 104), familiarIn, ItemFamiliar.FamiliarUpgrade.ATTACK_3,
                    new ItemStack[] { new ItemStack(ConfigItems.itemEldritchObject, 1, 3), wispyEssence, new ItemStack(ConfigBlocks.blockCustomPlant, 1, 4), wispyEssence, new ItemStack(Items.nether_star, 1, 0), wispyEssence, new ItemStack(ConfigBlocks.blockCustomPlant, 1, 4), wispyEssence });
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            upgradeArrayStr3[i] = infusion;

            familiarIn = new ItemStack(RegisteredItems.itemFamiliar);
            familiar.setAspect(familiarIn, aspect);

            infusion = new FamiliarAugmentInfusion(Gadomancy.MODID.toUpperCase() + ".FAM_RANGE_1", 8, new AspectList().add(Aspect.AURA, 44).add(Aspect.MAGIC, 51).add(aspect, 37), familiarIn, ItemFamiliar.FamiliarUpgrade.RANGE_1,
                    new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 0), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 17), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 0), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 17), wispyEssence });
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            upgradeArrayRange1[i] = infusion;


            familiarIn = new ItemStack(RegisteredItems.itemFamiliar);
            familiar.setAspect(familiarIn, aspect);

            infusion = new FamiliarAugmentInfusion(Gadomancy.MODID.toUpperCase() + ".FAM_COOLDOWN_1", 10, new AspectList().add(Aspect.AURA, 79).add(Aspect.MAGIC, 101).add(Aspect.EXCHANGE, 54).add(aspect, 81), familiarIn, ItemFamiliar.FamiliarUpgrade.COOLDOWN_1,
                    new ItemStack[] { new ItemStack(ConfigItems.itemEldritchObject, 1, 3), new ItemStack(ConfigItems.itemFocusPrimal, 1, 0), wispyEssence, new ItemStack(ConfigItems.itemBathSalts, 1, 0), new ItemStack(ConfigItems.itemFocusPrimal, 1, 0), wispyEssence, new ItemStack(ConfigItems.itemBathSalts, 1, 0) });
            ThaumcraftApi.getCraftingRecipes().add(infusion);
            upgradeArrayCd1[i] = infusion;
        }

        recipes[0] = upgradeArrayStr1;
        recipes[1] = upgradeArrayStr2;
        recipes[2] = upgradeArrayStr3;
        recipes[3] = upgradeArrayRange1;
        recipes[4] = upgradeArrayCd1;

        return recipes;
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

    public static InfusionRecipe[] createFamilarRecipes() {
        List<Aspect> aspects = new ArrayList<Aspect>(Aspect.aspects.values());
        InfusionRecipe[] recipes = new InfusionRecipe[aspects.size()];
        ItemFamiliar familiar = (ItemFamiliar) RegisteredItems.itemFamiliar;

        ItemWispEssence itemEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        for (int i = 0; i < aspects.size(); i++) {
            Aspect aspect = aspects.get(i);

            ItemStack wispyEssence = new ItemStack(itemEssence, 1, 0);
            itemEssence.setAspects(wispyEssence, new AspectList().add(aspect, 2));

            ItemStack result = new ItemStack(RegisteredItems.itemFamiliar);
            familiar.setAspect(result, aspect);

            recipes[i] = ThaumcraftApi.addInfusionCraftingRecipe(Gadomancy.MODID.toUpperCase() + ".FAMILIAR", result, 4, new AspectList().add(aspect, 46).add(Aspect.AURA, 34).add(Aspect.MAGIC, 51), new ItemStack(ConfigItems.itemAmuletRunic, 1, 0), new ItemStack[] { wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigBlocks.blockCrystal, 1, 6) , wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 1), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigBlocks.blockCrystal, 1, 6), wispyEssence, new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(ConfigItems.itemResource, 1, 1) });
        }
        return recipes;
    }
}
