package makeo.gadomancy.common.registration;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.aura.AuraResearchManager;
import makeo.gadomancy.common.crafting.InfusionVisualDisguiseArmor;
import makeo.gadomancy.common.crafting.RecipeVisualStickyJar;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.items.ItemAuraCore;
import makeo.gadomancy.common.items.baubles.ItemEtherealFamiliar;
import makeo.gadomancy.common.research.*;
import makeo.gadomancy.common.utils.NBTHelper;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.06.2015 14:01
 */
public class RegisteredResearches {
    private RegisteredResearches() {}

    private static final ResourceLocation CATEGORY_BACKGROUND = new SimpleResourceLocation("/gui/gui_researchback.png");
    private static final ResourceLocation CATEGORY_ICON = new SimpleResourceLocation("/items/category_icon.png");

    public static ResearchItem researchGolemSilverwood;
    public static ResearchItem researchGolemRunicShield;
    public static ResearchItem researchStickyJar;
    public static ResearchItem researchArcaneDropper;
    public static ResearchItem researchGolemCoreBreak;
    public static ResearchItem researchGolemCoreBodyguard;
    public static ResearchItem researchInfusionClaw;
    public static ResearchItem researchRemoteJar;
    public static ResearchItem researchNodeManipulator;
    public static ResearchItem researchEldritchPortalCreator;
    public static ResearchItem researchBlockProtector;

    //Enchantment stuff
    public static ResearchItem researchRevealer;
    public static ResearchItem researchArmorDisguise;

    //Aura core stuff
    public static ResearchItem researchAuraCore;
    public static ResearchItem researchCleanAuraCore;
    public static ResearchItem researchArcanePackager;
    public static ResearchItem researchAuraPylon;
    public static ResearchItem researchAuraEffects;
    public static ResearchItem researchKnowledgeBook;
    public static ResearchItem researchEssentiaCompressor;
    public static ResearchItem researchAiShutdown;

    //Skyblock Helper
    public static ResearchItem researchAncientStones;

    //Growing nodes stuff
    public static ResearchItem researchGrowingNodes;
    public static ResearchItem researchGrowingNodeAgression;
    public static ResearchItem researchGrowingNodeGrowth;
    public static ResearchItem researchGrowingNodeAttackNodes;
    public static ResearchItem researchGrowingNodeGrowthClue;

    //Familiar
    public static ResearchItem researchEtherealFamiliar;
    public static ResearchItem researchFamiliarElementLightning;
    public static ResearchItem researchFamiliarElementFire;
    public static ResearchItem researchFamiliarElementPoison;
    public static ResearchItem researchFamiliarElementWeakness;
    public static ResearchItem researchFamiliarAugmentDamage;
    public static ResearchItem researchFamiliarAugmentSpeed;
    public static ResearchItem researchFamiliarAugmentRange;

    public static void init() {

        ResearchCategories.registerCategory(Gadomancy.MODID, CATEGORY_ICON, CATEGORY_BACKGROUND);

        ResearchItem researchTallowGolem = PseudoResearchItem.create("GOLEMTALLOW", 0, -5).registerResearchItem();

        researchGolemSilverwood = new SimpleResearchItem("GOLEMSILVERWOOD", 1, -2, 3, (ItemStack) RegisteredRecipes.recipeGolemSilverwood.getRecipeOutput(),
                new AspectList().add(Aspect.MOTION, 10).add(Aspect.TREE, 10).add(Aspect.MAGIC, 8).add(Aspect.ORDER, 8).add(Aspect.FLESH, 6).add(Aspect.EXCHANGE, 5))
                .setParents(researchTallowGolem.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.GOLEMSILVERWOOD.1"), new ResearchPage(RegisteredRecipes.recipeGolemSilverwood)).registerResearchItem();

        researchGolemRunicShield = new SimpleResearchItem("GOLEMRUNICSHIELD", 0, 0, 4, new ItemStack(RegisteredItems.itemFakeGolemShield, 1, 32767),
                new AspectList().add(Aspect.AURA, 16).add(Aspect.ARMOR, 6).add(Aspect.MAGIC, 4).add(Aspect.MOTION, 8))
                .setParents(researchGolemSilverwood.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.GOLEMRUNICSHIELD.1"), new ResearchPage(RegisteredRecipes.recipesGolemRunicShield)).registerResearchItem();

        ResearchItem researchBellows = PseudoResearchItem.create("BELLOWS", -2, -5).registerResearchItem();

        researchArcaneDropper = new SimpleResearchItem("ARCANEDROPPER", -1, -3, 2, new ItemStack(RegisteredBlocks.blockArcaneDropper),
                new AspectList().add(Aspect.ORDER, 4).add(Aspect.AIR, 6).add(Aspect.EARTH, 5).add(Aspect.SENSES, 5).add(Aspect.MECHANISM, 7))
                .setParents("DISTILESSENTIA", researchBellows.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.ARCANEDROPPER.1"), new ResearchPage(RegisteredRecipes.recipeArcaneDropper)).registerResearchItem();

        ResearchItem researchHarvestGather = PseudoResearchItem.create("COREHARVEST", 2, -6).registerResearchItem();
        ResearchItem researchCoreGuard = PseudoResearchItem.create("COREGUARD", 4, -5).registerResearchItem();

        ResearchItem researchVEleToolAxe = new ResearchItem("ELEMENTALAXE", Gadomancy.MODID).registerResearchItem();
        ResearchItem researchVEleToolPick = new ResearchItem("ELEMENTALPICK", Gadomancy.MODID).registerResearchItem();
        ResearchItem researchVEleToolShovel = new ResearchItem("ELEMENTALSHOVEL", Gadomancy.MODID).registerResearchItem();
        ResearchItem researchVEleSword = new ResearchItem("ELEMENTALSWORD", Gadomancy.MODID).registerResearchItem();

        researchGolemCoreBreak = new SimpleResearchItem("GOLEMCOREBREAK", 3, -3, 3, new ItemStack(RegisteredItems.itemGolemCoreBreak),
                new AspectList().add(Aspect.TOOL, 8).add(Aspect.ENTROPY, 8).add(Aspect.MECHANISM, 8))
                .setParents(researchHarvestGather.key, researchVEleToolAxe.key, researchVEleToolPick.key, researchVEleToolShovel.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.GOLEMCOREBREAK.1"), new ResearchPage(RegisteredRecipes.recipeGolemCoreBreak)).registerResearchItem();

        ResearchItem researchVBootsTraveller = new ResearchItem("BOOTSTRAVELLER", Gadomancy.MODID).registerResearchItem();

        researchGolemCoreBodyguard = new SimpleResearchItem("GOLEMCOREBODYGUARD", 5, -3, 2, new ItemStack(RegisteredItems.itemGolemCoreBreak, 1, 1),
                new AspectList().add(Aspect.TOOL, 8).add(Aspect.ORDER, 8).add(Aspect.MECHANISM, 10).add(Aspect.ARMOR, 8).add(Aspect.WEAPON, 8))
                .setParents(researchVBootsTraveller.key, researchVEleSword.key, researchCoreGuard.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.GOLEMCOREBODYGUARD.1"), new ResearchPage(RegisteredRecipes.recipeGolemCoreBodyguard)).registerResearchItem();

        ResearchItem researchVFocusPrimal = new ResearchItem("FOCUSPRIMAL", Gadomancy.MODID).registerResearchItem();
        ResearchItem researchItemVoidMetal = PseudoResearchItem.create("VOIDMETAL", -8, -6).registerResearchItem();
        ResearchItem researchWandPedestal = PseudoResearchItem.create("WANDPED", -9, -8).registerResearchItem();
        ResearchItem researchVWardingStone = new ResearchItem("PAVEWARD", Gadomancy.MODID).registerResearchItem();
        ResearchItem researchVNodeStabilizer = new ResearchItem("WANDPEDFOC", Gadomancy.MODID).registerResearchItem();
        ResearchItem researchVCoreUse = new ResearchItem("COREUSE", Gadomancy.MODID).registerResearchItem();
        ResearchItem researchVRunicShielding = new ResearchItem("RUNICARMOR", Gadomancy.MODID).registerResearchItem();

        ResearchItem researchOculus = PseudoResearchItem.create("OCULUS", -11, -2).setRound().setSpecial().registerResearchItem();

        researchInfusionClaw = new SimpleResearchItem("INFUSIONCLAW", -6, -7, 5, new ItemStack(RegisteredBlocks.blockInfusionClaw),
                new AspectList().add(Aspect.ELDRITCH, 8).add(Aspect.MECHANISM, 8).add(Aspect.MAGIC, 8).add(Aspect.ORDER, 8).add(Aspect.DARKNESS, 4))
                .setParents(researchVFocusPrimal.key, researchItemVoidMetal.key, researchWandPedestal.key, researchVCoreUse.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.INFUSIONCLAW.1"), new ResearchPage(RegisteredRecipes.recipeInfusionClaw), new ResearchPage("gadomancy.research_page.INFUSIONCLAW.2")).registerResearchItem();

        researchNodeManipulator = new SimpleResearchItem("NODE_MANIPULATOR", -9, -4, 2, new ItemStack(RegisteredBlocks.blockNodeManipulator, 1, 5),
                new AspectList().add(Aspect.ELDRITCH, 8).add(Aspect.AURA, 6).add(Aspect.DARKNESS, 4).add(Aspect.MAGIC, 8).add(Aspect.GREED, 4).add(Aspect.VOID, 10).add(Aspect.MECHANISM, 6).add(Aspect.EXCHANGE, 4))
                .setConcealed().setSpecial()
                .setParents(researchItemVoidMetal.key, researchVNodeStabilizer.key, researchVWardingStone.key, researchWandPedestal.key)
                .setPages(new ResearchPage("gadomancy.research_page.NODE_MANIPULATOR.1"), new ResearchPage(RegisteredRecipes.recipeNodeManipulator), new ResearchPage("gadomancy.research_page.NODE_MANIPULATOR.3"), new ResearchPage(RegisteredRecipes.recipeRandomizationFocus), new ResearchPage(RegisteredRecipes.multiblockNodeManipulator), new ResearchPage("gadomancy.research_page.NODE_MANIPULATOR.6")).registerResearchItem();

        researchEldritchPortalCreator = new SimpleResearchItem("E_PORTAL_CREATOR", -10, 0, 2, new ItemStack(RegisteredBlocks.blockAdditionalEldrichPortal, 1, 0),
                new AspectList().add(Aspect.ELDRITCH, 6).add(Aspect.DARKNESS, 10).add(Aspect.TRAVEL, 8).add(Aspect.AURA, 10).add(Aspect.VOID, 6).add(Aspect.MECHANISM, 4))
                .setConcealed().setSpecial()
                .setParents(researchNodeManipulator.key, researchOculus.key)
                .setPages(new ResearchPage("gadomancy.research_page.E_PORTAL_CREATOR.1"), new ResearchPage(RegisteredRecipes.recipeAncientPedestal), new ResearchPage("gadomancy.research_page.E_PORTAL_CREATOR.3"), new ResearchPage("gadomancy.research_page.E_PORTAL_CREATOR.4"), new ResearchPage(RegisteredRecipes.recipePortalFocus), new ResearchPage("gadomancy.research_page.E_PORTAL_CREATOR.6"), new ResearchPage(RegisteredRecipes.multiblockEldritchPortalCreator), new ResearchPage("gadomancy.research_page.E_PORTAL_CREATOR.8")).registerResearchItem();

        researchBlockProtector = new SimpleResearchItem("BLOCK_PROTECTOR", -6, -2, 2, new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 2),
                new AspectList().add(Aspect.ORDER, 10).add(Aspect.ARMOR, 8).add(Aspect.EARTH, 12).add(Aspect.LIGHT, 10).add(Aspect.AURA, 8))
                .setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.BLOCK_PROTECTOR.1"), new ResearchPage(RegisteredRecipes.recipeBlockProtector), new ResearchPage("gadomancy.research_page.BLOCK_PROTECTOR.3"), new ResearchPage("gadomancy.research_page.BLOCK_PROTECTOR.4")).registerResearchItem();

        ItemStack displayFamiliar = new ItemStack(RegisteredItems.itemEtherealFamiliar);
        ItemEtherealFamiliar.setFamiliarAspect(displayFamiliar, Aspect.MAGIC);
        researchEtherealFamiliar = new SimpleResearchItem("ETHEREAL_FAMILIAR", 5, 1, 2, displayFamiliar,
                new AspectList().add(Aspect.MAGIC, 8).add(Aspect.FIRE, 6).add(Aspect.AURA, 4))
                .setConcealed().setSpecial()
                .setParents(researchVRunicShielding.key)
                .setPages(new ResearchPage("gadomancy.research_page.ETHEREAL_FAMILIAR.1"), new ResearchPage(RegisteredRecipes.recipesFamiliar),
                        new ResearchPage("gadomancy.research_page.ETHEREAL_FAMILIAR.3"))
                .registerResearchItem();

        researchFamiliarElementLightning = new SimpleResearchItem("FAMILIAR_SHOCK", 4, -1, 2, new ResourceLocation("gadomancy", "textures/misc/Fam_Lightning.png"),
                new AspectList().add(Aspect.WEATHER, 4).add(Aspect.WEAPON, 4).add(Aspect.AIR, 8))
                .setConcealed()
                .setParents(researchEtherealFamiliar.key)
                .setPages(new ResearchPage("gadomancy.research_page.FAMILIAR_SHOCK.1"), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[0]),
                        new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[1]), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[2]))
                .registerResearchItem();

        researchFamiliarElementFire = new SimpleResearchItem("FAMILIAR_FIRE", 6, -1, 2, new ResourceLocation("gadomancy", "textures/misc/Fam_Fire.png"),
                new AspectList().add(Aspect.FIRE, 8).add(Aspect.WEAPON, 4).add(Aspect.MAGIC, 4))
                .setConcealed()
                .setParents(researchEtherealFamiliar.key)
                .setPages(new ResearchPage("gadomancy.research_page.FAMILIAR_FIRE.1"), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[3]),
                        new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[4]), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[5]))
                .registerResearchItem();

        researchFamiliarElementPoison = new SimpleResearchItem("FAMILIAR_POISON", 3, 2, 2, new ResourceLocation("gadomancy", "textures/misc/Fam_Poison.png"),
                new AspectList().add(Aspect.POISON, 8).add(Aspect.WEAPON, 4).add(Aspect.ENTROPY, 4))
                .setConcealed()
                .setParents(researchEtherealFamiliar.key)
                .setPages(new ResearchPage("gadomancy.research_page.FAMILIAR_POISON.1"), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[6]),
                        new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[7]), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[8]))
                .registerResearchItem();

        researchFamiliarElementWeakness = new SimpleResearchItem("FAMILIAR_WEAKNESS", 2, 0, 2, new ResourceLocation("gadomancy", "textures/misc/Fam_Enervation.png"),
                new AspectList().add(Aspect.TRAP, 8).add(Aspect.WEAPON, 8).add(Aspect.TAINT, 6))
                .setConcealed()
                .setParents(researchEtherealFamiliar.key)
                .setPages(new ResearchPage("gadomancy.research_page.FAMILIAR_WEAKNESS.1"), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[9]),
                        new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[10]), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[11]))
                .registerResearchItem();

        researchFamiliarAugmentDamage = new IfAnyParentResearchItem("FAMILIAR_DAMAGE", 8, 2, 2, new ResourceLocation("gadomancy", "textures/misc/Fam_Damage.png"),
                new AspectList().add(Aspect.WEAPON, 8).add(Aspect.ENERGY, 8).add(Aspect.MAGIC, 4))
                .setAnyParents(researchFamiliarElementFire.key, researchFamiliarElementLightning.key,
                        researchFamiliarElementPoison.key, researchFamiliarElementWeakness.key)
                .setConcealed()
                .setParents(researchEtherealFamiliar.key, "VOIDMETAL")
                .setPages(new ResearchPage("gadomancy.research_page.FAMILIAR_DAMAGE.1"), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[12]),
                        new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[13]), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[14]))
                .registerResearchItem();

        researchFamiliarAugmentRange = new IfAnyParentResearchItem("FAMILIAR_RANGE", 6, 4, 2, new ResourceLocation("gadomancy", "textures/misc/Fam_Range.png"),
                new AspectList().add(Aspect.MAGIC, 8).add(Aspect.ELDRITCH, 8).add(Aspect.ORDER, 4))
                .setAnyParents(researchFamiliarElementFire.key, researchFamiliarElementLightning.key,
                        researchFamiliarElementPoison.key, researchFamiliarElementWeakness.key)
                .setConcealed()
                .setParents(researchEtherealFamiliar.key)
                .setPages(new ResearchPage("gadomancy.research_page.FAMILIAR_RANGE.1"), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[15]),
                        new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[16]))
                .registerResearchItem();

        researchFamiliarAugmentSpeed = new SimpleResearchItem("FAMILIAR_SPEED", 8, 5, 2, new ResourceLocation("gadomancy", "textures/misc/Fam_Speed.png"),
                new AspectList().add(Aspect.MOTION, 10).add(Aspect.MAGIC, 4).add(Aspect.AURA, 4))
                .setConcealed()
                .setParents(researchFamiliarAugmentDamage.key, researchFamiliarAugmentRange.key, "PRIMPEARL")
                .setPages(new ResearchPage("gadomancy.research_page.FAMILIAR_SPEED.1"), new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[17]),
                        new ResearchPage(RegisteredRecipes.recipesFamiliarAugmentation[18]))
                .registerResearchItem();


        if(ModConfig.enableAdditionalNodeTypes) {
            researchGrowingNodes = new SimpleResearchItem("GROWING", -8, -1, 5, new ResourceLocation("thaumcraft", "textures/misc/r_nodes.png"), new AspectList())
                    .setSpecial().setLost().setConcealed()
                    .setParents(Gadomancy.MODID.toUpperCase() + ".NODE_MANIPULATOR")
                    .setPages(new ResearchPage("gadomancy.research_page.GROWING.1"), new ResearchPage("gadomancy.research_page.GROWING.2")).registerResearchItem();

            researchGrowingNodeAgression = new SimpleResearchItem("GROWING_AGGRESSION", -5, 0, 6, new ItemStack(ConfigBlocks.blockAiry, 1, 5), new AspectList())
                    .setLost()
                    .setParents(Gadomancy.MODID.toUpperCase() + ".GROWING")
                    .setPages(new ResearchPage("gadomancy.research_page.GROWING_AGGRESSION.1")).registerResearchItem();

            researchGrowingNodeGrowth = new SimpleResearchItem("GROWING_GROWTH", -7, 2, 6, new ResourceLocation("gadomancy", "textures/misc/r_node_star.png"), new AspectList())
                    .setLost()
                    .setParents(Gadomancy.MODID.toUpperCase() + ".GROWING")
                    .setPages(new ResearchPage("gadomancy.research_page.GROWING_GROWTH.1"), new ResearchPage("gadomancy.research_page.GROWING_GROWTH.2")).registerResearchItem();

            researchGrowingNodeAttackNodes = new SimpleResearchItem("GROWING_ATTACK", -6, 1, 6, new ResourceLocation("gadomancy", "textures/misc/r_node_dark.png"), new AspectList())
                    .setLost()
                    .setParents(Gadomancy.MODID.toUpperCase() + ".GROWING")
                    .setPages(new ResearchPage("gadomancy.research_page.GROWING_ATTACK.1")).registerResearchItem();

            researchGrowingNodeGrowthClue = new SimpleResearchItem("GROWING_GROWTHCLUE", -4, 3, 2, new ItemStack(ConfigBlocks.blockAiry, 1, 5),
                    new AspectList().add(Aspect.AURA, 10).add(Aspect.MAGIC, 15).add(Aspect.ELDRITCH, 5).add(Aspect.VOID, 12).add(Aspect.ENERGY, 12).add(Aspect.ENTROPY, 8).add(Aspect.DEATH, 3)
                            .add(Aspect.GREED, 10).add(Aspect.HUNGER, 10).add(Aspect.MECHANISM, 12).add(Aspect.DARKNESS, 12))
                    .setSpecial().setRound().setConcealed()
                    .setParents(Gadomancy.MODID.toUpperCase() + ".GROWING_AGGRESSION", Gadomancy.MODID.toUpperCase() + ".GROWING_GROWTH", Gadomancy.MODID.toUpperCase() + ".GROWING_ATTACK")
                    .setPages(new ResearchPage("gadomancy.research_page.GROWING_GROWTHCLUE.1"), new ResearchPage("gadomancy.research_page.GROWING_GROWTHCLUE.2")).registerResearchItem();

        }

        if(ModConfig.ancientStoneRecipes)
            researchAncientStones = new AlternatingResearchItem("ANCIENT_STONES",
                -12, 1, 3,
                new AspectList().add(Aspect.ELDRITCH, 10).add(Aspect.EARTH, 8).add(Aspect.EXCHANGE, 6).add(Aspect.ENTROPY, 8),
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 15))
                .setConcealed()
                .setParents(researchOculus.key)
                .setPages(new ResearchPage("gadomancy.research_page.ANCIENT_STONES.1"), new ResearchPage(RegisteredRecipes.recipeAncientStone), new ResearchPage(RegisteredRecipes.recipeAncientStonePedestal)).registerResearchItem();

        ItemAuraCore.AuraCoreType[] coreTypes = ItemAuraCore.AuraCoreType.values();
        ItemStack[] cores = new ItemStack[coreTypes.length];
        for (int i = 0; i < coreTypes.length; i++) {
            ItemStack stack = new ItemStack(RegisteredItems.itemAuraCore, 1, 0);
            RegisteredItems.itemAuraCore.setCoreType(stack, coreTypes[i]);
            cores[i] = stack;
        }

        //Cores and stuff related to it.
        researchAuraCore = new AlternatingResearchItem("AURA_CORE",
                -1, 6, 2,
                new AspectList().add(Aspect.AURA, 22).add(Aspect.MAGIC, 10).add(Aspect.AIR, 8).add(Aspect.WATER, 8)
                        .add(Aspect.EARTH, 8).add(Aspect.FIRE, 8).add(Aspect.ORDER, 8).add(Aspect.ENTROPY, 8),
                cores)
                .setConcealed().setSpecial()
                .setParents("INFUSION", "THAUMIUM")
                .setPages(new ResearchPage("gadomancy.research_page.AURA_CORE.1"), new ResearchPage(RegisteredRecipes.recipeAuraCore), new ResearchPage("gadomancy.research_page.AURA_CORE.3"), new MultiResearchPage(RegisteredRecipes.auraCoreRecipes)).registerResearchItem();

        researchCleanAuraCore = new SimpleResearchItem("CLEAN_AURA_CORE",
                1, 5, 0, new ItemStack(RegisteredItems.itemAuraCore),
                new AspectList().add(Aspect.HEAL, 8).add(Aspect.AURA, 6).add(Aspect.MAGIC, 8).add(Aspect.WATER, 10))
                .setConcealed().setSecondary()
                .setParents(researchAuraCore.key)
                .setPages(new ResearchPage("gadomancy.research_page.CLEAN_AURA_CORE.1"), new ResearchPage(RegisteredRecipes.recipesWashAuraCore), new ResearchPage(RegisteredRecipes.recipesUndoAuraCore)).registerResearchItem();

        researchAuraPylon = new SimpleResearchItem("AURA_PYLON",
                0, 9, 2, new ItemStack(RegisteredBlocks.blockAuraPylon, 1, 1),
                new AspectList().add(Aspect.ORDER, 12).add(Aspect.AURA, 20).add(Aspect.MAGIC, 12).add(Aspect.MECHANISM, 8))
                .setHidden()
                .setParents(researchAuraCore.key, "TUBEFILTER")
                .setItemTriggers(new ItemStack(RegisteredItems.itemAuraCore, 1, ItemAuraCore.AuraCoreType.FIRE.ordinal()))
                .setPages(new ResearchPage("gadomancy.research_page.AURA_PYLON.1"), new ResearchPage(RegisteredRecipes.recipeAuraPylon), new ResearchPage(RegisteredRecipes.recipeAuraPylonPeak), new ResearchPage("gadomancy.research_page.AURA_PYLON.4"), new ResearchPage(RegisteredRecipes.multiblockAuraPylon)).registerResearchItem();

        researchAuraEffects = new SimpleResearchItem("AURA_EFFECTS",
                1, 10, 2, new ResourceLocation("thaumcraft", "textures/misc/r_aspects.png"),
                new AspectList().add(Aspect.AURA, 6).add(Aspect.MIND, 8).add(Aspect.MAGIC, 4).add(Aspect.ORDER, 4))
                .setConcealed().setSecondary()
                .setParents(researchAuraPylon.key)
                .setPages(new ResearchPage("gadomancy.research_page.AURA_EFFECTS.1")).registerResearchItem();

        researchKnowledgeBook = new SimpleResearchItem("KNOWLEDGE_BOOK",
                -2, 8, 2, new ItemStack(RegisteredBlocks.blockKnowledgeBook),
                new AspectList().add(Aspect.MIND, 12).add(Aspect.MAGIC, 8).add(Aspect.MOTION, 6).add(Aspect.ORDER, 10).add(Aspect.MECHANISM, 8).add(Aspect.CRAFT, 8))
                .setHidden()
                .setParents(researchAuraCore.key, "GOGGLES", "THAUMATORIUM")
                .setItemTriggers(new ItemStack(RegisteredItems.itemAuraCore, 1, ItemAuraCore.AuraCoreType.ORDER.ordinal()))
                .setPages(new ResearchPage("gadomancy.research_page.KNOWLEDGE_BOOK.1"), new ResearchPage(RegisteredRecipes.recipeKnowledgeBook),
                        new ResearchPage("gadomancy.research_page.KNOWLEDGE_BOOK.3"), new ResearchPage("gadomancy.research_page.KNOWLEDGE_BOOK.4")).registerResearchItem();

        ResearchItem researchVReservoir = PseudoResearchItem.create("ESSENTIARESERVOIR", -5, 9).registerResearchItem();

        researchEssentiaCompressor = new SimpleResearchItem("ESSENTIA_COMPRESSOR",
                -4, 7, 2, new ItemStack(RegisteredBlocks.blockEssentiaCompressor),
                new AspectList().add(Aspect.VOID, 12).add(Aspect.WATER, 10).add(Aspect.ENTROPY, 12).add(Aspect.MAGIC, 6).add(Aspect.MECHANISM, 12))
                .setHidden()
                .setParents(researchAuraCore.key, researchVReservoir.key)
                .setItemTriggers(new ItemStack(RegisteredItems.itemAuraCore, 1, ItemAuraCore.AuraCoreType.WATER.ordinal()))
                .setPages(new ResearchPage("gadomancy.research_page.ESSENTIA_COMPRESSOR.1"), new ResearchPage("gadomancy.research_page.ESSENTIA_COMPRESSOR.2"),
                        new ResearchPage(RegisteredRecipes.recipeElementVoid), new ResearchPage("gadomancy.research_page.ESSENTIA_COMPRESSOR.4"),
                        new ResearchPage(RegisteredRecipes.recipeEssentiaCompressor), new ResearchPage("gadomancy.research_page.ESSENTIA_COMPRESSOR.6"),
                        new ResearchPage(RegisteredRecipes.multiblockEssentiaCompressor), new ResearchPage("gadomancy.research_page.ESSENTIA_COMPRESSOR.8"),
                        new ResearchPage("gadomancy.research_page.ESSENTIA_COMPRESSOR.9")).registerResearchItem();

        /*researchAiShutdown = new SimpleResearchItem("AI_SHUTDOWN",
                -3, 5, 2, new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 5),
                new AspectList().add(Aspect.MIND, 10).add(Aspect.ENTROPY, 20).add(Aspect.AURA, 12).add(Aspect.BEAST, 16).add(Aspect.MECHANISM, 12))
                .setHidden()
                .setParents(researchAuraCore.key, "ROD_greatwood")
                .setItemTriggers(new ItemStack(RegisteredItems.itemAuraCore, 1, ItemAuraCore.AuraCoreType.EARTH.ordinal()))
                .setPages(new ResearchPage("gadomancy.research_page.AI_SHUTDOWN.1"), new ResearchPage(RegisteredRecipes.recipeAiShutdown))
                .registerResearchItem();*/

        String[] packagerParents = Config.wardedStone ? new String[] { researchAuraCore.key, "WARDEDARCANA" } : new String[] { researchAuraCore.key };
                researchArcanePackager = new SimpleResearchItem("ARCANE_PACKAGER",
                2, 7, 2, new ItemStack(RegisteredBlocks.blockStoneMachine, 1, 4),
                        new AspectList().add(Aspect.AIR, 12).add(Aspect.MECHANISM, 16).add(Aspect.VOID, 10).add(Aspect.MAGIC, 10))
                .setHidden()
                .setParents(packagerParents)
                .setItemTriggers(new ItemStack(RegisteredItems.itemAuraCore, 1, ItemAuraCore.AuraCoreType.AIR.ordinal()))
                .setPages(new ResearchPage("gadomancy.research_page.ARCANE_PACKAGER.1"), new ResearchPage(RegisteredRecipes.recipeArcanePackager), new ResearchPage("gadomancy.research_page.ARCANE_PACKAGER.3"), new ResearchPage("gadomancy.research_page.ARCANE_PACKAGER.4")).registerResearchItem();


        ResearchItem researchInfusionEnch = PseudoResearchItem.create("INFUSIONENCHANTMENT", -1, 2).registerResearchItem();

        ItemStack goggles = new ItemStack(ConfigItems.itemGoggles);
        goggles.addEnchantment(Enchantment.protection, 1);
        researchRevealer = new SimpleResearchItem("REVEALER", 1, 3, 1, goggles, new AspectList().add(Aspect.SENSES, 4).add(Aspect.MAGIC, 4))
                .setParents(researchInfusionEnch.key, "GOGGLES").setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.REVEALER.1"), new ResearchPage(RegisteredRecipes.recipeRevealer)).registerResearchItem();

        researchArmorDisguise = new DisguiseResearchItem("ARMORDISGUISE", -2, 0, 1, new AspectList().add(Aspect.SLIME, 8).add(Aspect.MAGIC, 4).add(Aspect.ARMOR, 4))
                .setParents(researchInfusionEnch.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.ARMORDISGUISE.1"), new ResearchPage(new InfusionVisualDisguiseArmor(false)), new ResearchPage(new InfusionVisualDisguiseArmor(true)), new ResearchPage("gadomancy.research_page.ARMORDISGUISE.4")).registerResearchItem();

        //Warpy warpy
        ThaumcraftApi.addWarpToResearch(Gadomancy.MODID.toUpperCase() + ".NODE_MANIPULATOR", 4);
        ThaumcraftApi.addWarpToResearch(Gadomancy.MODID.toUpperCase() + ".GROWING_GROWTHCLUE", 3);
        ThaumcraftApi.addWarpToResearch(Gadomancy.MODID.toUpperCase() + ".E_PORTAL_CREATOR", 4);
        ThaumcraftApi.addWarpToResearch(Gadomancy.MODID.toUpperCase() + ".KNOWLEDGE_BOOK", 1);
        ThaumcraftApi.addWarpToResearch(Gadomancy.MODID.toUpperCase() + ".ESSENTIA_COMPRESSOR", 3);
    }

    public static void postInit() {
        ResearchItem researchJar = PseudoResearchItem.create("JARLABEL", -3, -7).registerResearchItem();

        RecipeVisualStickyJar visualRecipe = new RecipeVisualStickyJar();
        ResearchPage page = new ResearchPage(visualRecipe);

        researchStickyJar = new StickyJarResearchItem("STICKYJAR", -5, -5, 2,
                new AspectList().add(Aspect.SLIME, 8).add(Aspect.EARTH, 8))
                .setParents(researchJar.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.STICKYJAR.1"), page).registerResearchItem();

        if(researchBlockProtector != null) {
            researchBlockProtector.setParents(researchStickyJar.key);
        }

        String[] parents = Config.allowMirrors ? new String[] { researchJar.key, "MIRRORESSENTIA" } : new String[] { researchJar.key };
        researchRemoteJar = new SimpleResearchItem("REMOTEJAR", -4, -3, 3,
                RegisteredRecipes.recipeRemoteJar.getRecipeOutput(),
                new AspectList().add(Aspect.WATER, 4).add(Aspect.MECHANISM, 8).add(Aspect.EARTH, 4).add(Aspect.ORDER, 8))
                .setParents(parents).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.REMOTEJAR.1"), new ResearchPage(RegisteredRecipes.recipeRemoteJar), new ResearchPage("gadomancy.research_page.REMOTEJAR.2")).registerResearchItem();

        //Aura researches
        AuraResearchManager.registerAuraResearches();
    }
}
