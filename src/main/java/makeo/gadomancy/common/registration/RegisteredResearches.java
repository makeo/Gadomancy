package makeo.gadomancy.common.registration;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.research.AlternatingResearchItem;
import makeo.gadomancy.common.research.PseudoResearchItem;
import makeo.gadomancy.common.research.SimpleResearchItem;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.research.ResearchManager;

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
    public static ResearchItem researchInfusionClaw;
    public static ResearchItem researchRemoteJar;
    public static ResearchItem researchGrowingNodes;
    public static ResearchItem researchGrowingNodeAgression;
    public static ResearchItem researchGrowingNodeGrowth;
    public static ResearchItem researchGrowingNodeGrowthClue;

    public static void init() {

        ResearchCategories.registerCategory(Gadomancy.MODID, CATEGORY_ICON, CATEGORY_BACKGROUND);

        ResearchItem researchAdvancedGolem = PseudoResearchItem.create("ADVANCEDGOLEM", 2, -2).registerResearchItem();

        researchGolemSilverwood = new SimpleResearchItem("GOLEMSILVERWOOD", 4, -3, 3, (ItemStack) RegisteredRecipes.recipeGolemSilverwood.getRecipeOutput(),
                new AspectList().add(Aspect.MOTION, 10).add(Aspect.TREE, 10).add(Aspect.MAGIC, 8).add(Aspect.ORDER, 8).add(Aspect.FLESH, 6).add(Aspect.EXCHANGE, 5))
                .setParents(researchAdvancedGolem.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.GOLEMSILVERWOOD.1"), new ResearchPage(RegisteredRecipes.recipeGolemSilverwood)).registerResearchItem();

        researchGolemRunicShield = new SimpleResearchItem("GOLEMRUNICSHIELD", 6, -4, 4, new ItemStack(RegisteredItems.itemFakeGolemShield, 1, 32767),
                new AspectList().add(Aspect.AURA, 16))
                .setParents(researchGolemSilverwood.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.GOLEMRUNICSHIELD.1"), new ResearchPage(RegisteredRecipes.recipesGolemRunicShield)).registerResearchItem();

        ResearchItem researchDistill = PseudoResearchItem.create("DISTILESSENTIA", -2, -1).registerResearchItem();

        researchArcaneDropper = new SimpleResearchItem("ARCANEDROPPER", -1, -1, 2, new ItemStack(RegisteredBlocks.blockArcaneDropper),
                new AspectList().add(Aspect.ORDER, 4).add(Aspect.AIR, 6).add(Aspect.EARTH, 5).add(Aspect.SENSES, 5).add(Aspect.MECHANISM, 7))
                .setParents(researchDistill.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.ARCANEDROPPER.1"), new ResearchPage(RegisteredRecipes.recipeArcaneDropper)).registerResearchItem();

        ResearchItem researchCoreGather = PseudoResearchItem.create("COREGATHER", 3, 0).registerResearchItem();

        researchGolemCoreBreak = new SimpleResearchItem("GOLEMCOREBREAK", 5, -1, 3, new ItemStack(RegisteredItems.itemGolemCoreBreak),
                new AspectList().add(Aspect.TOOL, 8).add(Aspect.ENTROPY, 8).add(Aspect.MECHANISM, 8))
                .setParents(researchCoreGather.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.GOLEMCOREBREAK.1"), new ResearchPage(RegisteredRecipes.recipeGolemCoreBreak)).registerResearchItem();

        ResearchItem researchFocusPrimal = PseudoResearchItem.create("FOCUSPRIMAL", -2, -3).registerResearchItem();

        researchInfusionClaw = new SimpleResearchItem("INFUSIONCLAW", -1, -3, 5, new ItemStack(RegisteredBlocks.blockInfusionClaw),
                new AspectList().add(Aspect.ELDRITCH, 8).add(Aspect.MECHANISM, 8).add(Aspect.MAGIC, 8).add(Aspect.ORDER, 8).add(Aspect.DARKNESS, 4))
                .setParents(researchFocusPrimal.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.INFUSIONCLAW.1"), new ResearchPage(RegisteredRecipes.recipeInfusionClaw)).registerResearchItem();

        researchGrowingNodes = new ResearchItem(Gadomancy.MODID.toUpperCase() + ".GROWING", Gadomancy.MODID, new AspectList(), 2, 2, 5, new ResourceLocation("thaumcraft", "textures/misc/r_nodes.png"))
                .setSpecial().setLost()
                .setPages(new ResearchPage("gadomancy.research_page.GROWING.1"), new ResearchPage("gadomancy.research_page.GROWING.2")).registerResearchItem();

        researchGrowingNodeAgression = new ResearchItem(Gadomancy.MODID.toUpperCase() + ".GROWING_AGGRESSION", Gadomancy.MODID, new AspectList(), 5, 3, 6, new ItemStack(ConfigBlocks.blockAiry, 1, 5))
                .setLost()
                .setParents(Gadomancy.MODID.toUpperCase() + ".GROWING")
                .setPages(new ResearchPage("gadomancy.research_page.GROWING_AGGRESSION.1")).registerResearchItem();

        researchGrowingNodeGrowth = new ResearchItem(Gadomancy.MODID.toUpperCase() + ".GROWING_GROWTH", Gadomancy.MODID, new AspectList(), 3, 5, 6, new ResourceLocation("thaumcraft", "textures/misc/r_nodes_2.png"))
                .setLost()
                .setParents(Gadomancy.MODID.toUpperCase() + ".GROWING")
                .setPages(new ResearchPage("gadomancy.research_page.GROWING_GROWTH.1"), new ResearchPage("gadomancy.research_page.GROWING_GROWTH.2")).registerResearchItem();

        researchGrowingNodeGrowthClue = new ResearchItem(Gadomancy.MODID.toUpperCase() + ".GROWING_GROWTHCLUE", Gadomancy.MODID,
                new AspectList().add(Aspect.AURA, 10).add(Aspect.MAGIC, 15).add(Aspect.ELDRITCH, 5).add(Aspect.VOID, 12).add(Aspect.ENERGY, 12).add(Aspect.ENTROPY, 8).add(Aspect.DEATH, 3)
                .add(Aspect.GREED, 10).add(Aspect.HUNGER, 10).add(Aspect.MECHANISM, 12).add(Aspect.DARKNESS, 12),
                6, 6, 2, new ItemStack(ConfigBlocks.blockAiry, 1, 0))
                .setSpecial().setRound().setConcealed()
                .setParents(Gadomancy.MODID.toUpperCase() + ".GROWING_AGGRESSION", Gadomancy.MODID.toUpperCase() + ".GROWING_GROWTH")
                .setPages(new ResearchPage("gadomancy.research_page.GROWING_GROWTHCLUE.1"), new ResearchPage("gadomancy.research_page.GROWING_GROWTHCLUE.2")).registerResearchItem();

        ThaumcraftApi.addWarpToResearch(Gadomancy.MODID.toUpperCase() + ".GROWING_GROWTHCLUE", 3);
    }

    public static void postInit() {
        ResearchItem researchVoidJar = PseudoResearchItem.create("JARVOID", -2, 1).registerResearchItem();

        researchStickyJar = new AlternatingResearchItem("STICKYJAR", -1, 1, 2,
                new AspectList().add(Aspect.SLIME, 8).add(Aspect.EARTH, 8),
                RegisteredItems.getStickyJarStacks())
                .setParents(researchVoidJar.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.STICKYJAR.1"), new ResearchPage(RegisteredRecipes.getVisualStickyJarRecipes())).registerResearchItem();

        researchRemoteJar = new SimpleResearchItem("REMOTEJAR", -2, 2, 3,
                RegisteredRecipes.recipeRemoteJar.getRecipeOutput(),
                new AspectList().add(Aspect.WATER, 4).add(Aspect.MECHANISM, 8).add(Aspect.EARTH, 4).add(Aspect.ORDER, 8))
                .setParents(researchVoidJar.key).setConcealed()
                .setPages(new ResearchPage("gadomancy.research_page.REMOTEJAR.1"), new ResearchPage(RegisteredRecipes.recipeRemoteJar)).registerResearchItem();
    }
}
