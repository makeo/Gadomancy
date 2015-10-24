package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.registry.GameRegistry;
import makeo.gadomancy.common.CommonProxy;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.blocks.BlockAiryItem;
import thaumcraft.common.config.ConfigBlocks;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 24.10.2015 16:48
 */
public class ModSubstitutions {

    public static void preInit() {
        try {
            RegisteredItems.itemBlockAiryCopy = new BlockAiryItem(RegisteredBlocks.blockNode);
            GameRegistry.addSubstitutionAlias("Thaumcraft:blockAiry", GameRegistry.Type.BLOCK, RegisteredBlocks.blockNode);
            GameRegistry.addSubstitutionAlias("Thaumcraft:blockAiry", GameRegistry.Type.ITEM, RegisteredItems.itemBlockAiryCopy);
            ConfigBlocks.blockAiry = RegisteredBlocks.blockNode;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {

    }

    public static void postInit() {
        CommonProxy.unregisterWandHandler("Thaumcraft", Blocks.glass, -1);

        //TC fix
        ResearchItem ri = ResearchCategories.getResearchList("BASICS").research.get("NODEJAR");
        ResearchPage page = ri.getPages()[1];
        List recipe = (List) page.recipe;
        List blocks = (List) recipe.get(4);
        blocks.set(22, new ItemStack(RegisteredItems.itemBlockAiryCopy, 1, 5));
    }

    public static void replaceAutomagyResearch() {
        //Automagy fix
        ResearchItem ri = ResearchCategories.getResearchList("AUTOMAGY").research.get("ADVNODEJAR");
        ResearchPage page = ri.getPages()[1];
        List recipe = (List) page.recipe;
        List blocks = (List) recipe.get(4);
        blocks.set(22, new ItemStack(RegisteredItems.itemBlockAiryCopy, 1, 5));
    }
}
