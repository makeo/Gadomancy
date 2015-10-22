package makeo.gadomancy.client.renderers.item;

import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.client.renderers.tile.ItemNodeRenderer;
/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 22.10.2015 20:11
 */
public class ItemExNodeRenderer extends ItemNodeRenderer {

    public ItemExNodeRenderer() {
        //AspectList list = new AspectList().add(Aspect.FIRE, 100).add(Aspect.ENTROPY, 100).add(Aspect.WATER, 50);
        //new Injector(this).setField("aspects", list);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return (item != null) && (item.getItem() == Item.getItemFromBlock(RegisteredBlocks.blockNode)) && (item.getItemDamage() == 0);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper != ItemRendererHelper.EQUIPPED_BLOCK;
    }
}
