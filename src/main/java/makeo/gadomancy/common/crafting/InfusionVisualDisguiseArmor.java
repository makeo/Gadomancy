package makeo.gadomancy.common.crafting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.Gadomancy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.internal.DummyInternalMethodHandler;
import thaumcraft.api.internal.IInternalMethodHandler;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 26.12.2015 18:10
 */
public class InfusionVisualDisguiseArmor extends InfusionRecipe {
    private static final IInternalMethodHandler FAKE_HANDLER = new FakeMethodHandler();
    private static final int[] POTION_METAS = new int[]{8206, 16398, 8270, 16462};

    private static ItemStack[] armorItems;

    private final ItemStack[] components;
    private final boolean transparent;

    public InfusionVisualDisguiseArmor(boolean transparent) {
        super("", new ItemStack(Blocks.cobblestone), 0, InfusionDisguiseArmor.ASPECTS, new ItemStack(Blocks.dirt), new ItemStack[0]);
        this.transparent = transparent;

        components = new ItemStack[InfusionDisguiseArmor.COMPONENTS.length + 1];
        System.arraycopy(InfusionDisguiseArmor.COMPONENTS, 0, components, 1, InfusionDisguiseArmor.COMPONENTS.length);

        if(armorItems == null && Gadomancy.proxy.getSide() == Side.CLIENT) {
            findArmorItems();
        }
    }

    private void findArmorItems() {
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (Object o : Item.itemRegistry) {
            if (o instanceof Item && ((Item) o).getCreativeTab() != null) {
                List list = new ArrayList();

                try {
                    ((Item) o).getSubItems((Item) o, ((Item) o).getCreativeTab(), list);
                } catch (Throwable ignored) {
                }

                for (Object o2 : list) {
                    if (o2 != null && o2 instanceof ItemStack
                            && EntityLiving.getArmorPosition((ItemStack) o2) != 0) {
                        items.add((ItemStack) o2);
                    }
                }
            }
        }
        armorItems = items.toArray(new ItemStack[items.size()]);
    }

    private boolean canSee(ItemStack stack) {
        String research = getResearchKey(stack);
        return research == null || ResearchManager.isResearchComplete(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), research);
    }

    public ItemStack getCurrentDisguise() {
        if(transparent) {
            return new ItemStack(Items.potionitem, 1, POTION_METAS[(int)((System.currentTimeMillis() / 1000) % POTION_METAS.length)]);
        }

        Random random = new Random(System.currentTimeMillis() / 1000);
        int first = getRecipeInput(random);
        int firstPos = EntityLiving.getArmorPosition(armorItems[first]);

        int next;
        do {
            next = random.nextInt(armorItems.length);
        }
        while (next == first || EntityLiving.getArmorPosition(armorItems[next]) != firstPos || !canSee(armorItems[next]));
        return armorItems[next];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getRecipeInput() {
        if(Minecraft.getMinecraft().thePlayer == null) {
            return new ItemStack(Items.cookie);
        }
        return armorItems[getRecipeInput(new Random(System.currentTimeMillis() / 1000))];
    }

    private int getRecipeInput(Random random) {
        int index;
        do {
            index = random.nextInt(armorItems.length);
        } while (!canSee(armorItems[index]));
        return index;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getRecipeOutput(ItemStack input) {
        if(Minecraft.getMinecraft().thePlayer == null) {
            return new ItemStack(Items.cookie);
        }
        return InfusionDisguiseArmor.disguiseStack(input, getCurrentDisguise());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack[] getComponents() {
        components[0] = getCurrentDisguise();
        return components;
    }

    @SideOnly(Side.CLIENT)
    private static String getResearchKey(ItemStack stack) {
        IInternalMethodHandler old = ThaumcraftApi.internalMethods;
        ThaumcraftApi.internalMethods = FAKE_HANDLER;
        Object[] result = ThaumcraftApi.getCraftingRecipeKey(Minecraft.getMinecraft().thePlayer, stack);
        ThaumcraftApi.internalMethods = old;
        return result == null ? null : (String)result[0];
    }

    private static class FakeMethodHandler extends DummyInternalMethodHandler {
        @Override
        public boolean isResearchComplete(String username, String researchkey) {
            return true;
        }
    }
}
