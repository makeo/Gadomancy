package makeo.gadomancy.common.registration;

import makeo.gadomancy.common.items.ItemArcanePackage;
import makeo.gadomancy.common.items.ItemAuraCore;
import makeo.gadomancy.common.items.ItemFakeLootbag;
import makeo.gadomancy.common.items.ItemFakeModIcon;
import makeo.gadomancy.common.items.baubles.ItemEtherealFamiliar;
import makeo.gadomancy.common.research.SimpleResearchItem;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 03.06.2015 03:37
 */
public class RegisteredItems {

    private RegisteredItems() {}

    public static ItemFakeModIcon itemFakeModIcon;
    //public static ItemFakeGolemShield itemFakeGolemShield;
    //public static ItemFakeGolemPlacer itemFakeGolemPlacer;

    //public static ItemTransformationFocus itemTransformationFocus;
    //public static ItemGolemCoreBreak itemGolemCoreBreak;
    //public static ItemExtendedNodeJar itemExtendedNodeJar;
    //public static ItemFamiliar_Old itemFamiliar_old;
    public static ItemEtherealFamiliar itemEtherealFamiliar;
    //public static ItemCreativeNode itemCreativeNode;
    public static ItemArcanePackage itemPackage;
    public static ItemFakeLootbag itemFakeLootbag;
    public static ItemAuraCore itemAuraCore;

    public static void preInit() {}

    public static void init() {
        registerItems();

        registerDefaultStickyJars();
    }

    public static void postInit() {
        registerItemAspects();
    }

    //Items
    private static void registerItems() {
        //itemTransformationFocus = registerItem(new ItemTransformationFocus());
        //itemGolemCoreBreak = registerItem(new ItemGolemCoreBreak());
        //itemFamiliar_old = registerItem(new ItemFamiliar_Old(), "ItemFamiliar");
        itemEtherealFamiliar = registerItem(new ItemEtherealFamiliar());
        itemAuraCore = registerItem(new ItemAuraCore());

        //itemCreativeNode = registerItem(new ItemCreativeNode());
        itemPackage = registerItem(new ItemArcanePackage());
        itemFakeLootbag = registerItem(Thaumcraft.MODID, new ItemFakeLootbag());
        //itemFakeGolemPlacer = registerItem(new ItemFakeGolemPlacer());
        //itemFakeGolemShield = registerItem(new ItemFakeGolemShield());
        //itemExtendedNodeJar = registerItem(new ItemExtendedNodeJar());
        itemFakeModIcon = registerItem(new ItemFakeModIcon());
    }

    private static <T extends Item> T registerItem(T item, String name) {
        GameRegistry.registerItem(item, name);
        return item;
    }

    private static <T extends Item> T registerItem(T item) {
        return registerItem(item, item.getClass().getSimpleName());
    }

    private static <T extends Item> T registerItem(String modId, T item) {
        return registerItem(modId, item, item.getClass().getSimpleName());
    }

    private static <T extends Item> T registerItem(String modId, T item, String name) {
        Injector modController = new Injector(new Injector(Loader.instance(), Loader.class).getField("modController"), LoadController.class);
        Object old = modController.getField("activeContainer");
        modController.setField("activeContainer", Loader.instance().getIndexedModList().get(modId));

        GameRegistry.registerItem(item, name);

        modController.setField("activeContainer", old);
        return item;
    }

    private static void registerItemAspects() {
        AspectList oldAspects = ThaumcraftCraftingManager.getObjectTags(new ItemStack(RegisteredBlocks.blockStoneMachine));

        ThaumcraftApi.registerObjectTag(new ItemStack(Item.getItemFromBlock(RegisteredBlocks.blockStoneMachine)), new int[]{11, 15}, new AspectList());
        ThaumcraftApi.registerObjectTag(new ItemStack(Item.getItemFromBlock(RegisteredBlocks.blockStoneMachine)), new int[]{0}, oldAspects);

        AspectList pylon = new AspectList().add(Aspect.WATER, 10).add(Aspect.AURA, 12).add(Aspect.VOID, 4).add(Aspect.MECHANISM, 4);
        ThaumcraftApi.registerObjectTag(new ItemStack(Item.getItemFromBlock(RegisteredBlocks.blockAuraPylon)), new int[]{0}, pylon);
        pylon = new AspectList().add(Aspect.FIRE, 10).add(Aspect.AURA, 12).add(Aspect.AURA, 8).add(Aspect.LIGHT, 4).add(Aspect.MECHANISM, 4);
        ThaumcraftApi.registerObjectTag(new ItemStack(Item.getItemFromBlock(RegisteredBlocks.blockAuraPylon)), new int[]{1}, pylon);

        AspectList packager = new AspectList().add(Aspect.PLANT, 10).add(Aspect.MECHANISM, 8).add(Aspect.CRAFT, 8).add(Aspect.AURA, 12);
        ThaumcraftApi.registerObjectTag(new ItemStack(Item.getItemFromBlock(RegisteredBlocks.blockStoneMachine)), new int[]{4}, packager);

        AspectList aspect = new AspectList();
        aspect.add(Aspect.METAL, 6).add(Aspect.AURA, 12).add(Aspect.ELDRITCH, 4).add(Aspect.VOID, 10);
        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemAuraCore), new int[]{0}, aspect.copy());
        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemAuraCore), new int[]{1}, aspect.copy().add(Aspect.AIR, 26));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemAuraCore), new int[]{2}, aspect.copy().add(Aspect.FIRE, 26));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemAuraCore), new int[]{3}, aspect.copy().add(Aspect.WATER, 26));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemAuraCore), new int[]{4}, aspect.copy().add(Aspect.EARTH, 26));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemAuraCore), new int[]{5}, aspect.copy().add(Aspect.ORDER, 26));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemAuraCore), new int[]{6}, aspect.copy().add(Aspect.ENTROPY, 26));

        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemPackage, 1, 0), new AspectList().add(Aspect.DESIRE, 2).add(Aspect.BEAST, 2).add(Aspect.ORDER, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemPackage, 1, 1), new AspectList().add(Aspect.DESIRE, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemFakeLootbag, 1, 0), new AspectList().add(Aspect.DESIRE, 2).add(Aspect.BEAST, 2).add(Aspect.ORDER, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredItems.itemFakeLootbag, 1, 1), new AspectList().add(Aspect.DESIRE, 4));

        ThaumcraftApi.registerObjectTag(new ItemStack(RegisteredBlocks.blockKnowledgeBook), new AspectList().add(Aspect.MIND, 8).add(Aspect.MECHANISM, 4).add(Aspect.AURA, 6).add(Aspect.ORDER, 4));
    }

    //Sticky jars
    private static void registerDefaultStickyJars() {
        registerStickyJar(Item.getItemFromBlock(BlocksTC.jar), 0);
        registerStickyJar(Item.getItemFromBlock(BlocksTC.jar), 1);

        Item itemBlockJar = Item.getItemFromBlock(BlocksTC.jar);
        registerStickyJar(itemBlockJar, 0, new ItemStack(itemBlockJar, 1, 0), "JARLABEL");
        registerStickyJar(itemBlockJar, 3, new ItemStack(itemBlockJar, 1, 1), "JARVOID");

        Item itemRemoteJar = Item.getItemFromBlock(RegisteredBlocks.blockRemoteJar);
        registerStickyJar(itemRemoteJar, 0, new ItemStack(itemRemoteJar), SimpleResearchItem.PREFIX + "REMOTEJAR");
    }

    private static List<StickyJarItemInfo> stickyJarItems = new ArrayList<StickyJarItemInfo>();

    public static void registerStickyJar(Item item, int damage, ItemStack recipeStack) {
        registerStickyJar(item, damage, recipeStack, null);
    }

    public static void registerStickyJar(Item item, int damage, ItemStack recipeStack, String research) {
        StickyJarItemInfo info = new StickyJarItemInfo();
        info.item = item;
        info.damage = damage;
        info.recipeStack = recipeStack;
        info.research = research;
        stickyJarItems.add(info);
    }

    public static void registerStickyJar(Item item, int damage) {
        registerStickyJar(item, damage, null);
    }

    public static void registerStickyJar(Item item) {
        registerStickyJar(item, Short.MAX_VALUE);
    }

    public static boolean isStickyableJar(Item item, int damage) {
        for(StickyJarItemInfo info : stickyJarItems) {
            if(info.item == item && (info.damage == damage || info.damage == Short.MAX_VALUE)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStickyableJar(ItemStack stack) {
        return stack != null && isStickyableJar(stack.getItem(), stack.getItemDamage());
    }

    public static List<ItemStack> getStickyJarStacks() {
        return getStickyJarStacks(null);
    }

    public static List<ItemStack> getStickyJarStacks(EntityPlayer player) {
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for(StickyJarItemInfo info : stickyJarItems) {
            if(info.recipeStack != null && (player == null || info.research == null
                    || ResearchManager.isResearchComplete(player.getName(), info.research))) {
                stacks.add(info.recipeStack);
            }
        }
        return stacks;
    }

    private static class StickyJarItemInfo {
        public Item item;
        public int damage;
        public ItemStack recipeStack;
        public String research = null;
    }
}
